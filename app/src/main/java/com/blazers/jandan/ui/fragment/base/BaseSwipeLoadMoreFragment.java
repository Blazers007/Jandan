package com.blazers.jandan.ui.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.views.GreySpaceItemDecoration;
import com.blazers.jandan.views.loadmore.LoadMoreRecyclerView;
import com.blazers.jandan.views.loadmore.PullCallback;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;
//import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by Blazers on 2015/10/16.
 */
public abstract class BaseSwipeLoadMoreFragment extends BaseFragment {

    @Bind(R.id.swipe_container) public SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) public LoadMoreRecyclerView loadMoreRecyclerView;
    @Bind(R.id.load_more_progress) public SmoothProgressBar smoothProgressBar;

    /* Vars */
    public Realm realm;
    private boolean isLoading = false;
    private boolean isLoadAllItems = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        realm = Realm.getInstance(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        realm.close();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    /**
     * 设置SwipeRefreshLayout 适当分离 不需要刻意整合在一起
     * */
    public void trySetupSwipeRefreshLayout() {
        if (null != swipeRefreshLayout) {
            swipeRefreshLayout.setOnRefreshListener(this::invokeRefresh);
        }
    }

    public void trySetupRecyclerViewWithAdapter(RecyclerView.Adapter adapter) {
        if (null != loadMoreRecyclerView && null != adapter) {
            loadMoreRecyclerView.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
            loadMoreRecyclerView.addItemDecoration(new GreySpaceItemDecoration());
//            loadMoreRecyclerView.setItemAnimator(new SlideInUpAnimator());
            loadMoreRecyclerView.setPullCallback(new PullCallback() {
                @Override
                public void onLoadMore() {
                    if (isLoading())
                        return;
                    Log.i(TAG, "开始刷新");
                    isLoading = true;
                    invokeLoadMore();
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return isLoadAllItems;
                }
            });
            loadMoreRecyclerView.setAdapter(adapter);
        }
    }


    /**
     * 下拉刷新
     * */
    private void invokeRefresh() {
        refresh();
    }

    public abstract void refresh();

    public void refreshComplete() {
        if (null != swipeRefreshLayout) {
            swipeRefreshLayout.postDelayed(()->swipeRefreshLayout.setRefreshing(false), 800);
            Log.i(TAG, "刷新成功");
        }
    }

    public void refreshError() {
        if (null != swipeRefreshLayout) {
            swipeRefreshLayout.postDelayed(()->swipeRefreshLayout.setRefreshing(false), 800);
            Log.i(TAG, "刷新失败");
        }
    }

    /**
     * 上拉加载更多
     * */
    private void invokeLoadMore() {
        smoothProgressBar.setVisibility(View.VISIBLE);
        loadMore();
    }

    /**
     * 将Rx逻辑部分整理至该模块中 便于精简代码
     * */
    public abstract void loadMore();

    public void loadMoreComplete() {
        if (null != smoothProgressBar && null != loadMoreRecyclerView) {
            smoothProgressBar.setVisibility(View.GONE);
            isLoading = false;
            Log.i(TAG, "加载更多完毕");
        }
    }

    public void loadMoreError() {
        if (null != smoothProgressBar && null != loadMoreRecyclerView) {
            smoothProgressBar.setVisibility(View.GONE);
            isLoading = false;
            Log.i(TAG, "加载更多失败");
        }
    }


}
