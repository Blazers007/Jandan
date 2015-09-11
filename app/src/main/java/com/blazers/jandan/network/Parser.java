package com.blazers.jandan.network;

import android.content.Context;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.models.jandan.ImagePosts;
import com.blazers.jandan.models.jandan.LocalStorageFlag;
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
import rx.Subscriber;

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

    public static void init(Context context) {
        Parser.context = context;
    }

    private Parser() {
        if (context == null) {
            throw new IllegalStateException("You must call static method Parser.Init(context) first");
        }
        client = new OkHttpClient();
        client.setReadTimeout(12, TimeUnit.SECONDS);
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
    //@param localPage 本地RecyclerView显示的Page 并非远程页面的Page 由于远程页面Page内容不固定?
    /**
     *
     *@param page 本地与远程同步的page仅仅显示后再放入DB中？重新设计DB 当该页已经不会变化的时候再从本地读取页面JSON信息 否则则全部从网络请求数据
     *
     *@param type 获取的类型 目前仅有 无聊图 妹子图
     *
     * */
    public Observable<List<Image>> getPictureData(int page, String type) {
        return Observable.create(subscriber -> {
            List<Image> stringList = getPostPictures(page, type);
            if (stringList == null){
                subscriber.onError(new Exception("Couldn't get ImagePost data from internet"));
            }
            subscriber.onNext(stringList);
            subscriber.onCompleted();
        });
    }


    private List<Image> getPostPictures(int page, String type) {
        // 检查数据库标志位是否已经完成本地缓存
        Realm realm = Realm.getInstance(context);
        LocalStorageFlag flag = LocalStorageFlag.getFlag(realm, page, type);
        List<Image> imageList = new ArrayList<>();
        if ( flag == null || !flag.isComplete()) {
            Request request = new Request.Builder()
                    .url(URL.getJandanAPIByPageAndType(page, type))
                    .build();
            try {
                String json = client.newCall(request).execute().body().string();
                List<ImagePosts> postses = new ArrayList<>();
                JSONObject object = new JSONObject(json);
                JSONArray comments = object.getJSONArray("comments");
                Gson gson = new GsonBuilder()
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
                /* 数据库读写操作放入线程中执行 首先返回数据? */
                for (int i = 0 ; i < comments.length() ; i ++) {
                    /* ImagePost */
                    JSONObject comment = comments.getJSONObject(i);
                    ImagePosts post = gson.fromJson(comment.toString(), ImagePosts.class);
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
                /* TODO: DB IO 操作 后期整理至其他Package */
                new Thread(()->{
                    Realm threadRealm = Realm.getInstance(context);
                    threadRealm.beginTransaction();
                    for (ImagePosts post : postses) {
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
        } else {
            imageList = ImagePosts.getAllImages(realm, page, type);
        }
        return imageList;
    }
}
