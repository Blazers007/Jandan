package com.blazers.jandan.model;

import android.content.Context;
import android.util.Log;

import com.blazers.jandan.api.IJandan;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.model.extension.Time;
import com.blazers.jandan.model.image.ImagePage;
import com.blazers.jandan.model.image.SingleImage;
import com.blazers.jandan.model.joke.JokePage;
import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.util.LoggintInterceptor;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.TimeHelper;
import com.google.gson.Gson;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private WeakReference<Context> mApplicationContext;
    private OkHttpClient client;
    private Gson gson;
    private IJandan mJandan;
    //==============================================================================================
    //=
    //=
    //=                                 数据库管理
    //=
    //=
    //==============================================================================================
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
        // 单一容量线程池
        mDatabaseExecutor = Executors.newSingleThreadExecutor();

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
     * @param context
     */
    public void init(Context context) throws SnappydbException {
        mDB = DBFactory.open(context);
        // Init executor
        mDatabaseExecutor = Executors.newSingleThreadExecutor();
        // Cache context
        mApplicationContext = new WeakReference<Context>(context);
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
    public Observable<List<NewsPage.Posts>> getNewsData(int page) {
        Log.d("DataManager", "==GetNewsData==");
        if (NetworkHelper.netWorkAvailable(mApplicationContext.get())) {
            Log.i("Loading", "From API");
            return mJandan.getNews(page)
                    .doOnNext(newsPage -> mDatabaseExecutor.execute(new DatabaseSavingTask<NewsPage>(NEWS + page, newsPage)))
                    .map(newsPage ->  newsPage.posts);
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
            try {
                Future<NewsPage> ret = mDatabaseExecutor.submit(new DatabaseQueryTask<>(NEWS + page, NewsPage.class));
                NewsPage newsPage = ret.get(5, TimeUnit.SECONDS);
                return Observable.just(newsPage.posts);
            } catch (InterruptedException e) {
                return Observable.error(e);
            } catch (ExecutionException e) {
                return Observable.error(e);
            } catch (TimeoutException e) {
                return Observable.just(null);
            } catch (Exception e) {
                return Observable.error(e);
            }
        });
    }

    /**
     * @param page 读取无聊图的页码
     */
    public Observable<List<SingleImage>> getWuliaoData(int page) {
        Log.d("DataManager", "==GetJokeData==");
        if (NetworkHelper.netWorkAvailable(mApplicationContext.get())) {
            Log.i("Loading", "From API");
            return mJandan.getWuliao(page)
                    .doOnNext(wuliaoPage -> mDatabaseExecutor.execute(new DatabaseSavingTask<ImagePage>(WULIAO + page, wuliaoPage)))
                    .map(wuliaoPage -> SingleImage.splitToSingle(wuliaoPage.comments));
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
            try {
                Future<ImagePage> ret = mDatabaseExecutor.submit(new DatabaseQueryTask<>(WULIAO + page, ImagePage.class));
                ImagePage imagePage = ret.get(5, TimeUnit.SECONDS);
                return Observable.just(SingleImage.splitToSingle(imagePage.comments));
            } catch (InterruptedException e) {
                return Observable.error(e);
            } catch (ExecutionException e) {
                return Observable.error(e);
            } catch (TimeoutException e) {
                return Observable.just(null);
            } catch (Exception e) {
                return Observable.error(e);
            }
        });
    }


    /**
     * @param page 读取段子的页码
     */
    public Observable<List<JokePage.Comments>> getJokeData(int page) {
        Log.d("DataManager", "==GetJokeData==");
        if (NetworkHelper.netWorkAvailable(mApplicationContext.get())) {
            Log.i("Loading", "From API");
            return mJandan.getJoke(page)
                    .doOnNext(jokePage -> mDatabaseExecutor.execute(new DatabaseSavingTask<JokePage>(JOKE + page, jokePage)))
                    .map(jokePage -> jokePage.comments);
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
            try {
                Future<JokePage> ret = mDatabaseExecutor.submit(new DatabaseQueryTask<>(JOKE + page, JokePage.class));
                JokePage jokePage = ret.get(5, TimeUnit.SECONDS);
                return Observable.just(jokePage.comments);
            } catch (InterruptedException e) {
                return Observable.error(e);
            } catch (ExecutionException e) {
                return Observable.error(e);
            } catch (TimeoutException e) {
                return Observable.just(null);
            } catch (Exception e) {
                return Observable.error(e);
            }
        });
    }

    /**
     * @param page 读取妹纸图的页码
     */
    public Observable<List<SingleImage>> getMeizhiData(int page) {
        Log.d("DataManager", "==GetMeizhi==");
        if (NetworkHelper.netWorkAvailable(mApplicationContext.get())) {
            return mJandan.getMeizhi(page)
                    .doOnNext(meizhiPage -> mDatabaseExecutor.execute(new DatabaseSavingTask<ImagePage>(MEIZHI + page, meizhiPage)))
                    .map(meizhiPage ->  SingleImage.splitToSingle(meizhiPage.comments));
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
                Future<ImagePage> ret = mDatabaseExecutor.submit(new DatabaseQueryTask<>(MEIZHI + page, ImagePage.class));
                ImagePage imagePage = ret.get(5, TimeUnit.SECONDS);
                return Observable.just(SingleImage.splitToSingle(imagePage.comments));
            } catch (InterruptedException e) {
                return Observable.error(e);
            } catch (ExecutionException e) {
                return Observable.error(e);
            } catch (TimeoutException e) {
                return Observable.just(null);
            } catch (Exception e) {
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


    /**
     * 存储数据
     * @param <T>
     */
    private class DatabaseSavingTask<T> implements Runnable {

        private String mKey;
        private T mData;

        DatabaseSavingTask(String key, T data) {
            this.mKey = key;
            mData = data;
        }

        @Override
        public void run() {
            try {
                mDB.put(mKey, mData);
            } catch (SnappydbException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取数据
     * @param <T>
     */
    private class DatabaseQueryTask<T> implements Callable<T> {

        private String mKey;
        private Class<T> mTClass;

        DatabaseQueryTask(String key, Class<T> TClass) {
            mKey = key;
            mTClass = TClass;
        }

        @Override
        public T call() throws Exception {
            if (mDB.exists(mKey)) {
                return mDB.getObject(mKey, mTClass);
            }
            return null;
        }
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
        // 关闭线程池
        mDatabaseExecutor.shutdown();
    }
}
