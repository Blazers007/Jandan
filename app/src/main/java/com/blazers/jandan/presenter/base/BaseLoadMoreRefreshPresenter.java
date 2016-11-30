package com.blazers.jandan.presenter.base;

import android.content.Context;

/**
 * Created by blazers on 2016/11/30.
 */

public abstract class BaseLoadMoreRefreshPresenter<T> extends BaseRefreshPresenter<T> {


    public BaseLoadMoreRefreshPresenter(T view, Context context) {
        super(view, context);
    }

    public abstract void onLoadMore();
}
