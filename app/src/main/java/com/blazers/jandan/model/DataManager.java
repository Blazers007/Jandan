package com.blazers.jandan.model;

import android.content.Context;
import android.util.Log;

import com.blazers.jandan.api.IJandan;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.model.image.ImagePage;
import com.blazers.jandan.model.image.SingleImage;
import com.blazers.jandan.model.joke.JokePage;
import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.util.LoggintInterceptor;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.TimeHelper;
import com.google.gson.Gson;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Blazers on 2015/9/11.
 * 访问煎蛋API接口与解析返回结果的工具类
 * <p>
 * 2.0.0 版本采用 SnappyDb 数据库 为了增加查找速度 分为如下几个数据库
 * 1.内容缓存库 [Main]  -> [N:id] [J:id] [W:id] [M:id]
 * 2.图片映射库 [Img_Mapping]->  [N:id] [J:id] [W:id] [M:id]
 * 3.收藏库   [Fav]  -> [N:id] [J:id] [W:id] [M:id]
 */
public class DataManager {
    // 缓存
    public static final String TAG = "[Parser]";
    public static final String NEWS = "N:";
    public static final String WULIAO = "W:";
    public static final String JOKE = "J:";
    public static final String MEIZHI = "M:";
    // 收藏
    public static final String FAV_NEWS = "F_N:";
    public static final String FAV_WULIAO = "F_W:";
    public static final String FAV_JOKE = "F_J:";
    public static final String FAV_MEIZHI = "F_M:";
    // 图片本地映射
    public static final String IMG_MAPPING = "MAP:";
    // Lock
    private static final Object monitor = new Object();
    // Instance
    private static DataManager INSTANCE;
    // Vars
    private OkHttpClient client;
    private Gson gson;
    private IJandan mJandan;
    // Database
    private ExecutorService mDatabaseExecutor;
    private DB mDB;

