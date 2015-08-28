package com.blazers.jandan.util.network;


import android.content.Context;
import android.util.Log;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.orm.Meizi;
import com.blazers.jandan.orm.Picture;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Blazers on 2015/8/26.
 */
public class MeiziParser {
    public static final String TAG = MeiziParser.class.getSimpleName();
    /* INSTANCE */
    private static Context mContext;
    private static MeiziParser INSTANCE;
    /* 缓存当前最新的Page 应该与数据库同步 */
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGE;
    private Realm mRealm;
    private OkHttpClient mClient;

    /**
     * 从数据库同步最新字段并读取
     * */
    public static void init(Context context) {
        mContext = context;
    }

    private MeiziParser(){
        mClient = new OkHttpClient();
    }

    public static MeiziParser getInstance() {
        if (mContext == null) {
            throw new NullPointerException(String.valueOf("You must init the MeiziParse before call it"));
        }
        if (INSTANCE == null) {
            INSTANCE = new MeiziParser();
        }
        return INSTANCE;
    }


    public void parseAPI(boolean refresh) {
        mRealm = Realm.getInstance(mContext);
        String url = refresh ? URL.JANDAN_OOXX_API : URL.JANDAN_OOXX_API + "&page=" + (CURRENT_PAGE + 1);
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            String json = mClient.newCall(request).execute().body().string();
            Log.i(TAG, "=== START PARSING ===" + System.currentTimeMillis());
            JSONObject object = new JSONObject(json);
            /* 保存页码信息 */
            CURRENT_PAGE = object.getInt("current_page");
            TOTAL_PAGE = object.getInt("page_count");
            int size = object.getInt("count");
            /* 提取信息 */
            JSONArray comments = object.getJSONArray("comments");
            /* 提取出首尾 */
            String first = comments.getJSONObject(0).getString("comment_ID");
            String last = comments.getJSONObject(size - 1).getString("comment_ID");
            Log.i(TAG, " From: " + first + " ====  To:" + last);
            /* 扎找本地备份 该更新的更新 该添加的添加 */
            RealmQuery<Meizi> query = mRealm.where(Meizi.class);
            long max = query.maximumInt("comment_ID");
            long min = query.minimumInt("comment_ID");
            Log.i(TAG, "Local Database From: " + max + " ====  To:" + min);
            /* 更新 */
            mRealm.beginTransaction();
            for (int i = 0 ; i < size ; i ++) {
                JSONObject comment = comments.getJSONObject(i);
                /* Parse */
                long comment_ID = Long.parseLong(comment.getString("comment_ID"));
                long comment_post_ID = Long.parseLong(comment.getString("comment_post_ID"));
                comment.put("comment_ID", comment_ID);
                comment.put("comment_post_ID", comment_post_ID);
                Meizi meizi = mRealm.createOrUpdateObjectFromJson(Meizi.class, comment);
                JSONArray pics = comment.getJSONArray("pics");
                for (int pi = 0 ; pi < pics.length() ; pi ++) {
                    /* 避免多次保存 */
                    Picture picture = new Picture();
                    picture.setComment_ID_index(comment_ID + "_" + pi);
                    picture.setUrl(pics.getString(pi));
                    picture.setMeizi(meizi);
                    try {
                        mRealm.copyToRealm(picture);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                meizi.setPicture_size(pics.length());
                Log.e("UPDATE OR CREATE ", "ID === > " + meizi.getComment_ID());
            }
            /* 添加 */
            Log.i(TAG, "=== END PARSING ===" + System.currentTimeMillis());
            mRealm.commitTransaction();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            mRealm.close();
        }
    }
}
