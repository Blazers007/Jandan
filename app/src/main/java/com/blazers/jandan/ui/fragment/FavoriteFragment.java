package com.blazers.jandan.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.fragment.base.BaseFragment;

/**
 * Created by Blazers on 2015/10/12.
 */
public class FavoriteFragment extends BaseFragment {

    public static final String TAG = FavoriteFragment.class.getSimpleName();
    private static FavoriteFragment INSTANCE;

    @Bind(R.id.toolbar) Toolbar toolbar;

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
        return root;
    }

}
