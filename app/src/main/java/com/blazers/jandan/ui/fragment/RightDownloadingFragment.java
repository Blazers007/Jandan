package com.blazers.jandan.ui.fragment;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.blazers.jandan.R;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.services.DownloadService;
import io.realm.Realm;

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

    private Realm realm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        realm = Realm.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_right_downloading, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @OnClick(R.id.button)
    public void download() {
        // 取消缓存PendingIntent
        PendingIntent cancelIntent = PendingIntent.getService(getActivity(), 500, new Intent(getActivity(), DownloadService.class), PendingIntent.FLAG_ONE_SHOT);
        //
        Notification notification = new NotificationCompat.Builder(getActivity())
            .setTicker("开始缓存")
            .addAction(R.mipmap.ic_cancel_grey600_24dp, "取消缓存", cancelIntent)
            .build();
        Parser.getInstance().offlineMeizi(1, 10);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        realm.close();
    }
}
