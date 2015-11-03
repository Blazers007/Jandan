package com.blazers.jandan.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Blazers on 2015/8/28.
 */
public class SdcardHelper {

    public static String getSdcardPicturePath() {
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Jandan/Images/");
        if (!file.exists())
            file.mkdirs();
        return file.getPath();
    }

    public static String getSdcardCachePath() {
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Jandan/Cached/");
        if (!file.exists())
            file.mkdirs();
        return file.getPath();
    }

    synchronized public static File createCachedImageFile(String type) {
        return new File(getSdcardCachePath() + "/" + System.currentTimeMillis() + "." + type);
    }

    synchronized public static File createSavedImageFile(String type) {
        return new File(getSdcardPicturePath() + "/" + System.currentTimeMillis() + "." + type);
    }
}
