package com.blazers.jandan.util.network;


import android.util.Log;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.orm.MeiziModel;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/8/26.
 */
public class MeiziParser {
    public static final String TAG = MeiziParser.class.getSimpleName();

    /**
     * 加载OOXX并返回对象数组
     * */
    public static ArrayList<MeiziModel> parse() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL.JANDAN_OOXX)
                .build();
        try {
            Document document = Jsoup.parse(client.newCall(request).execute().body().string());
            String nowPage = document.getElementsByClass("current-comment-page").first().val();
            Log.i(TAG, " NOW PAGE ==> " + nowPage);
            ArrayList<MeiziModel> array = new ArrayList<>();
            Element ol = document.getElementsByClass("commentlist").first();
            for(Element li : ol.children()) {
                for (Element img : li.getElementsByTag("img")) {
                    Log.i(TAG, "SRC ==> " + img.attr("src"));
                    MeiziModel meiziModel = new MeiziModel();
                    meiziModel.setUrl(img.attr("src"));
                    meiziModel.setCommentId(li.id());
                    array.add(meiziModel);
                }
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
