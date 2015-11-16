package com.blazers.jandan.util;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by BlazersDar on 6/25/2014.
 */
public class Dppx {
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int Sp2Px(Context context, float sp){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

    public static float getY(Context context,float marginToBottom){
        return context.getResources().getDisplayMetrics().heightPixels - Dp2Px(context,marginToBottom);
    }

    public static float getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
}

