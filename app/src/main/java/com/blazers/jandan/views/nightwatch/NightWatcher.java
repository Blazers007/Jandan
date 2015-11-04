package com.blazers.jandan.views.nightwatch;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Blazers on 2015/11/4.
 */
public class NightWatcher {
    public static void switchToModeNight(View root) {
        sssN(root);
    }

    private static void sssN(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            for (int i = 0 ; i < group.getChildCount() ; i ++)
                sssN(group.getChildAt(i));
        } else if (root instanceof INightWatch) {
            ((INightWatch)root).setNightMode();
        }
    }
}
