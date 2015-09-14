package com.blazers.jandan.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.blazers.jandan.R;
import com.blazers.jandan.util.sdcard.ResourceHelper;

/**
 * Created by Blazers on 2015/9/14.
 */
public class ShareHelper {
    public static void shareWebPage(Context context,String title, String url){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, ResourceHelper.getUriFromDrawable(context, R.mipmap.ic_launcher));
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, "分享了一个新鲜事给您(" + url + ")");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(share, "分享"));
    }
}
