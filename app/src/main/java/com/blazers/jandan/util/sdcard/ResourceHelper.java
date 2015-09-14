package com.blazers.jandan.util.sdcard;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

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
}
