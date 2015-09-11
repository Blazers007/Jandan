package com.blazers.jandan.network;


import android.util.Log;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.models.jandan.JokePosts;
import com.blazers.jandan.models.jandan.NewsPosts;
import com.squareup.okhttp.Request;
import io.realm.Realm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Blazers on 2015/8/26.
 */
public class JandanParser extends HttpParser {
    public static final String TAG = JandanParser.class.getSimpleName();

    protected static JandanParser INSTANCE;
    /* 缓存当前最新的Page 应该与数据库同步 */
    private int CURRENT_MEIZI_PAGE = 1;
    private int CURRENT_NEWS_PAGE = 1;
    private int TOTAL_PAGE;

    private JandanParser(){
        /* Init vars */
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

    private void parseJandanImageAPI(int type, float refreshing) {
        /* 每次从网络更新后  获取一下当前页码的标志 并判断数据库是否需要读取后面的内容？ */
    }

    /* 解析妹子API */
    public void parseMeiziAPI(boolean refresh) {
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
                NewsPosts newsList = new NewsPosts();
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
                mRealm.createOrUpdateObjectFromJson(JokePosts.class, comment);
            }
            Log.i(TAG, "=== END PARSING ===" + System.currentTimeMillis());
            mRealm.commitTransaction();
        } catch (Exception e) {

        } finally {
            mRealm.close();
        }
    }

    public void parsePicAPI(boolean refresh) {

    }
}
