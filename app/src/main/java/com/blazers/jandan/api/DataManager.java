package com.blazers.jandan.api;

import android.content.Context;
import android.util.Log;

import com.blazers.jandan.common.URL;
import com.blazers.jandan.model.database.local.LocalArticleHtml;
import com.blazers.jandan.model.database.sync.ImagePost;
import com.blazers.jandan.model.database.sync.JokePost;
import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.model.pojo.comment.Comments;
import com.blazers.jandan.util.NetworkHelper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmObject;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Blazers on 2015/9/11.
 * 访问煎蛋API接口与解析返回结果的工具类
 */
public class DataManager {

    public static final String TAG = "[Parser]";
    private static final Object monitor = new Object();
    private static DataManager INSTANCE;
    private OkHttpClient client;
    private Gson gson;
    private IJandan mJandan;


    private DataManager() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

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

        // Init api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IJandan.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mJandan = retrofit.create(IJandan.class);
    }

    public static DataManager getInstance() {
        synchronized (monitor) {
            if (INSTANCE == null) {
                INSTANCE = new DataManager();
            }
            return INSTANCE;
        }
    }


    /**
     * 根据URL采用OkHttp访问网络并返回字符串数据
     */
    private String simpleHttpRequest(String url) throws IOException {
        Log.i("[Connecting]", url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * POST数据
     */
    private String simpleHttpPostRequest(String url, RequestBody body) throws IOException {
        Log.i("[Connecting Post]", url);
        /* KV */
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * @param page 读取新鲜事的页码
     */
    public Observable<NewsPage> getNewsData(Context context, int page) {
        return getNewsData(false, page, context);
    }

    /**
     * @param page 读取新鲜事的页码
     */
    public Observable<NewsPage> getNewsDataFromDB(int page) {
        return getNewsData(true, page, null);
    }

    /**
     * @param page 读取新鲜事的页码
     */
    private Observable<NewsPage> getNewsData(boolean forceFromDB, int page, Context context) {
        if (forceFromDB || !NetworkHelper.netWorkAvailable(context)) {
            Log.i("Loading", "From DB : " + Realm.getDefaultInstance().where(NewsPage.class).count());
            return Realm.getDefaultInstance().where(NewsPage.class)
//                    .equalTo("current_page", page)
                    .findFirstAsync()
                    .asObservable();
        } else {
            Log.i("Loading", "From API");
            return mJandan.getNews(page)
                    .doOnNext(newsPage -> {
                        Realm.getDefaultInstance().executeTransaction(realm -> {
                            newsPage.current_page = page;
                            NewsPage newsPage1 = realm.createOrUpdateObjectFromJson(NewsPage.class, gson.toJson(newsPage));
                            realm.copyToRealmOrUpdate(newsPage1);
                        });
                    }).subscribeOn(Schedulers.io())
                    ;
        }
    }


    /**
     * @param page 本地与远程同步的page仅仅显示后再放入DB中？重新设计DB 当该页已经不会变化的时候再从本地读取页面JSON信息 否则则全部从网络请求数据
     * @param type 获取的类型 目前仅有 无聊图 妹子图 // 自动跳过重复页面 并发出有新的刷新的提示！
     */
    public Observable<List<ImagePost>> getPictureData(int page, String type) {
        return Observable.create((Subscriber<? super List<ImagePost>> subscriber) -> {
//            // 否则请求网络
//            List<ImagePost> postses = new ArrayList<>();
//            try {
//                String json = simpleHttpRequest(URL.getJandanAPIByPageAndType(page, type));
//                JSONObject object = new JSONObject(json);
//                JSONArray comments = object.getJSONArray("comments");
//                String commentInfoUrl = URL.JANDAN_COMMENT_COUNT;
//                /* 广播总数 */
//                long totalCount = object.getLong("total_comments");
//                context.get().sendBroadcast(
//                    new Intent(RightDownloadingFragment.ACTION_COUNT)
//                        .putExtra("data", new Count(type.equals("wuliao") ? Count.WULIAO : Count.MEIZI, totalCount))
//                );
//                for (int i = 0; i < comments.length(); i++) {
//                        /* ImagePost */
//                    JSONObject comment = comments.getJSONObject(i);
//                    ImagePost post = gson.fromJson(comment.toString(), ImagePost.class);
//                    commentInfoUrl += ("comment-" + post.getComment_ID() + ",");
//                        /* Image */
//                    StringBuilder sb = new StringBuilder();
//                    JSONArray pics = comment.getJSONArray("pics");
//                    for (int pi = 0; pi < pics.length(); pi++) {
//                        sb.append(pics.getString(pi));
//                        if (pi != pics.length() - 1)
//                            sb.append(",");
//                    }
//                    post.setPage(page);
//                    post.setType(type);
//                    post.setPicsArray(sb.toString());
//                    postses.add(post);
//                }
//                /* 请求评论数量 */
//                String commentInfo = simpleHttpRequest(commentInfoUrl.substring(0, commentInfoUrl.length() - 1));
//                JSONObject commentJSON = new JSONObject(commentInfo).getJSONObject("response");
//                for (ImagePost post : postses) {
//                    String key = "comment-" + post.getComment_ID();
//                    if (commentJSON.has(key)) {
//                        int commentNumber = commentJSON.getJSONObject(key).getInt("comments");
//                        post.setCommentNumber(commentNumber);
//                    }
//                }
//                subscriber.onNext(postses);
//            } catch (IOException | JSONException e) {
//                subscriber.onError(e);
//            }
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io());
    }


    /**
     * 根据文章的ID读取文章的信息
     * <p>
     * 如果是离线模式则在Fragment中进行处理?
     */
    public Observable<LocalArticleHtml> getNewsContentData(long id) {
        return Observable.create(subscriber -> {
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
                // 本地缓存
                LocalArticleHtml localArticleHtml = new LocalArticleHtml();
                localArticleHtml.setId(id);
                localArticleHtml.setHtml(sb.toString());
                subscriber.onNext(localArticleHtml);
            } catch (JSONException | IOException e) {
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        });
    }


    /**
     * 按照页码读取段子信息
     */
    public Observable<List<JokePost>> getJokeData(int page) {
        return Observable.create((Subscriber<? super List<JokePost>> subscriber) -> {
//            List<JokePost> jokePostList = new ArrayList<>();
//            try {
//                String json = simpleHttpRequest(URL.getJandanJokeAtPage(page));
//                JSONObject object = new JSONObject(json);
//                JSONArray comments = object.getJSONArray("comments");
//                /* 广播总数 */
//                long totalCount = object.getLong("total_comments");
//                context.get().sendBroadcast(new Intent(RightDownloadingFragment.ACTION_COUNT).putExtra("data", new Count(Count.JOKE, totalCount)));
//                /* 评论数量URL */
//                String commentInfoUrl = URL.JANDAN_COMMENT_COUNT;
//                for (int i = 0; i < comments.length(); i++) {
//                    JokePost jokePost = gson.fromJson(comments.getJSONObject(i).toString(), JokePost.class);
//                    jokePost.setPage(page);
//                    jokePostList.add(jokePost);
//                    commentInfoUrl += ("comment-" + jokePost.getComment_ID() + ",");
//                }
//                /* 获取数量 */
//                String commentInfo = simpleHttpRequest(commentInfoUrl.substring(0, commentInfoUrl.length() - 1));
//                JSONObject commentJSON = new JSONObject(commentInfo).getJSONObject("response");
//                for (JokePost post : jokePostList) {
//                    String key = "comment-" + post.getComment_ID();
//                    if (commentJSON.has(key)) {
//                        int commentNumber = commentJSON.getJSONObject(key).getInt("comments");
//                        post.setCommentNumber(commentNumber);
//                    }
//                }
//                subscriber.onNext(jokePostList);
//            } catch (IOException | JSONException e) {
//                subscriber.onError(e);
//            }
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io());
    }


    /**
     * 根据文章ID获取评论列表
     */
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


    // --------------------提交数据

    /**
     * 投票
     */
    public Observable<Boolean> voteByCommentIdAndVote(long id, boolean vote) {
        return Observable.create(subscriber -> {
            try {
                RequestBody body = new FormBody.Builder()
                        .add("ID", id + "")
                        .build();
                String str = simpleHttpPostRequest(URL.JANDAN_VOTE_API + (vote ? "1" : "0"), body);
                Log.i("Vote", str);
                subscriber.onNext(true);
            } catch (IOException e) {
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        });
    }
}
