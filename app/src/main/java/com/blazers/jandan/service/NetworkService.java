package com.blazers.jandan.service;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.blazers.jandan.common.URL;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Blazers on 2015/8/25.
 */
public class NetworkService extends android.app.Service {

    private static final String TAG = NetworkService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Start");
        getFrontPageInformation();
        return super.onStartCommand(intent, flags, startId);
    }

    /* Get front page information */
    void getFrontPageInformation() {
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(URL.JANDAN_HOME)
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
        Log.i(TAG, html);
        Document document = Jsoup.parse(html);
    }
}
