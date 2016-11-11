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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalFavImages;
import com.blazers.jandan.models.db.local.LocalFavJokes;
import com.blazers.jandan.models.db.local.LocalFavNews;
import com.blazers.jandan.util.rxbus.event.EnterSelectModeEvent;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.ui.fragment.favoritesub.FavoriteImageFragment;
import com.blazers.jandan.ui.fragment.favoritesub.FavoriteJokesFragment;
import com.blazers.jandan.ui.fragment.favoritesub.FavoriteNewsFragment;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.util.Unique;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Blazers on 2015/10/12.
 */
public class FavoriteFragment extends BaseFragment {

    public static final String TAG = FavoriteFragment.class.getSimpleName();
    //    private static final String[] titles = new String[]{"时间轴", "新鲜事", "图片", "文字"};
    private static final String[] titles = new String[]{"新鲜事", "图片", "文字"};
    private static FavoriteFragment INSTANCE;

    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.fav_news_count)
    TextView favNewsCount;
    @BindView(R.id.fav_images_count)
    TextView favImagesCount;
    @BindView(R.id.fav_jokes_count)
    TextView favJokesCount;
    /* Vars */
    private ArrayList<Fragment> fragments;
//    @Bind(R.id.fab_fav) FloatingActionButton floatingActionButton;

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
        initFavFragments();
        setupTabLayoutTheme();
        loadFavCounts();
        registerEventReceiver();
        return root;
    }

    void initFavFragments() {
        String name = SPHelper.getStringSP(getActivity(), SPHelper.NAME, null);
        userName.setText(name == null ? Unique.generateName(getActivity()) : name);
        avatar.setImageURI(Uri.parse(Unique.generateGavatar(getActivity(), null)));
        fragments = new ArrayList<>();
//        fragments.add(new FavoriteTimelineFragment());  // 暂时不使用
        fragments.add(new FavoriteNewsFragment());
        fragments.add(new FavoriteImageFragment());
        fragments.add(new FavoriteJokesFragment());
        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        // 点击修改名字
    }

    void loadFavCounts() {
        favNewsCount.setText(String.format("%d", realm.where(LocalFavNews.class).count()));
        favImagesCount.setText(String.format("%d", realm.where(LocalFavImages.class).count()));
        favJokesCount.setText(String.format("%d", realm.where(LocalFavJokes.class).count()));
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
    public void onDestroyView() {
        super.onDestroyView();
        unregisterEventReceiver();
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
