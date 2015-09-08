package com.blazers.jandan.network;


import android.content.Context;
import android.util.Log;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.orm.joke.Joke;
import com.blazers.jandan.orm.meizi.Meizi;
import com.blazers.jandan.orm.meizi.Picture;
import com.blazers.jandan.orm.news.NewsList;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import io.realm.Realm;
import io.realm.RealmQuery;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Blazers on 2015/8/26.
 */
public class JandanParser {
    public static final String TAG = JandanParser.class.getSimpleName();
    /* INSTANCE */
    private static Context mContext;
    private static JandanParser INSTANCE;
    /* 缓存当前最新的Page 应该与数据库同步 */
    private int CURRENT_MEIZI_PAGE = 1;
    private int CURRENT_NEWS_PAGE = 1;
    private int TOTAL_PAGE;
    private Realm mRealm;
    private OkHttpClient mClient;

    /**
     * 从数据库同步最新字段并读取
     * */
    public static void init(Context context) {
        mContext = context;
    }

    private JandanParser(){
        mClient = new OkHttpClient();
    }

    public static JandanParser getInstance() {
        if (mContext == null) {
            throw new NullPointerException(String.valueOf("You must init the JandanParse before call it"));
        }
        if (INSTANCE == null) {
            INSTANCE = new JandanParser();
        }
        return INSTANCE;
    }


    /* 解析妹子API */
    public void parseMeiziAPI(boolean refresh) {
        mRealm = Realm.getInstance(mContext);
        String url = refresh ? URL.JANDAN_OOXX_API : URL.JANDAN_OOXX_API + "&page=" + (CURRENT_MEIZI_PAGE + 1);
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            String json = mClient.newCall(request).execute().body().string();
            Log.i(TAG, "=== START PARSING ===" + System.currentTimeMillis());
            JSONObject object = new JSONObject(json);
            /* 保存页码信息 */
            CURRENT_MEIZI_PAGE = object.getInt("current_page");
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
                    picture.setType("meizi");
                    mRealm.copyToRealmOrUpdate(picture);
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

    /* 解析新鲜事API */
    public void parseNewsAPI(boolean refresh) {
        mRealm = Realm.getInstance(mContext);
        if (refresh){
            CURRENT_NEWS_PAGE = 1;
        } else {
            CURRENT_NEWS_PAGE ++;
        }
        String url = URL.JANDAN_NEWS_API_PREFIX + CURRENT_NEWS_PAGE + URL.JANDAN_NEWS_API_END;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            String json = mClient.newCall(request).execute().body().string();
            /* Parse News JSON */
            Log.i(TAG, "=== START PARSING ===" + System.currentTimeMillis());
            JSONObject object = new JSONObject(json);
            int count = object.getInt("count");
            int count_total = object.getInt("count_total"); //可用该项来对比是否需要更新?
            int pages = object.getInt("pages");
            mRealm.beginTransaction();
            JSONArray posts = object.getJSONArray("posts");
            for (int i = 0 ; i < count ; i ++) {
                JSONObject post = posts.getJSONObject(i);
                NewsList newsList = new NewsList();
                newsList.setId(post.getLong("id"));
                newsList.setTitle(post.getString("title"));
                newsList.setUrl(post.getString("url"));
                newsList.setAuthor(post.getJSONObject("author").getString("name"));
                newsList.setDate(post.getString("date"));
                newsList.setTagTitle(post.getJSONArray("tags").getJSONObject(0).getString("title"));
                newsList.setThumbUrl(post.getJSONObject("custom_fields").getJSONArray("thumb_c").getString(0));
                newsList.setViews(post.getJSONObject("custom_fields").getJSONArray("views").getLong(0));
                mRealm.copyToRealmOrUpdate(newsList);
            }
            Log.i(TAG, "=== END PARSING ===" + System.currentTimeMillis());
            mRealm.commitTransaction();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            mRealm.close();
        }
    }

    public String parseNewsContent(long id) {
        String url = URL.GernarateNewsContentUrl(id);
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            String json = mClient.newCall(request).execute().body().string();
            JSONObject object = new JSONObject(json);
            String html = object.getJSONObject("post").getString("content");
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>");
            sb.append("<html><body>");
            sb.append("<head>");
            sb.append("<link id=\"style\" rel=\"stylesheet\" type=\"text/css\" href=\"\" />");
            sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/style.css\" />");
            sb.append("<script src=\"file:///android_asset/js/main.js\" type=\"text/javascript\"></script>");
            sb.append("</head>");
            sb.append(html);
            sb.append("</body></html>");
            return sb.toString();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void parseJokeAPI(boolean refresh) {
        mRealm = Realm.getInstance(mContext);
        String url = URL.JANDAN_JOKE_API;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            String json = mClient.newCall(request).execute().body().string();
            Log.i(TAG, "=== START PARSING ===" + System.currentTimeMillis());
            JSONObject object = new JSONObject(json);
            JSONArray comments = object.getJSONArray("comments");
            mRealm.beginTransaction();
            for (int i = 0 ; i < comments.length() ; i ++) {
                JSONObject comment = comments.getJSONObject(i);
                mRealm.createOrUpdateObjectFromJson(Joke.class, comment);
            }
            Log.i(TAG, "=== END PARSING ===" + System.currentTimeMillis());
            mRealm.commitTransaction();
        } catch (Exception e) {

        } finally {
            mRealm.close();
        }
    }

    public void parsePicAPI(boolean refresh) {
        mRealm = Realm.getInstance(mContext);
        String url = refresh ? URL.JANDAN_PIC_API : URL.JANDAN_PIC_API + "&page=" + (CURRENT_MEIZI_PAGE + 1);
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            String json = mClient.newCall(request).execute().body().string();
            Log.i(TAG, "=== START PARSING ===" + System.currentTimeMillis());
            JSONObject object = new JSONObject(json);
            /* 保存页码信息 */
            CURRENT_MEIZI_PAGE = object.getInt("current_page");
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
                Meizi pic = mRealm.createOrUpdateObjectFromJson(Meizi.class, comment);
                JSONArray pics = comment.getJSONArray("pics");
                for (int pi = 0 ; pi < pics.length() ; pi ++) {
                    /* 避免多次保存 */
                    Picture picture = new Picture();
                    picture.setComment_ID_index(comment_ID + "_" + pi);
                    picture.setUrl(pics.getString(pi));
                    picture.setMeizi(pic);
                    picture.setType("pic");
                    mRealm.copyToRealmOrUpdate(picture);
                }
                pic.setPicture_size(pics.length());
                Log.e("UPDATE OR CREATE ", "ID === > " + pic.getComment_ID());
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
