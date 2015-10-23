package com.blazers.jandan.ui.activity.base;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.blazers.jandan.R;
import com.blazers.jandan.util.Dppx;

/**
 * Created by Blazers on 2015/8/31.
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    /* Static */
    public enum ToolbarType {
        NORMAL,
        FINISH
    }

    /* Vars */
    private Toolbar toolbar;
    private ViewGroup toolbarWithShadow;

    /* Init functions */
    protected void initToolbarByTypeWithShadow(ViewGroup holder, Toolbar toolbar, ToolbarType type) {
        this.toolbarWithShadow = holder;
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        switch (type) {
            case NORMAL:
                getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case FINISH:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(v -> finish());
                break;
        }
    }

    public void setToolbarTitle(String titleText) {
        if ( null != toolbar ) {
            getSupportActionBar().setTitle(titleText);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }



    /* Screen Display APIs */
    public void setContentFloatingModeEnabled(boolean enabled) {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
            /* 首先设置内容浮动在status bar navigation bar 之后 */
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
            Log.i("SET", "Color set 16");
        } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            getWindow().setStatusBarColor(Color.BLACK);
            getWindow().setNavigationBarColor(Color.BLACK);
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
            Log.i("SET", "Color set 21");
        }
    }

    public void showSystemUI(Toolbar toolbar) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 内容浮动在Status bar 之后
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE; // 真 沉浸模式
        }
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        // handle layout_toolbar
        View animate = toolbarWithShadow == null ? toolbar : toolbarWithShadow;
        if (null != animate) {
            animate.animate()
                    .translationY(0)
                    .setDuration(400)
                    .setStartDelay(200).start();
        }
    }

    public void hideNavigationBar(Toolbar toolbar) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN // 隐藏Status bar
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 内容浮动在Status bar 之后
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // 隐藏 Navigation bar
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE; // 真 沉浸模式
        }
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        // handle layout_toolbar
        View animate = toolbarWithShadow == null ? toolbar : toolbarWithShadow;
        if (null != animate) {
            animate.animate() //TODO: 如果Toolbar比较高 如何动态获取toolbar高度
                    .translationY(- getStatusBarHeight() - Dppx.Dp2Px(this, 56))
                    .setDuration(400)
                    .setStartDelay(200).start();
        }
    }


    /* Tools for running activity */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
