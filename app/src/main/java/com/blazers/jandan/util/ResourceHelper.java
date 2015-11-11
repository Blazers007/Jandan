package com.blazers.jandan.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Blazers on 2015/9/14.
 */
public class ResourceHelper {


    public static Uri getUriFromDrawable(Context context, int drawableId) {
        Resources resources = context.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(drawableId) + "/"
                + resources.getResourceTypeName(drawableId) + "/"
                + resources.getResourceEntryName(drawableId)
        );
    }

    /**
     * Assets
     * */
    private static String mainJs;
    public static String GetMainJs(Context context) {
        if (mainJs != null)
            return mainJs;
        try {
            InputStream is = context.getAssets().open("js/main.js");
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[1024*5];
            while (true) {
                int length = is.read(buffer);
                if (length == -1)
                    break;
                sb.append(new String(buffer, 0 , length));
            }
            is.close();
            mainJs = sb.toString();
            Log.i("ASSET", mainJs);
            return mainJs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
