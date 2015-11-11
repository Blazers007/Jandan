package com.blazers.jandan.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import rx.functions.Func1;

/**
 * Created by Blazers on 2015/9/14.
 *
 * Thanks to Pwittchen @https://github.com/pwittchen
 *
 */
public class NetworkHelper {

    public static boolean netWorkAvailable(Context context) {
        ConnectivityManager nw = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = nw.getActiveNetworkInfo();
        return networkInfo != null;
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
            && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
