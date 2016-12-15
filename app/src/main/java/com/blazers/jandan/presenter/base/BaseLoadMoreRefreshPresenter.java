package com.blazers.jandan.presenter.base;

/**
 * Created by blazers on 2016/11/30.
 */

public abstract class BaseLoadMoreRefreshPresenter<T> extends BaseRefreshPresenter<T> {

    protected boolean mIsLoading = false;
    protected boolean mToTheEnd = false;

    public BaseLoadMoreRefreshPresenter(T view) {
        super(view);
    }

    public abstract void loadMore();
}
