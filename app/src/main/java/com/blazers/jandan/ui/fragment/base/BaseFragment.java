package com.blazers.jandan.ui.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import com.blazers.jandan.util.log.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blazers.jandan.presenter.base.BasePresenter;
import com.blazers.jandan.model.event.NightModeEvent;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.util.ToolbarHelper;
import com.blazers.jandan.widgets.nightwatch.NightWatcher;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Created by Blazers on 2015/9/11.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment {

    public String mTAG = "BaseFragment";
    private Toolbar toolbar;

    protected T mPresenter;

    /* Vars */
    protected boolean isNowNightModeOn;

    /* Umeng */
    private boolean needUmengStatic = true;

    public void setNeedUmengStatic(boolean need) {
        needUmengStatic = need;
    }

    public BaseFragment() {
        super();
        Log.i(mTAG, "Constructor");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(mTAG, "Attach");
        isNowNightModeOn = SPHelper.getBooleanSP(context, SPHelper.NIGHT_MODE_ON, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(mTAG, "OnCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(mTAG, "OnActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    protected abstract int getLayoutResId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onStart() {
        Log.i(mTAG, "OnStart");
        super.onStart();
    }

    /**
     * 为何不能在此处统计页面周期?
     * */
    @Override
    public void onResume() {
        super.onResume();
        Log.i(mTAG, "OnResume");
        if (needUmengStatic) {
            MobclickAgent.onPageStart(mTAG);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(mTAG, "OnPause");
        if (needUmengStatic) {
            MobclickAgent.onPageEnd(mTAG);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(mTAG, "OnStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(mTAG, "OnDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(mTAG, "OnDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(mTAG, "OnDetach");
    }


    /**
     * 初始化Toolbar与Title文字
     * */
    protected void initToolbarAndLeftDrawer(Toolbar toolbar, String title) {
        initToolbarWithLeftDrawerAndMenu(toolbar, title, -1, null);
    }

    /**
     * 初始化Toolbar与Title文字
     * */
    protected void initToolbarWithLeftDrawerAndMenu(Toolbar toolbar, String title, int menuId, Toolbar.OnMenuItemClickListener listener) {
        this.toolbar = toolbar;
        toolbar.setTitle(title);
        // 初始化颜色以及ICON
        if (menuId != -1) {
            toolbar.inflateMenu(menuId);
            toolbar.setOnMenuItemClickListener(listener);
        }
//        toolbar.setNavigationOnClickListener(v->Rxbus.getInstance().send(new DrawerEvent(GravityCompat.START, DrawerEvent.TOGGLE)));
        ToolbarHelper.applyToolbarIconAndTheme(toolbar, getActivity(), isNowNightModeOn);
    }


    /**
     * 处理事件 TODO: 重新处理
     * */
    public void handleRxEvent(Object event){
        if (event instanceof NightModeEvent) {
            isNowNightModeOn = ((NightModeEvent) event).nightModeOn;
            ToolbarHelper.applyToolbarIconAndTheme(toolbar, getActivity(), isNowNightModeOn);
            /* 改变Root */
            NightWatcher.switchToModeNight(getView(), isNowNightModeOn);
        }
    }


}
