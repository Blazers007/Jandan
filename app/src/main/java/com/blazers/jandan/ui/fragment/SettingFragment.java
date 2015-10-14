package com.blazers.jandan.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.activity.MainActivity;


/**
 * Created by Blazers on 2015/10/14.
 *
 * http://stackoverflow.com/questions/9783368/alternatives-to-preferencefragment-with-android-support-v4
 *
 */
public class SettingFragment extends Fragment {

    @Bind(R.id.toolbar) Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, root);

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_black_24dp);
        toolbar.setTitle("设置");
        toolbar.setNavigationOnClickListener(v -> ((MainActivity) getActivity()).popupFragment());

        return root;
    }
}
