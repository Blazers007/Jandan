package com.blazers.jandan.util;

import android.content.Context;

/**
 * Created by Blazers on 2015/10/28.
 */
public class SPHelper {
    public static final String SP_NAME = "blazers";
    /* Default Keys */
    public static final String NIGHT_MODE_ON = "night_mode_on";
    public static final String AUTO_GIF_MODE_ON = "auto_gif_mode_on";
    public static final String MEIZI_MODE_ON = "meizi_mode_on";
    public static final String AUTO_FILTER_MODE_ON = "filter_mode_on";
    public static final String AUTO_FILTER_NUMBER = "filter_number";

    public static boolean getBooleanSP(Context context, String key, boolean def) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getBoolean(key, def);
    }

    public static void putBooleanSP(Context context, String key, boolean value) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    public static int getIntSP(Context context, String key, int def) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getInt(key, def);
    }

    public static void putIntSP(Context context, String key, int value) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
    }

    public static String getStringSP(Context context, String key, String def) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getString(key, def);
    }

    public static void putStringSP(Context context, String key, String value) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

}
