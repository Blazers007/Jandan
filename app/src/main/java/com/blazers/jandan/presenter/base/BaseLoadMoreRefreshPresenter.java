package com.blazers.jandan.presenter.base;

/**
 * Created by blazers on 2016/11/30.
 */

public abstract class BaseLoadMoreRefreshPresenter<T> extends BaseRefreshPresenter<T> {


    public BaseLoadMoreRefreshPresenter(T view) {
        super(view);
    }

    public abstract void onLoadMore();
}
