package com.blazers.jandan.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.blazers.jandan.R;

import java.io.File;

/**
 * Created by Blazers on 2015/9/14.
 */
public class ShareHelper {

    public static void shareWebPage(Context context, String title, String url){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, "分享了一个新鲜事给您(" + url + ")");
//        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(thumb)));
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(share, "分享给..."));
    }

    public static void shareImage(Context context, String title, String text, String path) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, text);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(share, "分享给..."));
    }

    public static void shareText(Context context, String title, String text) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(share, "分享给..."));
    }
}
