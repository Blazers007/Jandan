package com.blazers.jandan.network;

import com.blazers.jandan.common.URL;
import com.blazers.jandan.orm.HuabanPin;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import io.realm.Realm;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Blazers on 2015/9/9.
 */
public class HuabanParser extends HttpParser {

    public static final String TAG = HuabanParser.class.getSimpleName();

    protected static HuabanParser INSTANCE;
    /* 缓存当前最新的Page 应该与数据库同步 */
    private int CURRENT_MEIZI_PAGE = 1;
    private int CURRENT_NEWS_PAGE = 1;
    private int TOTAL_PAGE;

    private HuabanParser(){
        // 从数据库中读取相关参数
    }

    public static HuabanParser getInstance() {
        if (mContext == null) {
            throw new NullPointerException(String.valueOf("You must init the HuabanParse before call it"));
        }
        if (INSTANCE == null) {
            INSTANCE = new HuabanParser();
        }
        return INSTANCE;
    }


    /* APIs */
    public void parseQueryPinAPI(String keyword, int page) {
        mRealm = Realm.getInstance(mContext);
        String url = URL.huabanQuerySthAtPage(keyword, page);
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            String json = mClient.newCall(request).execute().body().string();
            JSONObject object = new JSONObject(json);
            JSONArray pins = object.getJSONArray("pins");
            mRealm.beginTransaction();
            for (int i = 0 ; i < pins.length() ; i ++ ){
                JSONObject pin = pins.getJSONObject(i);
                HuabanPin huabanPin = mRealm.createOrUpdateObjectFromJson(HuabanPin.class, pin);

                JSONObject file = pin.getJSONObject("file");
                huabanPin.setFile_key(file.getString("key"));

                JSONObject user = pin.getJSONObject("user");
                huabanPin.setUser_key(user.getJSONObject("avatar").getString("key"));

                JSONObject board = pin.getJSONObject("board");
                huabanPin.setBorad(board.getString("title"));
            }
            mRealm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mRealm.close();
        }
    }
}
