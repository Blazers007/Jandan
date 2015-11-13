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
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.views.loadmore.LoadMoreRecyclerView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;

/**
 * Created by Blazers on 2015/11/13.
 */
public abstract class BaseSwipeRefreshFragment extends BaseFragment {

    @Bind(R.id.recycler_list) public RecyclerView recyclerView;
    @Bind(R.id.swipe_container) public SwipeRefreshLayout swipeRefreshLayout;

    /* Vars */
    public Realm realm;
    protected String type;

    @Override
    public void setTAG(String TAG) {
        super.setTAG(TAG);
        type = TAG; // 没有指定的默认为TAG的值
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        realm = Realm.getInstance(context);
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

    /**
     * 下拉刷新
     * */
    private void invokeRefresh() {
        refresh();
    }

    public abstract void refresh();

    public void refreshComplete() {
        SPHelper.setLastRefreshTime(getActivity(), type);
        if (null != swipeRefreshLayout) {
            swipeRefreshLayout.postDelayed(()->swipeRefreshLayout.setRefreshing(false), 1200);
            Log.i(TAG, "刷新成功");
        }
    }

    public void refreshError() {
        if (null != swipeRefreshLayout) {
            swipeRefreshLayout.postDelayed(()->swipeRefreshLayout.setRefreshing(false), 1200);
            Log.i(TAG, "刷新失败");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        realm.close();
    }

}
