package com.blazers.jandan.util;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Blazers on 2015/9/7.
 */
public class RecyclerViewHelper {

    public static LinearLayoutManager getVerticalLinearLayoutManager(Activity activity) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

    public static RecyclerView.ItemDecoration getDefaultVeriticalDivider(Activity activity) {
        return new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST);
    }

}
