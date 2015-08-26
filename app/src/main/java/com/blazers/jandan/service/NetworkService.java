package com.blazers.jandan.service;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.orm.MeiziModel;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import io.realm.Realm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by Blazers on 2015/8/25.
 */
public class NetworkService extends android.app.Service {

    public static final String TAG = NetworkService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Start");
//        getFrontPageInformation();
        return super.onStartCommand(intent, flags, startId);
    }

    /* Get front page information */
    void getFrontPageInformation() {
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(URL.JANDAN_OOXX)
                        .build();
                try {
                    return client.newCall(request).execute().body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if(null == s) {
                    return;
                }
                handleHtmlPage(s);
                super.onPostExecute(s);
            }
        }.execute();
    }

    /* Export */
    void handleHtmlPage(String html) {
//        Log.i(TAG, html);
        Document document = Jsoup.parse(html);
        String nowPage = document.getElementsByClass("current-comment-page").first().val();
        Log.i(TAG, " NOW PAGE ==> " + nowPage);
        Element ol = document.getElementsByClass("commentlist").first();

        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();
        for(Element li : ol.children()) {
            for (Element img : li.getElementsByTag("img")) {
                Log.i(TAG, "SRC ==> " + img.attr("src"));
                MeiziModel meizi = realm.createObject(MeiziModel.class);
                meizi.setCommentId(li.id());
                meizi.setUrl(img.attr("src"));
            }
        }
        realm.commitTransaction();
        realm.close();
    }
}
