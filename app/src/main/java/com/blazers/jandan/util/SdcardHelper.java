package com.blazers.jandan.util;

import android.os.Environment;
import rx.Observable;

import java.io.File;

/**
 * Created by Blazers on 2015/8/28.
 */
public class SdcardHelper {

    /**
     * 获取图片存储路径
     * */
    public static String getSdcardPicturePath() {
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Jandan/Images/");
        if (!file.exists())
            file.mkdirs();
        return file.getPath();
    }

    /**
     * 获取图片缓存路径
     * */
    public static String getSdcardOfflinePath() {
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Jandan/Cached/");
        if (!file.exists())
            file.mkdirs();
        return file.getPath();
    }

    /**
     * 创建离线文件
     * */
    synchronized public static File createOfflineImageFile(String type) {
        return new File(getSdcardOfflinePath() + "/" + System.currentTimeMillis() + "." + type);
    }

    /**
     * 创建存储文件
     * */
    synchronized public static File createSavedImageFile(String type) {
        return new File(getSdcardPicturePath() + "/" + System.currentTimeMillis() + "." + type);
    }

    /**
     * 创建缓存文件
     */
    synchronized public static File createCachedImageFile(String type) {
        return new File(Environment.getDownloadCacheDirectory() + "/" + System.currentTimeMillis() + "." + type);
    }

    /**
     * 清空某个目录
     * */
    public static Observable<Boolean> cleanSDCardOffline() {
        return Observable.create(subscriber -> {
            try {
                File folder = new File(getSdcardOfflinePath());
                deleteDir(folder);
                subscriber.onNext(true);
            }catch (Exception e) {
                subscriber.onError(e);
            }finally {
                subscriber.onCompleted();
            }
        });
    }

    /**
     * 计算大小
     * */
    public static Observable<String> calculateOfflineSize() {
        return Observable.create(subscriber -> {
            try {
                File folder = new File(getSdcardOfflinePath());
                double size = (getDirSize(folder)+0.0) / 1024 / 1024;
                String str = size == 0 ? "0MB" : String.format("%.2fMB", size);
                subscriber.onNext(str);
            }catch (Exception e) {
                subscriber.onError(e);
            }finally {
                subscriber.onCompleted();
            }
        });
    }


    /* Utils */
    private static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] list = dir.listFiles();
            for (File file : list)
                deleteDir(file);
        } else {
            dir.delete();
        }
    }

    private static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return dir.length();
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }

    public static boolean isThisFileExist(String path) {
        if (path == null || path.isEmpty())
            return false;
        File file = new File(path);
        return file.exists();
    }
}
