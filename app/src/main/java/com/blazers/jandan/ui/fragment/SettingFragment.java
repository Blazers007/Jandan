package com.blazers.jandan.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.fragment.base.BaseFragment;


/**
 * Created by Blazers on 2015/10/14.
 *
 * http://stackoverflow.com/questions/9783368/alternatives-to-preferencefragment-with-android-support-v4
 *
 */
public class SettingFragment extends BaseFragment {

    public static final String TAG = SettingFragment.class.getSimpleName();
    public static SettingFragment INSTANCE;

    @Bind(R.id.toolbar) Toolbar toolbar;

    public static SettingFragment getInstance() {
        if (INSTANCE == null){
            INSTANCE = new SettingFragment();
            INSTANCE.setTAG(TAG);
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, root);
        initToolbarAndLeftDrawer(toolbar, "设置");
        return root;
    }

}
