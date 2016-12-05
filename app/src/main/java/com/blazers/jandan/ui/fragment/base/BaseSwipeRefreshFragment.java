package com.blazers.jandan.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.blazers.jandan.R;
import com.blazers.jandan.presenter.base.BaseRefreshPresenter;
import com.blazers.jandan.util.SPHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Blazers on 2015/11/13.
 */
public abstract class BaseSwipeRefreshFragment<T extends BaseRefreshPresenter> extends BaseFragment<T> implements BaseRefreshView {

    @BindView(R.id.recycler_list)
    public RecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public boolean mIsRefreshing;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void setTag(String tag) {
        mTAG = tag;
    }

    /**
     * 设置SwipeRefreshLayout 适当分离 不需要刻意整合在一起
     */
    public void trySetupSwipeRefreshLayout() {
        if (null != mSwipeRefreshLayout) {
            mSwipeRefreshLayout.setOnRefreshListener(this::refresh);
        }
    }

    public void refresh() {
        if (mIsRefreshing) {
            return;
        }
        Log.i(mTAG, "==Refreshing==");
        mIsRefreshing = true;
        mPresenter.onRefresh();
    }


    @Override
    public void hideRefreshingView(boolean successful) {
        if (successful) {
            SPHelper.setLastRefreshTime(getActivity(), mTAG);
        }
        if (null != mSwipeRefreshLayout) {
            mSwipeRefreshLayout.postDelayed(() -> {
                mSwipeRefreshLayout.setRefreshing(false);
                mIsRefreshing = false;
            }, 800);
        }
    }

    @Override
    public void showRefreshingView() {
        if (null != mSwipeRefreshLayout) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }
}
