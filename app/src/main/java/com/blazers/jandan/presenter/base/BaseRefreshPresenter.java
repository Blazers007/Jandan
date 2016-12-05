package com.blazers.jandan.presenter.base;

/**
 * Created by blazers on 2016/11/30.
 */

public abstract class BaseRefreshPresenter<T> extends BasePresenter<T> {

    public BaseRefreshPresenter(T view) {
        super(view);
    }

    public abstract void onInitPageData();

    public abstract void onRefresh();
}
