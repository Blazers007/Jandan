package com.blazers.jandan.ui.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.blazers.jandan.IOfflineDownloadInterface;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.activity.MainActivity;


/**
 * Created by Blazers on 2015/9/2.
 */

//        root.setFitsSystemWindows(true); // Status bar still white?

/**
 * http://stackoverflow.com/questions/26440879/how-do-i-use-drawerlayout-to-display-over-the-actionbar-toolbar-and-under-the-st/26440880
 *
 * Only works fine with Google NavigationView !
 *
 * If you have two drawer the second won't works well
 *
 * */


public class RightDownloadingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_right_downloading, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @OnClick(R.id.button)
    public void download() {
        IOfflineDownloadInterface binder = ((MainActivity) getActivity()).getOfflineBinder();
        if (binder != null) {
            try {
                binder.startDownloadPicture("wuliao",1, 3);
                binder.startDownloadPicture("meizi", 1, 3);
                binder.startDownloadNews(1, 3);
                binder.startDownloadJokes(1, 3);
                ReadingFragment.getInstance().closeDrawer();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
