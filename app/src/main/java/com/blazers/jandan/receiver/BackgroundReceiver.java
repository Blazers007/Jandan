package com.blazers.jandan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blazers.jandan.R;

/**
 * Created by blazers on 2016/11/17.
 *
 * 接收广播时间 启动 服务 发送指令
 *
 */

public class BackgroundReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(context.getString(R.string.action_do_offline_default))) {
            Log.i("===", "Request offline resources with default options");
        }
    }
}
