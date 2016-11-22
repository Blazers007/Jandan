package com.blazers.jandan.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.blazers.jandan.R;
import com.blazers.jandan.widgets.AutoScaleFrescoView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Blazers on 2016/1/8.
 */
public class ImageDetailActivity extends AppCompatActivity implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.75f;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    // Vars
    private List<String> mUrls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);
        mUrls = getIntent().getStringArrayListExtra("List");
        mViewPager.setAdapter(new DetailPagerAdapter());
        mViewPager.setCurrentItem(getIntent().getIntExtra("Position", 0));
        mViewPager.setPageTransformer(true, this);
        mViewPager.setOffscreenPageLimit(2);
    }

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);
        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when
            // moving to the left page
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view.setAlpha(1 - position);
            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);
            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }


    /* Pager Adapter */
    class DetailPagerAdapter extends PagerAdapter {

        private SparseArray<View> mCache = new SparseArray<>();

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            AutoScaleFrescoView autoScaleFrescoView = new AutoScaleFrescoView(ImageDetailActivity.this);
            autoScaleFrescoView.showImage(null, mUrls.get(position));
            container.addView(autoScaleFrescoView);
            mCache.put(position, autoScaleFrescoView);
            return autoScaleFrescoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mCache.get(position));
        }

        @Override
        public int getCount() {
            return mUrls == null ? 0 : mUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
