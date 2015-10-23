package com.blazers.jandan.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.activity.MainActivity;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.ui.fragment.sub.JokeFragment;
import com.blazers.jandan.ui.fragment.sub.NewsFragment;
import com.blazers.jandan.ui.fragment.sub.PicFragment;
import com.blazers.jandan.util.Dppx;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/10/12.
 */
public class ReadingFragment extends BaseFragment {

    public static final String TAG = ReadingFragment.class.getSimpleName();
    private static ReadingFragment INSTANCE;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.container) ViewPager viewPager;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;


    private ArrayList<Fragment> fragments;
    private String[] titles = {"新鲜事" , "无聊图", "段子", "妹子图"};

    public static ReadingFragment getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ReadingFragment();
            INSTANCE.setTAG(TAG);
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_reading, container, false);
        ButterKnife.bind(this, root);
        initDownloadFragment();
        initJandanFragments();
        initToolbarAndLeftDrawer(toolbar, "煎蛋");
        setHasOptionsMenu(true);
        return root;
    }


    void initDownloadFragment() {

    }

    void initJandanFragments() {
        fragments = new ArrayList<>();
        fragments.add(new NewsFragment());
        fragments.add(PicFragment.newInstance("wuliao"));
        fragments.add(new JokeFragment());
        fragments.add(PicFragment.newInstance("meizi"));
        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        /* TODO: 根据屏幕尺寸设置TabMode */
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setPageMargin(Dppx.Dp2Px(getActivity(), 12));
        viewPager.setOffscreenPageLimit(4);
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        private FragmentManager fm;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_jandan, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.offline:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
        }
        return true;
    }
}
