package com.blazers.jandan.presenter;

import android.content.Context;
import android.util.Log;

import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.presenter.base.BaseLoadMoreRefreshPresenter;
import com.blazers.jandan.ui.fragment.readingsub.JokeView;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.util.TimeHelper;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by blazers on 2016/12/2.
 */

public class JokePresenter extends BaseLoadMoreRefreshPresenter<JokeView> {

    private int mPage = 1;

    public JokePresenter(JokeView view, Context context) {
        super(view, context);
    }


    @Override
    public void onInitPageData() {
        Log.i("Presenter", "==OnInitJokePageData==");
        DataManager.getInstance()
                .getJokeDataFromDB(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    Log.e("onInitJokePageData", "==OnNext==");
                    // 有数据则首先显示
                    if (posts!= null && !posts.isEmpty()) {
                        mView.refreshDataList(posts);
                        // 在根据刷新时间判断是否需要刷新
                        if (TimeHelper.isTimeEnoughForRefreshing(SPHelper.getLastRefreshTime(getActivity(), "jokes"))) {
//                            onRefresh();
                        }
                    } else {
                        // 无数据则直接刷新
//                        onRefresh();
                    }
                }, error -> {
                    Log.e("onInitJokePageData", error.toString());
                    onRefresh();
                }); // 出错直接刷新
    }

    @Override
    public void onRefresh() {
        Log.i("Presenter", "==OnRefresh==");
        mPage = 1;
        DataManager.getInstance().getJokeData(getContext(), mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    // 刷新成功 更新UI
                    if (posts!= null && !posts.isEmpty()) {
                        mView.refreshDataList(posts);
                        mView.hideRefreshingView(true);
                    }
                    // 刷新失败 不更新UI弹出提示
                }, error -> {
                    Log.e("onRefresh", error.toString());
                    mView.hideRefreshingView(false);
                });
    }


    @Override
    public void onLoadMore() {
        mPage++;
        DataManager.getInstance().getJokeData(getContext(), mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    // 刷新成功 更新UI
                    mView.addDataList(posts);
                    mView.hideLoadMoreView(true);
                    // 刷新失败 不更新UI弹出提示
                }, error -> {
                    Log.e("onLoadMore", error.toString());
                    mView.hideLoadMoreView(false);
                });
    }

}
