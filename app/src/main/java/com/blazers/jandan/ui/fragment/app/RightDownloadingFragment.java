package com.blazers.jandan.ui.fragment.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.blazers.jandan.R;

/**
 * Created by Blazers on 2015/9/2.
 */
public class RightDownloadingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_right_downloading, container, false);
        ButterKnife.bind(this, root);
//        root.setFitsSystemWindows(true); // Status bar still white?

        /**
         * http://stackoverflow.com/questions/26440879/how-do-i-use-drawerlayout-to-display-over-the-actionbar-toolbar-and-under-the-st/26440880
         *
         * Only works fine with Google NavigationView !
         *
         * If you have two drawer the second won't works well
         *
         * */

        return root;
    }
}
