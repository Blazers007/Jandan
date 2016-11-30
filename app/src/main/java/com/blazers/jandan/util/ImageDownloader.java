package com.blazers.jandan.util;

import android.util.Log;

import com.blazers.jandan.model.database.local.LocalImage;
import com.blazers.jandan.model.pojo.image.ImageRelateToPost;

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
     * 根据Url地址直接缓存图像
     *
     * @param url 图像地址
     * @return 返回一个LocalImage对象 映射Url与本地File路径
     */
    public LocalImage doSimpleOfflineCaching(String url) {
        return doSimpleDownload(url, SdcardHelper.createOfflineImageFile(getTypeByUrl(url)));
    }

    /**
     * 根据Url地址直接缓存图像
     *
     * @param imageRelateToPost RecyclerView中的图像对象
     * @return 返回一个LocalImage对象 映射Url与本地File路径
     */
    public LocalImage doOfflineCachingImage(ImageRelateToPost imageRelateToPost) {
        String url = imageRelateToPost.url;
        return doSimpleDownload(url, SdcardHelper.createOfflineImageFile(getTypeByUrl(url)));
    }

    /**
     * 同上 目录不同
     */
    public LocalImage doSavingImage(String url) {
        return doSimpleDownload(url, SdcardHelper.createSavedImageFile(getTypeByUrl(url)));
    }

    /**
     * 同上 目录不同
     */
    public LocalImage doSavingImage(ImageRelateToPost imageRelateToPost) {
        String url = imageRelateToPost.url;
        return doSimpleDownload(url, SdcardHelper.createSavedImageFile(getTypeByUrl(url)));
    }

    public LocalImage doCachingImage(ImageRelateToPost imageRelateToPost) {
        String url = imageRelateToPost.url;
        return doSimpleDownload(url, SdcardHelper.createCachedImageFile(getTypeByUrl(url)));
    }

    /**
     * 根据Url获取文件类型
     */
    private String getTypeByUrl(String url) {
        String type = HtmlImgReplaceUtil.getNoParameterUrl(url).substring(url.lastIndexOf(".") + 1);
        if (type.isEmpty())
            type = "cache";
        return type;
    }

    /**
     * 下载并返回LocalImage对象
     */
    private LocalImage doSimpleDownload(String url, File file) {
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
