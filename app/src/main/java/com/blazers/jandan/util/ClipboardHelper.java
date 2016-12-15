package com.blazers.jandan.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import com.blazers.jandan.util.log.Log;
import android.widget.Toast;

/**
 * Created by Blazers on 2015/11/5.
 *
 * 监听剪贴板 用于汇总收藏功能
 *
 */
public class ClipboardHelper {

    private static ClipboardManager manager;
    private static ClipboardManager.OnPrimaryClipChangedListener listener;

    public static void registerClipboardListener(Context context) {
        manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        listener = ()->{
            ClipData data = manager.getPrimaryClip();
            for (int i = 0 ; i < data.getItemCount(); i ++) {
                ClipData.Item item = data.getItemAt(i);
                Log.i("Clip -->", item.getText().toString());
            }
            /* 判断是否来自本程序 程序在前台 复制内容应该来自于本程序？ */
            Toast.makeText(context, "文本已经复制", Toast.LENGTH_SHORT).show();
        };
        manager.addPrimaryClipChangedListener(listener);
    }

    public static void unregisterListener() {
        if (null != manager)
            manager.removePrimaryClipChangedListener(listener);
    }
}
