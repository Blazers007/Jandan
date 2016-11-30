package com.blazers.jandan.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.blazers.jandan.R;
import com.blazers.jandan.presenter.base.BasePresenter;
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

    /* Vars */

    protected String type;

    @Override
    public void setTAG(String TAG) {
        super.setTAG(TAG);
        type = TAG; // 没有指定的默认为TAG的值
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
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
        Log.i(TAG, "==Refreshing==");
        mIsRefreshing = true;
        mPresenter.onRefresh();
    }


    @Override
    public void hideRefreshingView(boolean successful) {
        if (successful) {
            SPHelper.setLastRefreshTime(getActivity(), type);
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
