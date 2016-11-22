package com.blazers.jandan.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.blazers.jandan.R;

/**
 * Created by Blazers on 2015/10/26.
 */
public class NotificationHelper {

    private static NotificationManager manager;
    private static int ICON_BG_COLOR = Color.parseColor("#ffaaff");

    public static NotificationManager getManager(Context context) {
        if (null == manager) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }


    /**
     * 显示离线情况
     */
    public static Notification showOfflineNotification(Context context, int tag, String title, String text, String extra, @Nullable PendingIntent cancel) {
        /**/
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_offline);
        remoteViews.setTextViewText(R.id.notification_title, title);
        remoteViews.setTextViewText(R.id.notification_text, text);
        remoteViews.setTextViewText(R.id.notification_extra, extra);
        /**
         *
         * Android 7.0 Version https://segunfamisa.com/posts/notifications-direct-reply-android-nougat
         *
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContent(remoteViews)
                .setSmallIcon(R.drawable.ic_quick_tile_offline)
                .setColor(ICON_BG_COLOR)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setTicker("您已经开始离线下载")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(false);

        // 当设置ACTION后就无法使用Remote布局？

        Notification notification = builder.build();
        getManager(context).notify(tag, notification);
        return notification;
    }
}
