package com.blazers.jandan.util.network;

import com.blazers.jandan.util.sdcard.SdcardHelper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Blazers on 2015/8/28.
 */
public class ImageDownloader {


    private static ImageDownloader INSTANCE;

    private OkHttpClient client;

    private ImageDownloader(){
        client = new OkHttpClient();
    }

    public static ImageDownloader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImageDownloader();
        }
        return INSTANCE;
    }

    public String doDownloading(String url) {
        String type = url.substring(url.lastIndexOf(".")+1);
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            InputStream inputStream = client.newCall(request).execute().body().byteStream();
            File file = SdcardHelper.createImageFile(type);
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
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
