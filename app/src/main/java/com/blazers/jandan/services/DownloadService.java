package com.blazers.jandan.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.network.ImageDownloader;
import io.realm.Realm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 15/9/19.
 */
public class DownloadService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadService() {
        super("download");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        long id = intent.getLongExtra("id", -1);
        String url = intent.getStringExtra("url");
        Log.i("[Download-Pre]", url);
        ImageDownloader downloader = ImageDownloader.getInstance();
        String path = downloader.doDownloading(url);
        /*  */
        new Thread(()->{
            Realm realm = Realm.getInstance(this);
            Image image = realm.where(Image.class).equalTo("id", id).findFirst();
            if (image == null)
                return;
            realm.beginTransaction();
            image.setLocalUrl(path);
            realm.commitTransaction();
            realm.close();
        }).start();
        Log.i("[Download-Post]", path);
    }
}
