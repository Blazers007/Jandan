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
public enum NetworkHelper {

    UNKNOWN("unknown"),
    WIFI_CONNECTED("connected to WiFi"),
    WIFI_CONNECTED_HAS_INTERNET("connected to WiFi (Internet available)"),
    WIFI_CONNECTED_HAS_NO_INTERNET("connected to WiFi (Internet not available)"),
    MOBILE_CONNECTED("connected to mobile network"),
    OFFLINE("offline");

    private String status;

    NetworkHelper(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }


    /**
     * Creates a function, which checks
     * if single connectivity status or many statuses
     * are equal to current status. It can be used inside filter(...)
     * method from RxJava
     *
     * @param statuses many connectivity statuses or single status
     * @return Func1<NetworkHelper, Boolean> from RxJava
     */
    public static Func1<NetworkHelper, Boolean> isEqualTo(final NetworkHelper... statuses) {
        return networkStatue -> {
            boolean statuesAreEqual = false;
            for (NetworkHelper statue : statuses) {
                statuesAreEqual = statue == networkStatue;
            }
            return statuesAreEqual;
        };
    }

    /**
     * Creates a function, which checks
     * if single connectivity status or many statuses
     * are not equal to current status. It can be used inside filter(...)
     * method from RxJava
     *
     * @param statuses many connectivity statuses or single status
     * @return Func1< NetworkHelper, Boolean> from RxJava
     */
    public static Func1<NetworkHelper, Boolean> isNotEqualTo(final NetworkHelper... statuses) {
        return networkStatue -> {
            boolean statuesAreNotEqual = false;

            for (NetworkHelper singleStatus : statuses) {
                statuesAreNotEqual = singleStatus != networkStatue;
            }
            return statuesAreNotEqual;
        };
    }

    /* APIs */
    public static boolean isWifiWorksFine() {
        return false;
    }

    public static boolean isPhoneInOfflineMode() {
        return false;
    }


    public static boolean netWorkAvailable(Context context) {
        ConnectivityManager nw = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = nw.getActiveNetworkInfo();
        return networkInfo != null;
    }
}
