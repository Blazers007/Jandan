package com.blazers.jandan.presenter.base;

/**
 * Created by blazers on 2016/11/30.
 */

public abstract class BaseLoadMoreRefreshPresenter<T> extends BaseRefreshPresenter<T> {

    /**
     * 是否在读取中
     */
    protected boolean mIsLoading = false;

    /**
     * 是否已经加载完毕
     */
    protected boolean mToTheEnd = false;

    /**
     *
     * @param view MVP V
     */
    public BaseLoadMoreRefreshPresenter(T view) {
        super(view);
    }

    /**
     * 请求加载更多
     */
    public abstract void loadMore();
}
