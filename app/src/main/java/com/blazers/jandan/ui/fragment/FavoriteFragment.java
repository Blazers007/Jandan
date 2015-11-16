package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.ui.fragment.favoritesub.FavoriteJokesFragment;
import com.blazers.jandan.ui.fragment.favoritesub.FavoriteImageFragment;
import com.blazers.jandan.ui.fragment.favoritesub.FavoriteNewsFragment;
import com.blazers.jandan.ui.fragment.favoritesub.FavoriteTimelineFragment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/10/12.
 */
public class FavoriteFragment extends BaseFragment {

    public static final String TAG = FavoriteFragment.class.getSimpleName();
    private static FavoriteFragment INSTANCE;

    private static final String[] titles = new String[]{"时间轴", "新鲜事", "图片", "文字"};

    /* Vars */
    private ArrayList<Fragment> fragments;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.avatar) SimpleDraweeView avatar;
    @Bind(R.id.view_pager) ViewPager viewPager;
    @Bind(R.id.tab_layout) TabLayout tabLayout;


    public static FavoriteFragment getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new FavoriteFragment();
            INSTANCE.setTAG(TAG);
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_holder_favorite, container, false);
        ButterKnife.bind(this, root);
        initToolbarAndLeftDrawer(toolbar, "收藏");
        initTest();
        setupTabLayoutTheme();
        return root;
    }

    void initTest() {
        avatar.setImageURI(Uri.parse("http://eightbitavatar.herokuapp.com/?id=blazers&s=male&size=320"));
        fragments = new ArrayList<>();
        fragments.add(new FavoriteTimelineFragment());
        fragments.add(new FavoriteNewsFragment());
        fragments.add(new FavoriteImageFragment());
        fragments.add(new FavoriteJokesFragment());
        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
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
}
