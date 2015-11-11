package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blazers.jandan.IOfflineDownloadInterface;
import com.blazers.jandan.R;
import com.blazers.jandan.rxbus.Rxbus;
import com.blazers.jandan.rxbus.event.DrawerEvent;
import com.blazers.jandan.ui.activity.MainActivity;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.views.InfiniteSeekBar;
import com.blazers.jandan.views.SelectableTextView;

import java.util.List;


/**
 * Created by Blazers on 2015/9/2.
 *
 * 离线下载
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

    @Bind({R.id.seg_news, R.id.seg_wuliao, R.id.seg_jokes, R.id.seg_meizi}) List<SelectableTextView> segments;
    @Bind(R.id.page_seek_bar) InfiniteSeekBar pageSeekBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_right_downloading, container, false);
        ButterKnife.bind(this, root);
        init();
        return root;
    }

    void init() {
        if (!SPHelper.getBooleanSP(getActivity(), SPHelper.MEIZI_MODE_ON, false)) {
            segments.get(3).setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.button)
    public void download() {
        if (NetworkHelper.isWifi(getActivity())) {
            startDownload();
        } else {
            new MaterialDialog.Builder(getActivity())
                .title(R.string.not_in_wifi)
                .content(R.string.not_in_wifi_message)
                .positiveText(R.string.do_offline_download)
                .positiveColor(Color.rgb(240, 114, 175)) // 需要采用Color
                .negativeText(R.string.negetive)
                .negativeColor(Color.rgb(109, 109, 109))
                .onPositive((dialog, action)->startDownload())
                .build()
                .show();
        }
    }

    private void startDownload() {
        IOfflineDownloadInterface binder = ((MainActivity) getActivity()).getOfflineBinder();
        if (binder != null) {
            try {
                int page = pageSeekBar.getSelectedValue();
                String toast = "";
                // 判断
                if (segments.get(0).isSegSelected()) {
                    binder.startDownloadNews(1, page);
                    toast += "新鲜事,";
                }
                if (segments.get(1).isSegSelected()) {
                    binder.startDownloadPicture("wuliao",1, page);
                    toast += "无聊图,";
                }
                if (segments.get(2).isSegSelected()) {
                    binder.startDownloadJokes(1, page);
                    toast += "段子,";
                }
                if (segments.get(3).isSegSelected()) {
                    binder.startDownloadPicture("meizi", 1, page);
                    toast += "妹子图,";
                }
                Toast.makeText(getActivity(), "已经开始离线: " + toast, Toast.LENGTH_SHORT).show();
                Rxbus.getInstance().send(new DrawerEvent(DrawerEvent.CLOSE_DRAWER_AND_LOCK));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.close)
    public void closeDrawer(){
        Rxbus.getInstance().send(new DrawerEvent(DrawerEvent.CLOSE_DRAWER_AND_LOCK));
    }
}
