package com.blazers.jandan.model;

import android.content.Context;
import android.support.annotation.MainThread;

import com.blazers.jandan.api.IJandan;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.model.image.ImageComment;
import com.blazers.jandan.model.image.SingleImage;
import com.blazers.jandan.model.joke.JokeComment;
import com.blazers.jandan.model.news.NewsPost;
import com.blazers.jandan.ui.fragment.readingsub.JokeFragment;
import com.blazers.jandan.ui.fragment.readingsub.NewsFragment;
import com.blazers.jandan.util.LoggintInterceptor;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.util.log.Log;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.snappydb.SnappydbException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    private static final String TAG = "DataManager";
    private static final int TYPE_WULUAO = 0;
    private static final int TYPE_MEIZHI = 1;

    // Lock
    private static final Object monitor = new Object();
    // 超时
    private static int TIMEOUT = 10;
    // Instance
    private static DataManager INSTANCE;
    // Vars
    private WeakReference<Context> mApplicationContext;
    private OkHttpClient client;
    private Gson gson;
    private IJandan mJandan;
    private Realm mRealm;

    private DataManager() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new LoggintInterceptor())
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
                .registerTypeAdapter(new TypeToken<RealmList<RealmString>>() {
                }.getType(), new TypeAdapter<RealmList<RealmString>>() {

                    @Override
                    public void write(JsonWriter out, RealmList<RealmString> value) throws IOException {
                        // 目前暂时没有 Object 到 JsonString的 需要 所以目前不需要复写
                    }

                    @Override
                    public RealmList<RealmString> read(JsonReader in) throws IOException {
                        RealmList<RealmString> realmStrings = new RealmList<>();
                        in.beginArray();
                        while (in.hasNext()) {
                            realmStrings.add(new RealmString(in.nextString()));
                        }
                        in.endArray();
                        return realmStrings;
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
     * 设置数据库
     *
     * @param context
     */
    public void init(Context context) throws SnappydbException {
        // Cache context
        mApplicationContext = new WeakReference<>(context);
        // Open database
        mRealm = Realm.getDefaultInstance();
    }

    private void test() {
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
    public Observable<List<NewsPost>> getNewsData(int page) {
        Log.d(TAG, "==GetNewsData==");
        Observable<List<NewsPost>> observable;
        if (NetworkHelper.netWorkAvailable(mApplicationContext.get())) {
            observable = mJandan.getNews(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(news -> news != null && news.posts != null)
                    .map(data -> data.posts)
                    .doOnNext(list -> mRealm.executeTransaction(realm -> {
                        // 记录时间
                        SPHelper.setLastRefreshTime(mApplicationContext.get(), NewsFragment.class.getSimpleName());
                        // 清除旧数据 添加新数据
                        RealmResults<NewsPost> ret = mRealm.where(NewsPost.class).equalTo("page", page).findAll();
                        for (NewsPost oldPost : ret) {
                            oldPost.page = 0;
                        }
                        for (NewsPost newPost : list) {
                            newPost.page = page;
                        }
                        mRealm.copyToRealmOrUpdate(list);
                    })
            );
        } else {
            observable = Observable.just(getNewsDataFromDB(page));
        }
        return observable.filter(ret -> ret != null).timeout(TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * @param page 读取新鲜事的页码
     */
    public List<NewsPost> getNewsDataFromDB(int page) {
        Log.d(TAG, "==GetNewsDataFromDB START ==");
        return mRealm.where(NewsPost.class).equalTo("page", page).findAllSorted("date", Sort.DESCENDING);
    }

    /**
     * 获取指定ID的文章内容
     *
     * @param id
     * @return
     */
    public NewsPost getNewsPost(int id) {
        return mRealm.where(NewsPost.class).equalTo("id", id).findFirst();
    }

    /**
     * @param page 读取无聊图的页码
     */
    public Observable<List<SingleImage>> getWuliaoData(int page) {
        return getImageData(page, IJandan.TYPE_WULIAO, TYPE_WULUAO);
    }

    /**
     * @param page 读取无聊图的页码
     */
    public List<SingleImage> getWuliaoDataFromDB(int page) {
        Log.d(TAG, "==GetJokeDataFromDB==");
        return getImageFromDB(page, TYPE_WULUAO);
    }


    /**
     * @param page 读取段子的页码
     */
    public Observable<List<JokeComment>> getJokeData(int page) {
        Log.d(TAG, "==GetJokeData==");
        Observable<List<JokeComment>> observable;
        if (NetworkHelper.netWorkAvailable(mApplicationContext.get())) {
            Log.i("Loading", "From API");
            observable = mJandan.getJoke(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(joke -> joke != null && joke.comments != null)
                    .map(data -> data.comments)
                    .doOnNext(list -> {
                        // 记录时间
                        SPHelper.setLastRefreshTime(mApplicationContext.get(), JokeFragment.class.getSimpleName());
                        // 清除旧数据 添加新数据
                        RealmResults<JokeComment> ret = mRealm.where(JokeComment.class).equalTo("page", page).findAllAsync();
                        mRealm.executeTransaction(realm -> {
                            for (JokeComment oldPost : ret) {
                                oldPost.page = 0;
                            }
                            for (JokeComment newPost : list) {
                                newPost.page = page;
                            }
                            mRealm.copyToRealmOrUpdate(list);
                        });
                    });
        } else {
            observable = Observable.just(getJokeDataFromDB(page));
        }
        return observable.filter(ret -> ret != null).timeout(TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * @param page 读取段子的页码
     */
    public List<JokeComment> getJokeDataFromDB(int page) {
        Log.d(TAG, "==GetJokeDataFromDB==");
        return mRealm.where(JokeComment.class).equalTo("page", page).findAllSorted("comment_ID", Sort.DESCENDING);
    }

    /**
     * @param page 读取妹纸图的页码
     */
    public Observable<List<SingleImage>> getMeizhiData(int page) {
        return getImageData(page, IJandan.TYPE_MEIZHI, TYPE_MEIZHI);
    }


    /**
     * @param page 读取妹纸图
     */
    public List<SingleImage> getMeizhiDataFromDB(int page) {
        Log.d(TAG, "==GetJokeDataFromDB==");
        return getImageFromDB(page, TYPE_MEIZHI);
    }


    private Observable<List<SingleImage>> getImageData(int page, String queryType, int equalType) {
        Log.d(TAG, "==GetMeizhi==");
        Observable<List<SingleImage>> observable;
        if (NetworkHelper.netWorkAvailable(mApplicationContext.get())) {
            observable = mJandan.getImage(page, queryType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(image -> image != null && image.comments != null)
                    .map(data -> {
                        // 记录时间
                        SPHelper.setLastRefreshTime(mApplicationContext.get(), queryType);
                        List<SingleImage> pageImages = new ArrayList<>();
                        // 清除旧数据 添加新数据
                        RealmResults<SingleImage> ret = mRealm.where(SingleImage.class)
                                .equalTo("page", page)
                                .equalTo(SingleImage.TYPE, equalType)
                                .findAll();
                        mRealm.executeTransaction(realm -> {
                            // 存储关联对象
                            List<ImageComment> savedComment = mRealm.copyToRealmOrUpdate(data.comments);
                            for (SingleImage oldPost : ret) {
                                oldPost.page = 0;
                            }
                            for (ImageComment newPost : savedComment) {
                                List<SingleImage> singleImageList = SingleImage.splitToSingle(mRealm, newPost);
                                int i = 0;
                                for (SingleImage singleImage : singleImageList) {
                                    singleImage.url = newPost.pics.get(i).getValue();
                                    singleImage.page = page;
                                    singleImage.type = equalType;
                                    i++;
                                }
                                pageImages.addAll(singleImageList);
                            }
                            mRealm.copyToRealmOrUpdate(pageImages);
                        });
                        return pageImages;
                    });
        } else {
            observable = Observable.just(getMeizhiDataFromDB(page));
        }
        return observable.filter(ret -> ret != null).timeout(TIMEOUT, TimeUnit.SECONDS);
    }

    private List<SingleImage> getImageFromDB(int page, int type) {
        return mRealm.where(SingleImage.class).equalTo("page", page).equalTo("type", type).findAllSorted(SingleImage.ID, Sort.DESCENDING);
    }


    //==============================================================================================
    //=
    //=
    //=                              本地离线图片管理
    //=
    //=
    //==============================================================================================


    //==============================================================================================
    //=
    //=
    //=                                 收藏管理
    //=
    //=
    //==============================================================================================

    @MainThread
    public void manageNewsFavorite(NewsPost newsPost, boolean favorite) {
        mRealm.executeTransaction(realm -> {
            if (favorite) {
                newsPost.favorite = true;
                newsPost.favorite_time = new Date();
            } else {
                newsPost.favorite = false;
                newsPost.favorite_time = null;
            }
        });
    }

    public void manageJokeFavorite(JokeComment comment, boolean favorite) {
        mRealm.executeTransaction(realm -> {
            if (favorite) {
                comment.favorite = true;
                comment.favorite_time = new Date();
            } else {
                comment.favorite = false;
                comment.favorite_time = null;
            }
        });
    }

    @MainThread
    public void manageImageFavorite(SingleImage singleImage, boolean favorite) {
        mRealm.executeTransaction(realm -> {
            if (favorite) {
                singleImage.favorite = true;
                singleImage.favorite_time = new Date();
            } else {
                singleImage.favorite = false;
                singleImage.favorite_time = null;
            }
        });
    }

    /**
     *
     * //    if (Looper.myLooper() == Looper.getMainLooper()) {
     //            // http://stackoverflow.com/questions/11411022/how-to-check-if-current-thread-is-not-main-thread
     //        }
     *
     *
     * **/

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
}
