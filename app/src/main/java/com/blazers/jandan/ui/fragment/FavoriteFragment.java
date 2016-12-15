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
import android.widget.TextView;

import com.blazers.jandan.R;
import com.blazers.jandan.model.event.EnterSelectModeEvent;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.ui.fragment.favoritesub.FavoriteImageFragment;
import com.blazers.jandan.ui.fragment.favoritesub.FavoriteJokesFragment;
import com.blazers.jandan.ui.fragment.favoritesub.FavoriteNewsFragment;
import com.blazers.jandan.util.SPHelper;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Blazers on 2015/10/12.
 */
public class FavoriteFragment extends BaseFragment {

    public static final String TAG = FavoriteFragment.class.getSimpleName();
    //    private static final String[] titles = new String[]{"时间轴", "新鲜事", "图片", "文字"};
    private static final String[] titles = new String[]{"新鲜事", "图片", "文字"};

    @BindView(R.id.avatar)
    SimpleDraweeView mAvatar;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.fav_news_count)
    TextView mFavNewsCount;
    @BindView(R.id.fav_images_count)
    TextView mFavImagesCount;
    @BindView(R.id.fav_jokes_count)
    TextView mFavJokesCount;

    /* Vars */
//    @Bind(R.id.fab_fav) FloatingActionButton floatingActionButton;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_holder_favorite;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initFavAdapter();
        setupTabLayoutTheme();
        loadFavCounts();
//        registerEventReceiver();
    }

    void initFavAdapter() {
        String name = SPHelper.getStringSP(getActivity(), SPHelper.NAME, null);
        // TODO: 封装头像工具 统一地方处理权限申请！ 梳理多个地方需要同一个权限的时候如何用PermissionDispatcher做优化处理
//        mUserName.setText(name == null ? Unique.generateName(getActivity()) : name);
//        mAvatar.setImageURI(Uri.parse(Unique.generateGavatar(getActivity(), null)));
        mViewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
        // 点击修改名字
    }

    void loadFavCounts() {
//        mFavNewsCount.setText(String.format("%d", realm.where(LocalFavNews.class).count()));
//        mFavImagesCount.setText(String.format("%d", realm.where(LocalFavImages.class).count()));
//        mFavJokesCount.setText(String.format("%d", realm.where(LocalFavJokes.class).count()));
    }

    /**
     * 初始化TabLayout的主题
     */
    void setupTabLayoutTheme() {
        if (isNowNightModeOn) {
            mTabLayout.setBackgroundColor(Color.rgb(44, 44, 44));
            mTabLayout.setTabTextColors(Color.parseColor("#666666"), Color.parseColor("#fafafa"));
            mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#cccccc"));
        } else {
            mTabLayout.setBackgroundColor(Color.rgb(250, 250, 250));
            mTabLayout.setTabTextColors(Color.parseColor("#989898"), Color.parseColor("#343434"));
            mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#434343"));
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unregisterEventReceiver();
    }

    @Override
    public void handleRxEvent(Object event) {
        super.handleRxEvent(event);
        if (event instanceof EnterSelectModeEvent) {
//            floatingActionButton.animate().scaleX(1).scaleY(1).setDuration(400).setInterpolator(new BounceInterpolator()).start();
        }
    }

    /**
     * Adapter
     */
    class FragmentAdapter extends FragmentPagerAdapter {

        private FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FavoriteNewsFragment();
                case 1:
                    return new FavoriteImageFragment();
                case 2:
                    return new FavoriteJokesFragment();
                default:
                    return null;
            }

//            new FavoriteTimelineFragment()
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
