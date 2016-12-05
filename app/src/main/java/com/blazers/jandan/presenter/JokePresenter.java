package com.blazers.jandan.presenter;

import android.util.Log;

import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.presenter.base.BaseLoadMoreRefreshPresenter;
import com.blazers.jandan.ui.fragment.readingsub.JokeView;
import com.blazers.jandan.util.ListHelper;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by blazers on 2016/12/2.
 */

public class JokePresenter extends BaseLoadMoreRefreshPresenter<JokeView> {

    public static final String TAG_JOKE = "joke";

    private int mPage = 1;

    public JokePresenter(JokeView view) {
        super(view);
        view.setTag(TAG_JOKE);
    }

    @Override
    public void onInitPageData() {
        Log.i("Presenter", "==OnInitJokePageData==");
        addFUISubscription(DataManager.getInstance()
                .getJokeDataFromDB(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    // 有数据则首先显示
                    if (ListHelper.isNotEmptySafe(posts)) {
                        mView.refreshDataList(posts);
                        // 在根据刷新时间判断是否需要刷新
//                        if (TimeHelper.isTimeEnoughForRefreshing(SPHelper.getLastRefreshTime(TAG_JOKE))) {
//                            onRefresh();
//                        }
                    } else {
                        // 无数据则直接刷新
                        onRefresh();
                    }
                }, error -> {
                    System.out.println("onInitJokePage: " + error.toString());
                    onRefresh();
                }));
    }

    @Override
    public void onRefresh() {
        Log.i("Presenter", "==OnRefresh==");
        mPage = 1;
        addFUISubscription(DataManager.getInstance().getJokeData(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    // 刷新成功 更新UI
                    if (ListHelper.isNotEmptySafe(posts)) {
                        mView.refreshDataList(posts);
                        mView.hideRefreshingView(true);
                    } else {
                        mView.hideRefreshingView(false);
                    }
                    // 刷新失败 不更新UI弹出提示
                }, error -> {
                    System.out.println("onRefreshJoke: " + error.toString());
                    mView.hideRefreshingView(false);
                }));
    }


    @Override
    public void onLoadMore() {
        mPage++;
        addFUISubscription(DataManager.getInstance().getJokeData(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    // 刷新成功 更新UI
                    if (ListHelper.isNotEmptySafe(posts)) {
                        mView.addDataList(posts);
                        mView.hideLoadMoreView(true);
                    } else {
                        mView.hideLoadMoreView(false);
                    }
                    // 刷新失败 不更新UI弹出提示
                }, error -> {
                    System.out.println("onLoadMoreJoke: " + error.toString());
                    mView.hideLoadMoreView(false);
                }));
    }

}
