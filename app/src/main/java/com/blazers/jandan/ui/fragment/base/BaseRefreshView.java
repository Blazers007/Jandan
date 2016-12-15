package com.blazers.jandan.ui.fragment.base;

/**
 * Created by blazers on 2016/11/30.
 */

public interface BaseRefreshView {

    void setTag(String tag);

    void onShowRefreshing();

    void onHideRefreshing(boolean successful);
}
