package com.blazers.jandan.presenter.base;

/**
 * Created by blazers on 2016/11/30.
 */

public abstract class BaseRefreshPresenter<T> extends BasePresenter<T> {

    protected int mPage = 1;
    protected boolean mIsRefreshing = false;

    public BaseRefreshPresenter(T view) {
        super(view);
    }

    public abstract void initPageData();

    public abstract void refresh();
}
