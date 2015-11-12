package com.blazers.jandan.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.ui.fragment.favoritesub.FavoriteMeizhiFragment;
import com.blazers.jandan.views.loadmore.LoadMoreRecyclerView;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/10/12.
 */
public class FavoriteFragment extends BaseFragment {

    public static final String TAG = FavoriteFragment.class.getSimpleName();
    private static FavoriteFragment INSTANCE;

    /* Vars */
    private ArrayList<Fragment> fragments;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.view_pager) ViewPager viewPager;


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
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, root);
        initToolbarAndLeftDrawer(toolbar, "收藏");
        initTest();
        return root;
    }

    void initTest() {
        fragments = new ArrayList<>();
        fragments.add(new FavoriteMeizhiFragment());
        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
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
    }
}
