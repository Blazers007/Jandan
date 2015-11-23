package com.blazers.jandan.util;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Blazers on 2015/10/28.
 */
public class SPHelper {
    public static final String SP_NAME = "blazers";
    /* Default Keys */
    public static final String NAME = "name";
    public static final String USER_ID = "userId";
    public static final String NIGHT_MODE_ON = "night_mode_on";
    public static final String AUTO_GIF_MODE_ON = "auto_gif_mode_on";
    public static final String MEIZI_MODE_ON = "meizi_mode_on";
    public static final String AUTO_FILTER_MODE_ON = "filter_mode_on";
    public static final String AUTO_FILTER_NUMBER = "filter_number";

    /* 缓存 */
    private static HashMap<String, Boolean> temp = new HashMap<>();

    public static boolean getBooleanSP(Context context, String key, boolean def) {
        if (temp.containsKey(key))
            return temp.get(key);
        boolean value = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getBoolean(key, def);
        temp.put(key, value);
        return value;
    }

    public static void putBooleanSP(Context context, String key, boolean value) {
        temp.put(key, value);
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    public static int getIntSP(Context context, String key, int def) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getInt(key, def);
    }

    public static void putIntSP(Context context, String key, int value) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
    }

    public static long getLongSP(Context context, String key, long def) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getLong(key, def);
    }

    public static void putLongSP(Context context, String key, long value) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putLong(key, value).apply();
    }

    public static String getStringSP(Context context, String key, String def) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getString(key, def);
    }

    public static void putStringSP(Context context, String key, String value) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    /* 刷新 */
    private static HashMap<String, Long> tempLong = new HashMap<>();
    /**
     * 获取上次刷新时间
     * */
    public static long getLastRefreshTime(Context context, String key) {
        if (tempLong.containsKey(key))
            return tempLong.get(key);
        long time = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getLong(key, 0);
        tempLong.put(key, time);
        return time;
    }

    /**
     * 设置更新时间
     * */
    public static void setLastRefreshTime(Context context, String key) {
        long time = TimeHelper.currentTime();
        tempLong.put(key, time);
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putLong(key, time).apply();
    }

}
