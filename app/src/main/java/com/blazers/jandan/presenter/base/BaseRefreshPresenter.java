package com.blazers.jandan.presenter.base;

/**
 * Created by blazers on 2016/11/30.
 */

public abstract class BaseRefreshPresenter<T> extends BasePresenter<T> {

    /**
     * 起始加载的页面
     */
    protected int mPage = 1;

    /**
     * 是否处于刷新状态
     */
    protected boolean mIsRefreshing = false;

    /**
     *
     * @param view MVP V
     */
    BaseRefreshPresenter(T view) {
        super(view);
    }


    /**
     * 进入页面刷新数据
     */
    public abstract void init(long lastRefreshTime);


    /**
     * 请求刷新数据
     */
    public abstract void refresh();
}
