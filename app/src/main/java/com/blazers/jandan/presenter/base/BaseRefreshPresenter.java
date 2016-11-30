package com.blazers.jandan.presenter.base;

import android.content.Context;

/**
 * Created by blazers on 2016/11/30.
 */

public abstract class BaseRefreshPresenter<T> extends BasePresenter<T> {

    public BaseRefreshPresenter(T view, Context context) {
        super(view, context);
    }

    public abstract void onRefresh();
}
