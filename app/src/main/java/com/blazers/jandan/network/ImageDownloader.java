package com.blazers.jandan.network;

import android.util.Log;
import com.blazers.jandan.models.db.local.LocalImage;
import com.blazers.jandan.models.pojo.image.ImageRelateToPost;
import com.blazers.jandan.util.SdcardHelper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by Blazers on 2015/8/28.
 */
public class ImageDownloader {


    private static ImageDownloader INSTANCE;

    private OkHttpClient client;

    private ImageDownloader(){
        client = new OkHttpClient();
        client.setConnectTimeout(10, TimeUnit.SECONDS);
    }

    public static ImageDownloader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImageDownloader();
        }
        return INSTANCE;
    }

    public LocalImage doSimpleCaching(String url) {
        return doSimpleDownload(url, SdcardHelper.createCachedImageFile(getTypeByUrl(url)));
    }

    public LocalImage doCachingImage(ImageRelateToPost imageRelateToPost) {
        String url = imageRelateToPost.url;
        return doSimpleDownload(url, SdcardHelper.createCachedImageFile(getTypeByUrl(url)));
    }

    public LocalImage doSavingImage(ImageRelateToPost imageRelateToPost) {
        String url = imageRelateToPost.url;
        return doSimpleDownload(url, SdcardHelper.createSavedImageFile(getTypeByUrl(url)));
    }

    public LocalImage doSavingImage(String url) {
        return doSimpleDownload(url, SdcardHelper.createSavedImageFile(getTypeByUrl(url)));
    }

    private String getTypeByUrl(String url){
        String type = url.substring(url.lastIndexOf(".") + 1);
        if (type.isEmpty())
            type = "cache";
        return type;
    }

    private LocalImage doSimpleDownload(String url, File file) {
        Request request = new Request.Builder()
            .url(url)
            .build();
        Log.i("Downloading", url);
        try {
            InputStream inputStream = client.newCall(request).execute().body().byteStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024*10];
            while(true){
                int length = inputStream.read(buffer);
                if (length == -1)
                    break;
                fos.write(buffer, 0, length);
            }
            inputStream.close();
            fos.close();
            LocalImage localImage = new LocalImage();
            localImage.setUrl(url);
            localImage.setLocalUrl(file.getAbsolutePath());
            return localImage;
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
        }
        return null;
    }
}
