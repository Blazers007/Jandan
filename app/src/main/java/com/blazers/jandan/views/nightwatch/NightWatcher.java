package com.blazers.jandan.views.nightwatch;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Blazers on 2015/11/4.
 */
public class NightWatcher {
    public static void switchToModeNight(View root, boolean night) {
        if (root instanceof INightWatch) {
            if (night) {
                ((INightWatch)root).setNightMode();
            } else {
                ((INightWatch)root).setDayMode();
            }
        }
        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            for (int i = 0 ; i < group.getChildCount() ; i ++)
                switchToModeNight(group.getChildAt(i), night);
        }
    }
}
