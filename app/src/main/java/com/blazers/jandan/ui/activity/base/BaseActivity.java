package com.blazers.jandan.ui.activity.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.blazers.jandan.presenter.base.BasePresenter;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Created by Blazers on 2015/8/31.
 */
@SuppressLint("Registered")
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    protected T mPresenter;
    /* Vars */
    private Toolbar toolbar;
    private ViewGroup toolbarWithShadow;

    //        isNowNightModeOn = SPHelper.getBooleanSP(this, SPHelper.NIGHT_MODE_ON, false);

    /**
     * 设置StatusBar显示的ICON为深色ICON
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBarLightTheme() {
        if (getWindow() != null && getWindow().getDecorView() != null) {
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 取消StatusBar的Tint模式并显示黑色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void disableStatusBarTheme() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.BLACK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // M 以上的版本会自动使用style里面的配置设置Tint
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            disableStatusBarTheme();
        }
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
     * @return 当前Fragment
     */
    protected Fragment switchCurrentFragment(int wrapperId, String tag, Fragment currentFragment, Fragment targetFragment) {
        if (targetFragment == null || targetFragment == currentFragment) {
            return null;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        if (targetFragment.isAdded()) {
            fragmentTransaction.show(targetFragment);
        } else {
            fragmentTransaction.add(wrapperId, targetFragment, tag);
        }
        fragmentTransaction.commitNow();
        return targetFragment;
    }


    /**
     * 根据TAG返回当前的FragmentManager中的指定Fragment
     *
     * @param tag
     * @return
     */
    protected Fragment findFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    /**
     * 设置视图的浮动模式 设置内容能够显示在status bar navigation bar 之后
     */
    public void setReadyForImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(getUiOptionFlagForReadyImmersiveMode());
        // 设置toolbar 的 padding
        if (toolbar != null) {
            toolbar.setPadding(toolbar.getPaddingLeft(),
                    getStatusBarHeight() + toolbar.getPaddingTop(),
                    toolbar.getPaddingRight(),
                    toolbar.getPaddingBottom());
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
        getWindow().getDecorView().setSystemUiVisibility(getUiOptionFlagForReadyImmersiveMode());
        // handle layout_toolbar
        View animate = toolbarWithShadow == null ? toolbar : toolbarWithShadow;
        if (null != animate) {
            animate.animate()
                    .translationY(0)
                    .setDuration(150)
                    .start();
        }
    }

    /**
     * TODO
     */
    public void hideSystemUI(Toolbar toolbar) {
        getWindow().getDecorView().setSystemUiVisibility(getUiOptionFlagForGoingToImmersiveMode());
        // handle layout_toolbar
        View animate = toolbarWithShadow == null ? toolbar : toolbarWithShadow;
        if (null != animate) {
            animate.animate() //TODO: 如果Toolbar比较高 如何动态获取toolbar高度
                    .translationY(-toolbar.getHeight())
                    .setDuration(400)
                    .setStartDelay(200)
                    .start();
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

    private int getUiOptionFlagForReadyImmersiveMode() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 使内容能够显示在在 Status bar 之后
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE     // 同上
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;   // 让内容能够显示在 Navigation bar 之后
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uiOptions |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        return uiOptions;
    }

    private int getUiOptionFlagForGoingToImmersiveMode() {
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN // 隐藏Status bar 与 Navigation bar

                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 使内容能够显示在在 Status bar 之后
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE     // 同上

                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION           // 隐藏 Navigation bar
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;   // 让内容能够显示在 Navigation bar 之后

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE; // 真 沉浸模式
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uiOptions |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        return uiOptions;
    }

    /**
     * ======================================== Life Cycle ======================================
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestory();
        }
    }

    /* Static */
    public enum ToolbarType {
        NORMAL,
        FINISH
    }
}
