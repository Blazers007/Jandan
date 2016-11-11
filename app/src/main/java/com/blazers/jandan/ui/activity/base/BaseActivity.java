package com.blazers.jandan.ui.activity.base;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.blazers.jandan.presenter.base.BasePresenter;
import com.blazers.jandan.util.Dppx;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Created by Blazers on 2015/8/31.
 */
@SuppressLint("Registered")
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    /* Vars */
    private Toolbar toolbar;

    private ViewGroup toolbarWithShadow;

    protected T mPresenter;

    //        isNowNightModeOn = SPHelper.getBooleanSP(this, SPHelper.NIGHT_MODE_ON, false);

    /**
     * 初始化Presenter
     */
    public abstract void initPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    /**
     * 自动Bind
     *
     * @param layoutResID 布局资源
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    /**
     * 初始化Toolbar
     *
     * @param holder
     * @param toolbar
     * @param type
     */
    protected void initToolbarByTypeWithShadow(@Nullable ViewGroup holder, Toolbar toolbar, ToolbarType type) {
        this.toolbarWithShadow = holder;
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() == null)
            return;
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

    /**
     * 设置Toolbar文字
     *
     * @param titleText
     */
    public void setToolbarTitle(String titleText) {
        if (null != toolbar && null != getSupportActionBar()) {
            getSupportActionBar().setTitle(titleText);
        }
    }

    /**
     * 获取Toolbar
     *
     * @return
     */
    public Toolbar getToolbar() {
        return toolbar;
    }


    /**
     * 切换 Fragment
     *
     * @param wrapperId      容器Id
     * @param tag            Fragment对应的Tag
     * @param targetFragment Fragment对象
     */
    protected void switchCurrentFragment(int wrapperId, String tag, Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Stream.of(getSupportFragmentManager().getFragments())
                .filter(n -> n != null)
                .forEach(transaction::hide);
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            transaction.add(wrapperId, targetFragment, tag);
        } else {
            transaction.show(targetFragment);
        }
        transaction.commit();
    }

    /**
     * 设置视图的浮动模式
     *
     * @param enabled
     */
    public void setContentFloatingModeEnabled(boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            /* 首先设置内容浮动在status bar navigation bar 之后 */
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
            Log.i("SET", "Color set 16");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
            getWindow().setNavigationBarColor(Color.BLACK);
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
            Log.i("SET", "Color set 21");
        }
    }

    /**
     * TODO
     */
    public void setNormalWindow() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 内容浮动在Status bar 之后
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE; // 真 沉浸模式
        }
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    /**
     * TODO
     */
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

    /**
     * TODO
     */
    public void setFullWindow() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN // 隐藏Status bar
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 内容浮动在Status bar 之后
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // 隐藏 Navigation bar
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE; // 真 沉浸模式
        }
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    /**
     * TODO
     */
    public void hideSystemUI(Toolbar toolbar) {
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
                    .translationY(-getStatusBarHeight() - Dppx.Dp2Px(this, 56))
                    .setDuration(400)
                    .setStartDelay(200).start();
        }
    }

    /**
     * 获取StatusBar 高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航条高度
     */
    public int getNavigationBarHeight() {
        int resIdShow = getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        boolean hasNavigationBar = false;
        if (resIdShow > 0) {
            hasNavigationBar = getResources().getBoolean(resIdShow);//是否显示底部navigationBar
        }
        if (hasNavigationBar) {
            int resIdNavigationBar = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            int navigationBarHeight;
            if (resIdNavigationBar > 0) {
                navigationBarHeight = getResources().getDimensionPixelSize(resIdNavigationBar);//navigationBar高度
                return navigationBarHeight;
            }
        }
        return 0;
    }

    /**
     * ======================================== Life Cycle ======================================
     */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
    }

    /* Static */
    public enum ToolbarType {
        NORMAL,
        FINISH
    }
}
