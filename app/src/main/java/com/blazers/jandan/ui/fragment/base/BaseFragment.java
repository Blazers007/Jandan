package com.blazers.jandan.ui.fragment.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.blazers.jandan.R;
import com.blazers.jandan.rxbus.Rxbus;
import com.blazers.jandan.rxbus.event.InitToolbarEvent;
import com.blazers.jandan.rxbus.event.NightModeEvent;
import com.blazers.jandan.ui.activity.MainActivity;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.views.nightwatch.NightWatcher;
import com.umeng.analytics.MobclickAgent;
import rx.Subscription;

import java.lang.reflect.Field;

/**
 * Created by Blazers on 2015/9/11.
 */
public abstract class BaseFragment extends Fragment {

    /* Vars */
    public String TAG = "BaseFragment";
    private Toolbar toolbar;
    private String title;

    /* Vars */
    protected boolean isNowNightModeOn;

    /* Umeng */
    private boolean needUmengStatic = true;

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public void setNeedUmengStatic(boolean need) {
        needUmengStatic = need;
    }

    public BaseFragment() {
        super();
        Log.i(TAG, "Constructor");
    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "Attach");
        super.onAttach(context);
        /* 读取模式 */
        isNowNightModeOn = SPHelper.getBooleanSP(context, SPHelper.NIGHT_MODE_ON, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        registerEventReceiver();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "OnActivityCreated");
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        Log.i(TAG, "OnStart");
        super.onStart();
    }

    /**
     * 为何不能在此处统计页面周期?
     * */
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "OnResume");
        if (needUmengStatic) {
            MobclickAgent.onPageStart(TAG);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "OnPause");
        if (needUmengStatic) {
            MobclickAgent.onPageEnd(TAG);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "OnStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "OnDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventReceiver();
        Log.i(TAG, "OnDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "OnDetach");
    }


    /**
     * 初始化Toolbar与Title文字
     * */
    protected void initToolbarAndLeftDrawer(Toolbar toolbar, String title) {
        this.toolbar = toolbar;
        this.title = title;
        toolbar.setTitle(title);
        applyNewMode();
        reboundToolbar();
    }

    /**
     * 重新绑定Toolbar与Menu
     * */
    public void reboundToolbar() {
        if (null != toolbar)
            Rxbus.getInstance().send(new InitToolbarEvent(toolbar));
    }

    /**
     * 注册事件
     * */
    private Subscription subscription;
    public void registerEventReceiver() {
        Rxbus.getInstance().toObservable().subscribe(this::handleRxEvent);
    }

    /**
     * 解注事件
     * */
    public void unregisterEventReceiver() {
        if (null != subscription)
            subscription.unsubscribe();
    }

    /**
     * 处理事件
     * */
    public void handleRxEvent(Object event){
        if (event instanceof NightModeEvent) {
            isNowNightModeOn = ((NightModeEvent) event).nightModeOn;
            applyNewMode();
            /* 改变Root */
            NightWatcher.switchToModeNight(getView(), isNowNightModeOn);
        }
    }

    /**
     * 改变当前Fragment的主题颜色
     * */
    protected void applyNewMode() {
        if (null == toolbar)
            return;
        if (isNowNightModeOn) {
            toolbar.setBackgroundColor(Color.rgb(44, 44, 44));
            toolbar.setTitleTextColor(Color.rgb(190, 190, 190));
            final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_menu_grey600_24dp);
            upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(upArrow);
        } else {
            toolbar.setBackgroundColor(Color.rgb(250, 250, 250));
            toolbar.setTitleTextColor(Color.rgb(60, 64, 67));
            final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_menu_grey600_24dp);
            upArrow.setColorFilter(Color.parseColor("#3c4043"), PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(upArrow);
        }
    }
}
