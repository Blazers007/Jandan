package com.blazers.jandan.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.blazers.jandan.R;

/**
 * Created by blazers on 2016/11/30.
 */

public class ToolbarHelper {

    /**
     * 改变当前Fragment的Toolbar的颜色
     * */
    public static void applyToolbarIconAndTheme(Toolbar toolbar, Context context, boolean isNowNightModeOn) {
        if (null == toolbar)
            return;
        // Background and color
        if (isNowNightModeOn) {
            toolbar.setBackgroundColor(Color.rgb(44, 44, 44));
            toolbar.setTitleTextColor(Color.rgb(190, 190, 190));
            final Drawable upArrow = context.getResources().getDrawable(R.drawable.ic_menu_grey600_24dp);
            upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(upArrow);
        } else {
            toolbar.setBackgroundColor(Color.rgb(250, 250, 250));
            toolbar.setTitleTextColor(Color.rgb(60, 64, 67));
            final Drawable upArrow = context.getResources().getDrawable(R.drawable.ic_menu_grey600_24dp);
            upArrow.setColorFilter(Color.parseColor("#3c4043"), PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(upArrow);
        }
        // Menu Icon
        Menu menu = toolbar.getMenu();
        if (null != menu) {
            for (int i = 0 ; i < menu.size() ; i ++) {
                MenuItem item = menu.getItem(i);
                Drawable icon = item.getIcon();
                if (isNowNightModeOn) {
                    icon.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                } else {
                    icon.setColorFilter(Color.parseColor("#3c4043"), PorterDuff.Mode.SRC_ATOP);
                }
                item.setIcon(icon);
            }
        }
    }

}
