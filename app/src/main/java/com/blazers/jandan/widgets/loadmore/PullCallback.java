package com.blazers.jandan.widgets.loadmore;

/**
 * Created by Blazers on 2015/10/20.
 */
public interface PullCallback {

    void onLoadMore();

    boolean isLoading();

    boolean hasLoadedAllItems();

}
