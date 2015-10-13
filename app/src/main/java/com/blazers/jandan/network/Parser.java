package com.blazers.jandan.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.models.jandan.Post;
import com.blazers.jandan.models.jandan.JokePost;
import com.blazers.jandan.models.jandan.news.NewsPost;
import com.blazers.jandan.models.jandan.comment.Comments;
import com.blazers.jandan.services.DownloadService;
import com.blazers.jandan.util.NetworkHelper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Blazers on 2015/9/11.
 */
public class Parser {

    public static final String TAG = "[Parser]";
    protected static final Object monitor = new Object();
    private static Parser INSTANCE;
    private static Context context;
    private OkHttpClient client;
    private Gson gson;

    public static void init(Context context) {
        Parser.context = context;
    }

    private Parser() {
        if (context == null) {
            throw new IllegalStateException("You must call static method Parser.Init(context) first");
        }
        client = new OkHttpClient();
        client.setReadTimeout(12, TimeUnit.SECONDS);

        /* Init Gson  */
        gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

    public static Parser getInstance() {
        synchronized (monitor) {
            if (INSTANCE == null) {
                INSTANCE = new Parser();
            }
            return INSTANCE;
        }
    }


    /**
     * 根据URL采用OkHttp访问网络并返回字符串数据
     * */
    private String simpleHttpRequest(String url) throws IOException {
        Log.i("[Connecting]", url);
        Request request = new Request.Builder()
            .url(url)
            .build();
        Response response = client.newCall(request).execute();
        String str = response.body().string();
//        Log.i("[Response]", str);
        return str;
    }

    /**
     *
     *@param page 本地与远程同步的page仅仅显示后再放入DB中？重新设计DB 当该页已经不会变化的时候再从本地读取页面JSON信息 否则则全部从网络请求数据
     *
     *@param type 获取的类型 目前仅有 无聊图 妹子图
     *
     * */
    public Observable<List<Image>> getPictureData(int page, String type) {
        return Observable.create(subscriber -> {
            Realm realm = Realm.getInstance(context);
                try {
                    List<Image> imageList = new ArrayList<>();
                    String json = simpleHttpRequest(URL.getJandanAPIByPageAndType(page, type));
                    List<Post> postses = new ArrayList<>();
                    JSONObject object = new JSONObject(json);
                    JSONArray comments = object.getJSONArray("comments");

                    for (int i = 0 ; i < comments.length() ; i ++) {
                    /* Post */
                        JSONObject comment = comments.getJSONObject(i);
                        Post post = gson.fromJson(comment.toString(), Post.class);
                    /* Image */
                        JSONArray pics = comment.getJSONArray("pics");
                        RealmList<Image> images = new RealmList<>();
                        for (int pi = 0 ; pi < pics.length() ; pi ++) {
                            Image image = new Image();
                            image.id = Long.parseLong(post.comment_ID + (type.equals("wuliao")?"0":"1") +String.format("%02d", pi));
                            image.url = pics.getString(pi);
                            images.add(image);
                            image.post = post;
                        }
                        post.images = images;
                        postses.add(post);
                        imageList.addAll(images);
                    }
                    subscriber.onNext(imageList);
                    new Thread(()->{
                        Realm threadRealm = Realm.getInstance(context);
                        threadRealm.beginTransaction();
                        for (Post post : postses) {
                            threadRealm.copyToRealmOrUpdate(post);
                            for (Image image : post.images) {
                                threadRealm.copyToRealmOrUpdate(image);
                            }
                        }
                        threadRealm.commitTransaction();
                        threadRealm.close();
                    }).start();
                } catch (IOException | JSONException e) {
                    subscriber.onError(e);
                }
            realm.close();
            subscriber.onCompleted();
        });
    }


    /**
     *@param page 读取新鲜事的页码
     *
     * */
    public Observable<List<NewsPost>> getNewsData(int page) {
        return Observable.create(subscriber -> {
                List<NewsPost> newsPostList = new ArrayList<>();
                Realm realm = Realm.getInstance(context);
                try {
                    String json = simpleHttpRequest(URL.getJandanNewsAtPage(page));
                    JSONObject object = new JSONObject(json);
                    JSONArray posts = object.getJSONArray("posts");
                    List<NewsPost> newsPostListTemp = new ArrayList<>();
                    for (int i = 0 ; i < posts.length() ; i ++) {
                        JSONObject post = posts.getJSONObject(i);

                        NewsPost newsPost = gson.fromJson(post.toString(), NewsPost.class);
                        newsPost.page = page;
                        newsPost.author = post.getJSONObject("author").getString("name");
                        newsPost.tagTitle = post.getJSONArray("tags").getJSONObject(0).getString("title");
                        newsPost.thumbUrl = post.getJSONObject("custom_fields").getJSONArray("thumb_c").getString(0);
                        newsPost.views = post.getJSONObject("custom_fields").getJSONArray("views").getLong(0);
                        newsPostListTemp.add(newsPost);
                    }
                    newsPostList.addAll(newsPostListTemp);
                    subscriber.onNext(newsPostList);

                    new Thread(()->{
                        Realm threadRealm = Realm.getInstance(context);
                        threadRealm.beginTransaction();
                        for (NewsPost post : newsPostListTemp) {
                            threadRealm.copyToRealmOrUpdate(post);
                        }
                    /* 判断是否需要更新Flag表 */
                        threadRealm.commitTransaction();
                        threadRealm.close();
                    }).start();
                } catch (IOException | JSONException e) {
                    subscriber.onError(e);
                }
            realm.close();
            subscriber.onCompleted();
        });
    }



    /**
     * 根据文章的ID读取文章的信息
     * */
    public Observable<NewsPost> getNewsContentData(long id) {
        return Observable.create(subscriber -> {
            Realm realm = Realm.getInstance(context);
            NewsPost post = NewsPost.getPostById(realm, id);
            if (post == null) {
                throw new IllegalStateException("Couldn't find news post relate to id : " + id);
            }
            String html = post.html;
            if (html == null || html.equals("")) {
                try {

                    String json = simpleHttpRequest(URL.getJandanNewsContentById(id));
                    JSONObject object = new JSONObject(json);
                    String body = object.getJSONObject("post").getString("content");
                    StringBuilder sb = new StringBuilder();
                    sb.append("<!DOCTYPE html>");
                    sb.append("<html><body>");
                    sb.append("<head>");
                    sb.append("<link id=\"style\" rel=\"stylesheet\" type=\"text/css\" href=\"\" />");
                    sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/style.css\" />");
                    sb.append("<script src=\"file:///android_asset/js/main.js\" type=\"text/javascript\"></script>");
                    sb.append("</head>");
                    sb.append(body);
                    sb.append("</body></html>");
                    html = sb.toString();
                    realm.beginTransaction();
                    post.html = html;
                    realm.commitTransaction();
                    post = NewsPost.copySimplePost(post); // RealmObject which create by Realm can not cross thread and context
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
            realm.close();
            subscriber.onNext(post);
            subscriber.onCompleted();
        });
    }




    /**
     * 按照页码读取段子信息
     * */
    public Observable<List<JokePost>> getJokeData(int page) {
        return Observable.create(subscriber -> {
            List<JokePost> jokePostList = new ArrayList<>();
            Realm realm = Realm.getInstance(context);
            try {
                String json = simpleHttpRequest(URL.getJandanJokeAtPage(page));
                JSONObject object = new JSONObject(json);
                JSONArray comments = object.getJSONArray("comments");
                List<JokePost> jokePostListTemp = new ArrayList<>();
                for (int i = 0 ; i < comments.length() ; i ++) {
                    JokePost jokePost = gson.fromJson(comments.getJSONObject(i).toString(), JokePost.class);
                    jokePostListTemp.add(jokePost);
                }
                jokePostList.addAll(jokePostListTemp);
                subscriber.onNext(jokePostList);
                new Thread(()->{
                    Realm threadRealm = Realm.getInstance(context);
                    threadRealm.beginTransaction();
                    for (JokePost post : jokePostListTemp) {
                        threadRealm.copyToRealmOrUpdate(post);
                    }
                    threadRealm.commitTransaction();
                    threadRealm.close();
                }).start();
            } catch (IOException | JSONException e) {
                subscriber.onError(e);
            }
            realm.close();
            subscriber.onCompleted();
        });
    }


    /**
     * 根据文章ID获取评论列表
     * */
    public Observable<Comments> getCommentById(long id) {
        return Observable.create(subscriber -> {
            try {
                String json = simpleHttpRequest(URL.JANDAN_COMMENT_API + "comment-" + id);
                Comments comments = gson.fromJson(json, Comments.class);
                subscriber.onNext(comments);
            } catch (Exception e) {
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        });
    }

    /**
     * 离线下载的API <测试中>
     * */
    public void offlineNews() {

    }

    public void offlinePics() {

    }

    private int offline = 0;
    public void offlineMeizi(int start, int pages) {
        /* 根据页码更新数据库 */
        List<Observable<List<Image>>> requestList = new ArrayList<>();
        for (int i = start ; i < pages; i ++) {
            requestList.add(getPictureData(i, "meizi"));
        }
        Observable.from(requestList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    download(result);
                }, throwable -> {
                    Log.e("DOWNLOAD", throwable.toString());
                });

    }

    public void offlineJokes() {}

    public void download(Observable<List<Image>>  images) {
        images
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(sub -> {
                    for (Image image : sub) {
                        Intent intent = new Intent(context, DownloadService.class);
                        intent.putExtra("id", image.id);
                        intent.putExtra("url", image.url);
                        context.startService(intent);
                    }
                }, throwable -> {
                    Log.e("DOWNLOAD", throwable.toString());
                });
    }

}
