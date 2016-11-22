package com.blazers.jandan.ui.activity;

import android.os.Bundle;

import com.blazers.jandan.R;
import com.blazers.jandan.presenter.OfflinePresenter;
import com.blazers.jandan.ui.activity.base.BaseActivity;

/**
 * Created by blazers on 2016/11/18.
 */

public class OfflineActivity extends BaseActivity<OfflinePresenter> implements OfflineView {

    @Override
    public void initPresenter() {
        mPresenter = new OfflinePresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
    }
}
