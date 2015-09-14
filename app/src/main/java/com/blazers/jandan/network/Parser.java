package com.blazers.jandan.network;

import android.content.Context;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.models.jandan.ImagePost;
import com.blazers.jandan.models.jandan.JokePost;
import com.blazers.jandan.models.jandan.NewsPost;
import com.blazers.jandan.util.NetworkHelper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;

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

    /* APIs */

    /**
     *
     *@param page 本地与远程同步的page仅仅显示后再放入DB中？重新设计DB 当该页已经不会变化的时候再从本地读取页面JSON信息 否则则全部从网络请求数据
     *
     *@param type 获取的类型 目前仅有 无聊图 妹子图
     *
     * */
    public Observable<List<Image>> getPictureData(int page, String type) {
        return Observable.create(subscriber -> {
            subscriber.onNext(getPostPictures(page, type));
            subscriber.onCompleted();
        });
    }


    private List<Image> getPostPictures(int page, String type) {
        List<Image> imageList = new ArrayList<>();
        // 检查数据库标志位是否已经完成本地缓存
        Realm realm = Realm.getInstance(context);
        // 本地加载
        if (NetworkHelper.isPhoneInOfflineMode()) {
            imageList = ImagePost.getAllImages(realm, page, type);
        } else {
            try {
                // 网络加载
                Request request = new Request.Builder()
                        .url(URL.getJandanAPIByPageAndType(page, type))
                        .build();
                String json = client.newCall(request).execute().body().string();
                List<ImagePost> postses = new ArrayList<>();
                JSONObject object = new JSONObject(json);
                JSONArray comments = object.getJSONArray("comments");

                /* 数据库读写操作放入线程中执行 首先返回数据? */
                for (int i = 0 ; i < comments.length() ; i ++) {
                    /* ImagePost */
                    JSONObject comment = comments.getJSONObject(i);
                    ImagePost post = gson.fromJson(comment.toString(), ImagePost.class);
                    /* Image */
                    JSONArray pics = comment.getJSONArray("pics");
                    RealmList<Image> images = new RealmList<>();
                    for (int pi = 0 ; pi < pics.length() ; pi ++) {
                        Image image = new Image();
                        image.setId(Long.parseLong(post.getComment_ID() + (type.equals("wuliao")?"0":"1") +String.format("%02d", pi)));
                        image.setUrl(pics.getString(pi));
                        images.add(image);
                        image.setPost(post);
                    }
                    post.setImages(images);
                    postses.add(post);
                    /* 用于返回 */
                    imageList.addAll(images);
                }
                /* TODO: DB IO 操作 后期整理至其他Package  新线程写入DB */
                new Thread(()->{
                    Realm threadRealm = Realm.getInstance(context);
                    threadRealm.beginTransaction();
                    for (ImagePost post : postses) {
                        threadRealm.copyToRealmOrUpdate(post);
                        for (Image image : post.getImages()) {
                            threadRealm.copyToRealmOrUpdate(image);
                        }
                    }
                    /* 判断是否需要更新Flag表 */
                    threadRealm.commitTransaction();
                    threadRealm.close();
                }).start();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        realm.close();
        return imageList;
    }

    /**
     *@param page 读取新鲜事的页码
     *
     * */
    public Observable<List<NewsPost>> getNewsData(int page) {
        return Observable.create(subscriber -> {
            subscriber.onNext(getPostNews(page));
            subscriber.onCompleted();
        });
    }

    private List<NewsPost> getPostNews(int page) {
        List<NewsPost> newsPostList = new ArrayList<>();
        Realm realm = Realm.getInstance(context);
        // 本地加载
        if (NetworkHelper.isPhoneInOfflineMode()) {
            newsPostList = NewsPost.getAllPost(realm, page);
        } else {
            try {
                Request request = new Request.Builder()
                        .url(URL.getJandanNewsAtPage(page))
                        .build();
                String json = client.newCall(request).execute().body().string();
                JSONObject object = new JSONObject(json);
                JSONArray posts = object.getJSONArray("posts");
                List<NewsPost> newsPostListTemp = new ArrayList<>();
                for (int i = 0 ; i < posts.length() ; i ++) {
                    JSONObject post = posts.getJSONObject(i);
                    NewsPost newsPost = new NewsPost();
                    newsPost.setPage(page);
                    newsPost.setId(post.getLong("id"));
                    newsPost.setTitle(post.getString("title"));
                    newsPost.setUrl(post.getString("url"));
                    newsPost.setAuthor(post.getJSONObject("author").getString("name"));
                    newsPost.setDate(post.getString("date"));
                    newsPost.setTagTitle(post.getJSONArray("tags").getJSONObject(0).getString("title"));
                    newsPost.setThumbUrl(post.getJSONObject("custom_fields").getJSONArray("thumb_c").getString(0));
                    newsPost.setViews(post.getJSONObject("custom_fields").getJSONArray("views").getLong(0));
                    newsPostListTemp.add(newsPost);
                }
                newsPostList.addAll(newsPostListTemp);
                /* TODO: Merge db operate */
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
                e.printStackTrace();
            }
        }
        realm.close();
        return newsPostList;
    }


    public Observable<NewsPost> getNewsContentData(long id) {
        return Observable.create(subscriber -> {
            subscriber.onNext(getNewsContent(id));
            subscriber.onCompleted();
        });
    }

    private NewsPost getNewsContent(long id) {
        Realm realm = Realm.getInstance(context);
        NewsPost post = NewsPost.getPostById(realm, id);
        if (post == null) {
            throw new IllegalStateException("Couldn't find news post relate to id : " + id);
        }
        String html = post.getHtml();
        if (html == null || html.equals("")) {
            try {
                Request request = new Request.Builder()
                        .url(URL.getJandanNewsContentById(id))
                        .build();
                String json = client.newCall(request).execute().body().string();
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
                post.setHtml(html);
                realm.commitTransaction();
                post = NewsPost.copySimplePost(post); // RealmObject which create by Realm can not cross thread and context
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        realm.close();
        return post;
    }


    /**
     *
     *
     * */
    public Observable<List<JokePost>> getJokeData(int page) {
        return Observable.create(subscriber -> {
            subscriber.onNext(getPostJokes(page));
            subscriber.onCompleted();
        });
    }

    private List<JokePost> getPostJokes(int page) {
        List<JokePost> jokePostList = new ArrayList<>();
        Realm realm = Realm.getInstance(context);
        if (NetworkHelper.isPhoneInOfflineMode()) {
            jokePostList = JokePost.getAllPost(realm, page);
        } else {
            try {
                Request request = new Request.Builder()
                        .url(URL.getJandanJokeAtPage(page))
                        .build();
                String json = client.newCall(request).execute().body().string();
                JSONObject object = new JSONObject(json);
                JSONArray comments = object.getJSONArray("comments");
                List<JokePost> jokePostListTemp = new ArrayList<>();
                for (int i = 0 ; i < comments.length() ; i ++) {
                    JokePost jokePost = gson.fromJson(comments.getJSONObject(i).toString(), JokePost.class);
                    jokePostListTemp.add(jokePost);
                }
                jokePostList.addAll(jokePostListTemp);
                /* TODO: Merge db operate */
                new Thread(()->{
                    Realm threadRealm = Realm.getInstance(context);
                    threadRealm.beginTransaction();
                    for (JokePost post : jokePostListTemp) {
                        threadRealm.copyToRealmOrUpdate(post);
                    }
                    /* 判断是否需要更新Flag表 */
                    threadRealm.commitTransaction();
                    threadRealm.close();
                }).start();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        realm.close();
        return jokePostList;
    }
}
