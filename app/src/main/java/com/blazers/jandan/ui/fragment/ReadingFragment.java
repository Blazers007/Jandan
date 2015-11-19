package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.rxbus.Rxbus;
import com.blazers.jandan.rxbus.event.DrawerEvent;
import com.blazers.jandan.rxbus.event.NightModeEvent;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.ui.fragment.readingsub.JokeFragment;
import com.blazers.jandan.ui.fragment.readingsub.NewsFragment;
import com.blazers.jandan.ui.fragment.readingsub.PicFragment;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.views.nightwatch.NightWatcher;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/10/12.
 *
 * 主 Fragment负责管理四个阅读板块
 *
 */
public class ReadingFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener{

    public static final String TAG = ReadingFragment.class.getSimpleName();
    private static ReadingFragment INSTANCE;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.container) ViewPager viewPager;


    private ArrayList<Fragment> fragments;
    private String[] titles = {"新鲜事" , "无聊图", "段子", "妹子图"};

    public static ReadingFragment getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ReadingFragment();
            INSTANCE.setTAG(TAG);
            INSTANCE.setNeedUmengStatic(false);
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_holder_reading, container, false);
        ButterKnife.bind(this, root);
        initJandanFragments();
        setupTabLayoutTheme();
        initToolbarWithLeftDrawerAndMenu(toolbar, "煎蛋阅读", R.menu.offline, this);
        return root;
    }

    /**
     * 初始化各个Fragment
     * */
    void initJandanFragments() {
        fragments = new ArrayList<>();
        fragments.add(new NewsFragment());
        fragments.add(PicFragment.newInstance("wuliao"));
        fragments.add(new JokeFragment());
        // 是否需要加载妹纸页面
        if (SPHelper.getBooleanSP(getActivity(), SPHelper.MEIZI_MODE_ON, false))
            fragments.add(PicFragment.newInstance("meizi"));
        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
//        viewPager.setPageMargin(Dppx.Dp2Px(getActivity(), 12));  //  TODO: 需要适配夜间模式
//        viewPager.setOffscreenPageLimit(fragments.size());        // 暂不缓存 能够自动释放
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 初始化TabLayout的主题
     * */
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


    /**
     * 点击Menu
     * */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.offline) {
            Rxbus.getInstance().send(new DrawerEvent(GravityCompat.END, DrawerEvent.OPEN_DRAWER_AND_LOCK));
            return true;
        }
        return false;
    }

    /**
     * Adapter
     * */
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

    @Override
    public void handleRxEvent(Object event) {
        if (event instanceof NightModeEvent) {
            isNowNightModeOn = ((NightModeEvent) event).nightModeOn;
            applyToolbarIconAndTheme();
            NightWatcher.switchToModeNight(getView(), isNowNightModeOn);
            /* Extra 改变TabLayout */
            setupTabLayoutTheme();
        }
    }
}
