package com.blazers.jandan.ui.fragment.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import com.blazers.jandan.util.log.Log;
import android.view.View;

import com.blazers.jandan.R;
import com.blazers.jandan.presenter.base.BaseLoadMoreRefreshPresenter;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.widgets.VerticalDividerItemDecoration;
import com.blazers.jandan.widgets.loadmore.LoadMoreRecyclerView;
import com.blazers.jandan.widgets.loadmore.PullCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
//import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by Blazers on 2015/10/16.
 */
public abstract class BaseSwipeLoadMoreFragment<T extends BaseLoadMoreRefreshPresenter> extends BaseSwipeRefreshFragment<T> implements BaseLoadMoreRefreshView {

    @BindView(R.id.recycler_list)
    public LoadMoreRecyclerView mLoadMoreRecyclerView;
    @BindView(R.id.load_more_progress)
    public MaterialProgressBar mSmoothProgressBar;

    /* Vars */
    private boolean mIsLoading = false;
    private boolean isLoadAllItems = false;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    public void trySetupRecyclerViewWithAdapter(RecyclerView.Adapter adapter) {
        trySetupRecyclerViewWithAdapter(adapter, 18, Color.rgb(241, 242, 241));
    }

    public void trySetupRecyclerViewWithAdapter(RecyclerView.Adapter adapter, int dividerHeight, int color) {
        if (null != mLoadMoreRecyclerView && null != adapter) {
            mLoadMoreRecyclerView.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
            mLoadMoreRecyclerView.addItemDecoration(new VerticalDividerItemDecoration(getActivity(), 18, Color.rgb(241, 242, 241)));
            mLoadMoreRecyclerView.setItemAnimator(new DefaultItemAnimator());
//            mLoadMoreRecyclerView.setItemAnimator(new SlideInUpAnimator());
            mLoadMoreRecyclerView.setPullCallback(new PullCallback() {
                @Override
                public void onLoadMore() {
                    loadMore();
                }

                @Override
                public boolean isLoading() {
                    return mIsLoading;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return isLoadAllItems;
                }
            });
            mLoadMoreRecyclerView.setAdapter(adapter);
        }
    }

    /**
     * 将Rx逻辑部分整理至该模块中 便于精简代码
     */
    public void loadMore() {
        if (mIsLoading)
            return;
        Log.i(mTAG, "==LoadMore==");
        mIsLoading = true;
        showLoadMoreView();
        mPresenter.loadMore();
    }


    @Override
    public void showLoadMoreView() {
        if (null != mSmoothProgressBar && null != mLoadMoreRecyclerView) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoadMoreView(boolean successful) {
        if (null != mSmoothProgressBar && null != mLoadMoreRecyclerView) {
            mSmoothProgressBar.setVisibility(View.GONE);
            mIsLoading = false;
            Log.i(mTAG, "加载更多失败");
        }
    }
}
