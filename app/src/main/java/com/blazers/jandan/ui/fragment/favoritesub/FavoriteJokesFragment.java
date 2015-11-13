package com.blazers.jandan.ui.fragment.favoritesub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.fragment.base.BaseSwipeRefreshFragment;

/**
 * Created by Blazers on 2015/11/13.
 */
public class FavoriteJokesFragment extends BaseSwipeRefreshFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common_fav_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        trySetupSwipeRefreshLayout();
    }

    @Override
    public void refresh() {

    }
}
