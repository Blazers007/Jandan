package com.blazers.jandan.ui.fragment.jandan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.activity.MainActivity;
import com.blazers.jandan.util.Dppx;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/10/12.
 */
public class JandanFragmentHolder extends Fragment {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.container) ViewPager viewPager;

    private FragmentAdapter adapter;
    private ArrayList<Fragment> fragments;
    private String[] titles = {"新鲜事" , "无聊图", "段子", "妹子图"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.holder_jandan, container, false);
        // 读取 savedInstanceState
        ButterKnife.bind(this, root);
        initToolbarAndLeftDrawer();
        initDownloadFragment();
        // 载入Fragment
        initJandanFragments();
        setHasOptionsMenu(true);
        return root;
    }

    void initToolbarAndLeftDrawer() {
        ((MainActivity)getActivity()).initDrawerWithToolbar(toolbar);
        toolbar.setTitle("煎蛋");
    }

    void initDownloadFragment() {

    }

    void initJandanFragments() {
        fragments = new ArrayList<>();
        fragments.add(new NewsFragment());
        fragments.add(PicFragment.newInstance("wuliao"));
        fragments.add(new JokeFragment());
        fragments.add(PicFragment.newInstance("meizi"));
        viewPager.setAdapter(adapter = new FragmentAdapter(getChildFragmentManager()));
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
        return super.onOptionsItemSelected(item);
    }
}
