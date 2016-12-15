package com.blazers.jandan.util;

import com.blazers.jandan.util.log.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Blazers on 2015/8/28.
 *
 * TODO: 重构
 */
public class ImageDownloader {


    private static ImageDownloader INSTANCE;

    private OkHttpClient client;

    private ImageDownloader() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static ImageDownloader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImageDownloader();
        }
        return INSTANCE;
    }


    /**
     * 根据Url获取文件类型
     */
    private String getTypeByUrl(String url) {
//        String type = HtmlImgReplaceUtil.getNoParameterUrl(url).substring(url.lastIndexOf(".") + 1);
        String type = "";
        if (type.isEmpty())
            type = "cache";
        return type;
    }

    /**
     * 下载并返回LocalImage对象
     */
    private void doSimpleDownload(String url, File file) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.i("Downloading", url);
        try {
            InputStream inputStream = client.newCall(request).execute().body().byteStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 10];
            while (true) {
                int length = inputStream.read(buffer);
                if (length == -1)
                    break;
                fos.write(buffer, 0, length);
            }
            inputStream.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
        }
    }
}
