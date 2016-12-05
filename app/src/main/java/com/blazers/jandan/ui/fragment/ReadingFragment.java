package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.blazers.jandan.R;
import com.blazers.jandan.model.event.NightModeEvent;
import com.blazers.jandan.presenter.ImagePresenter;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.ui.fragment.readingsub.ImageFragment;
import com.blazers.jandan.ui.fragment.readingsub.JokeFragment;
import com.blazers.jandan.ui.fragment.readingsub.NewsFragment;
import com.blazers.jandan.widgets.nightwatch.NightWatcher;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Blazers on 2015/10/12.
 * <p>
 * 主 Fragment负责管理四个阅读板块
 */
public class ReadingFragment extends BaseFragment {

    public static final String TAG = ReadingFragment.class.getSimpleName();

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.container)
    ViewPager viewPager;


    private ArrayList<Fragment> fragments;
    private String[] titles = {"新鲜事", "段子", "无聊图", "妹子图"};


    @Override
    protected void initPresenter() {
        // No logic
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_holder_reading;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initJandanFragments();
        setupTabLayoutTheme();
    }

    /**
     * 初始化各个Fragment
     */
    void initJandanFragments() {
        fragments = new ArrayList<>();
        fragments.add(new NewsFragment());
        fragments.add(new JokeFragment());
        fragments.add(ImageFragment.newInstance(ImagePresenter.TAG_WULIAO));
        // 是否需要加载妹纸页面
//        if (SPHelper.getBooleanSP(getActivity(), SPHelper.MEIZI_MODE_ON, false))
            fragments.add(ImageFragment.newInstance(ImagePresenter.TAG_MEIZHI));
        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
//        mViewPager.setPageMargin(Dppx.Dp2Px(getActivity(), 12));  //  TODO: 需要适配夜间模式
//        mViewPager.setOffscreenPageLimit(fragments.size());        // 暂不缓存 能够自动释放
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 初始化TabLayout的主题
     */
    void setupTabLayoutTheme() {
        if (isNowNightModeOn) {
            tabLayout.setBackgroundColor(Color.rgb(44, 44, 44));
            tabLayout.setTabTextColors(Color.parseColor("#666666"), Color.parseColor("#fafafa"));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#cccccc"));
        } else {
            tabLayout.setBackgroundColor(Color.rgb(250, 250, 250));
            tabLayout.setTabTextColors(Color.parseColor("#989898"), Color.parseColor("#343434"));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#434343"));
        }
    }


    @Override
    public void handleRxEvent(Object event) {
        if (event instanceof NightModeEvent) {
            isNowNightModeOn = ((NightModeEvent) event).nightModeOn;
//            applyToolbarIconAndTheme();
            NightWatcher.switchToModeNight(getView(), isNowNightModeOn);
            /* Extra 改变TabLayout */
            setupTabLayoutTheme();
        }
    }


    /**
     * Adapter
     */
    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