    private DataManager() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new LoggintInterceptor())
                .build();
        /* Init Gson  */
        gson = new Gson();
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
     * 设置数据库
     *
     * @param db
     */
    public void setDB(DB db) {
        mDB = db;
        // Init executor
        mDatabaseExecutor = Executors.newSingleThreadExecutor();
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
    public Observable<List<NewsPage.Posts>> getNewsData(Context context, int page) {
        Log.d("DataManager", "==GetNewsData==");
        if (NetworkHelper.netWorkAvailable(context)) {
            Log.i("Loading", "From API");
            return mJandan.getNews(page)
                    .map(newsPage -> {
                        synchronized (monitor) {
                            try {
                                Log.i("Saving News", System.currentTimeMillis() + "");
                                mDB.put(NEWS + page, newsPage);
                                Log.i("Saving News", System.currentTimeMillis() + "");
                            } catch (SnappydbException e) {
                                e.printStackTrace();
                            }
                            return newsPage.posts;
                        }
                    });
        } else {
            return getNewsDataFromDB(page);
        }
    }

    /**
     * @param page 读取新鲜事的页码
     */
    public Observable<List<NewsPage.Posts>> getNewsDataFromDB(int page) {
        Log.d("DataManager", "==GetNewsDataFromDB==");
        return Observable.defer(() -> {
            synchronized (monitor) {
                try {
                    if (mDB.exists(NEWS + page)) {
                        NewsPage newsPage = mDB.getObject(NEWS + page, NewsPage.class);
                        return Observable.just(newsPage.posts);
                    } else {
                        // 如何编写效率最高
                        return Observable.just(null);
                    }
                } catch (SnappydbException e) {
                    return Observable.error(e);
                }
            }

        });
    }

    /**
     * @param page 读取无聊图的页码
     */
    public Observable<List<SingleImage>> getWuliaoData(Context context, int page) {
        Log.d("DataManager", "==GetJokeData==");
        if (NetworkHelper.netWorkAvailable(context)) {
            Log.i("Loading", "From API");
            return mJandan.getWuliao(page)
                    .map(wuliaoPage -> {
                        synchronized (monitor) {
                            try {
                                mDB.put(WULIAO + page, wuliaoPage);
                            } catch (SnappydbException e) {
                                e.printStackTrace();
                            }
                            return SingleImage.splitToSingle(wuliaoPage.comments);
                        }
                    });
        } else {
            return getWuliaoDataFromDB(page);
        }
    }

    /**
     * @param page 读取无聊图的页码
     */
    public Observable<List<SingleImage>> getWuliaoDataFromDB(int page) {
        Log.d("DataManager", "==GetJokeDataFromDB==");
        return Observable.defer(() -> {
            synchronized (monitor) {
                try {
                    if (mDB.exists(JOKE + page)) {
                        ImagePage wuliaoPage = mDB.getObject(WULIAO + page, ImagePage.class);
                        return Observable.just(wuliaoPage.comments).map(SingleImage::splitToSingle);
                    } else {
                        return Observable.just(null);
                    }
                } catch (SnappydbException e) {
                    return Observable.error(e);
                }
            }
        });
    }


    /**
     * @param page 读取段子的页码
     */
    public Observable<List<JokePage.Comments>> getJokeData(Context context, int page) {
        Log.d("DataManager", "==GetJokeData==");
        if (NetworkHelper.netWorkAvailable(context)) {
            Log.i("Loading", "From API");
            return mJandan.getJoke(page)
                    .map(jokePage -> {
                        synchronized (monitor) {
                            try {
                                mDB.put(JOKE + page, jokePage);
                            } catch (SnappydbException e) {
                                e.printStackTrace();
                            }
                            return jokePage.comments;
                        }
                    });
        } else {
            return getJokeDataFromDB(page);
        }
    }

    /**
     * @param page 读取段子的页码
     */
    public Observable<List<JokePage.Comments>> getJokeDataFromDB(int page) {
        Log.d("DataManager", "==GetJokeDataFromDB==");
        return Observable.defer(() -> {
            synchronized (monitor) {
                try {
                    if (mDB.exists(JOKE + page)) {
                        JokePage jokePage = mDB.getObject(JOKE + page, JokePage.class);
                        return Observable.just(jokePage.comments);
                    } else {
                        return Observable.just(null);
                    }
                } catch (SnappydbException e) {
                    return Observable.error(e);
                }
            }

        });
    }

    /**
     * @param page 读取妹纸图的页码
     */
    public Observable<List<SingleImage>> getMeizhiData(Context context, int page) {
        Log.d("DataManager", "==GetMeizhi==");
        if (NetworkHelper.netWorkAvailable(context)) {
            return mJandan.getMeizhi(page)
                    .map(meizhiPage -> {
                        try {
                            mDB.put(MEIZHI + page, meizhiPage);
                        } catch (SnappydbException e) {
                            e.printStackTrace();
                        }
                        return SingleImage.splitToSingle(meizhiPage.comments);
                    });
        } else {
            return getMeizhiDataFromDB(page);
        }
    }


    /**
     * @param page 读取妹纸图
     */
    public Observable<List<SingleImage>> getMeizhiDataFromDB(int page) {
        Log.d("DataManager", "==GetJokeDataFromDB==");
        return Observable.defer(() -> {
            try {
                if (mDB.exists(JOKE + page)) {
                    ImagePage meizhiPage = mDB.getObject(JOKE + page, ImagePage.class);
                    return Observable.just(meizhiPage.comments).map(SingleImage::splitToSingle);
                } else {
                    return Observable.just(null);
                }
            } catch (SnappydbException e) {
                return Observable.error(e);
            }
        });
    }

    /**
     * 根据文章ID获取评论列表
     */
    public Observable<Void> getCommentById(long id) {
        return Observable.create(subscriber -> {
            try {
                String json = simpleHttpRequest(URL.JANDAN_COMMENT_API + "comment-" + id);
//                Comments comments = gson.fromJson(json, Comments.class);
                subscriber.onNext(null);
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


    //==============================================================================================
    //=
    //=
    //=                                 收藏管理
    //=
    //=
    //==============================================================================================


    public void manageNewsFavorite(NewsPage.Posts posts, boolean favorite) {
        String key = FAV_NEWS + posts.id;
        manageFavorite(key, posts, favorite);
    }

    public void manageJokeFavorite(JokePage.Comments comments, boolean favorite) {
        String key = FAV_JOKE + comments.comment_ID;
        manageFavorite(key, comments, favorite);
    }

    /**
     * 采用KV对进行储存 速度快 空间占用更多
     */
    public void manageWuliaoFavorite(SingleImage singleImage, boolean favorite) {
        String key = FAV_WULIAO + singleImage.id;
        manageFavorite(key, singleImage, favorite);
    }

    public void manageMeizhiFavorite(SingleImage singleImage, boolean favorite) {
        String key = FAV_MEIZHI + singleImage.id;
        manageFavorite(key, singleImage, favorite);
    }

    public boolean isNewsFavorite(NewsPage.Posts posts) {
        String key = FAV_NEWS + posts.id;
        return isThisFavorite(key);
    }

    public boolean isJokeFavorite(JokePage.Comments comments) {
        String key = FAV_JOKE + comments.comment_ID;
        return isThisFavorite(key);
    }

    public boolean isWuliaoFavorite(SingleImage singleImage) {
        String key = FAV_WULIAO + singleImage.id;
        return isThisFavorite(key);
    }

    public boolean isMeizhiFavorite(SingleImage singleImage) {
        String key = FAV_MEIZHI + singleImage.id;
        return isThisFavorite(key);
    }


    private boolean isThisFavorite(String key) {
        try {
            return mDB.exists(key);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param key
     * @param data
     * @param favorite
     * @param <T>
     */
    private <T extends Time> void manageFavorite(String key, T data, boolean favorite) {
        try {
            if (favorite) {
                // 添加到喜欢
                data.time = TimeHelper.currentTime();
                mDB.put(key, data);
            } else {
                // 移除喜欢
                if (mDB.exists(key)) {
                    mDB.del(key);
                }
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        // 关闭数据库
        try {
            if (mDB != null && mDB.isOpen()) {
                mDB.close();
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
}
