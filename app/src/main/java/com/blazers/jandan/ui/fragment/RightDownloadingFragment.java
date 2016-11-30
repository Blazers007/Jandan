package com.blazers.jandan.ui.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blazers.jandan.R;
import com.blazers.jandan.model.pojo.count.Count;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.widgets.InfiniteSeekBar;
import com.blazers.jandan.widgets.SelectableTextView;

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

@RuntimePermissions
public class RightDownloadingFragment extends Fragment {

    public static final String ACTION_COUNT = "action.count";

    @BindViews({R.id.seg_news, R.id.seg_wuliao, R.id.seg_jokes, R.id.seg_meizi})
    List<SelectableTextView> segments;
    @BindView(R.id.page_seek_bar)
    InfiniteSeekBar pageSeekBar;
    // 控制显示与隐藏于数量的更新
    @BindView(R.id.meizhi_title)
    TextView meizhiTitle;
    @BindViews({R.id.news_count, R.id.wuliao_count, R.id.joke_count, R.id.meizhi_count})
    List<TextView> countLabels;
    private CountReceiver receiver;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        receiver = new CountReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_COUNT);
        context.registerReceiver(receiver, filter);

    }

    @Override
    public void onDetach() {
        if (null != receiver) {
            getContext().unregisterReceiver(receiver);
        }
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_offline, container, false);
        ButterKnife.bind(this, root);
        init();
        return root;
    }

    void init() {
        if (!SPHelper.getBooleanSP(getActivity(), SPHelper.MEIZI_MODE_ON, false)) {
            segments.get(3).setVisibility(View.GONE);
            meizhiTitle.setVisibility(View.GONE);
            countLabels.get(3).setVisibility(View.GONE);
        }
        // Read Count from sp
        for (int i = 0 ; i < 4 ; i ++)
            countLabels.get(i).setText(String.format("%d", SPHelper.getLongSP(getActivity(), "Count" + i, 0)));
    }

    @OnClick(R.id.button)
    public void clickDownload() {
        if (Build.VERSION.SDK_INT >= 23
                && getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            RightDownloadingFragmentPermissionsDispatcher.showWriteExternalStorageWithCheck(this);
        } else {
            tryDownload();
        }
    }


    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showWriteExternalStorage() {
        tryDownload();
    }


    private void tryDownload() {
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

    /**
     * 开始离线阅读
     * */
    private void startDownload() {
//        if (binder != null) {
//            try {
//                int page = pageSeekBar.getSelectedValue();
//                String toast = "";
//                // 判断
//                if (segments.get(0).isSegSelected()) {
//                    binder.startDownloadNews(1, page);
//                    toast += "新鲜事,";
//                }
//                if (segments.get(1).isSegSelected()) {
//                    binder.startDownloadPicture("wuliao",1, page);
//                    toast += "无聊图,";
//                }
//                if (segments.get(2).isSegSelected()) {
//                    binder.startDownloadJokes(1, page);
//                    toast += "段子,";
//                }
//                if (segments.get(3).isSegSelected()) {
//                    binder.startDownloadPicture("meizi", 1, page);
//                    toast += "妹子图,";
//                }
//                Toast.makeText(getActivity(), "已经开始离线: " + toast, Toast.LENGTH_SHORT).show();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 监听
     * */
    class CountReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Count count = (Count)intent.getSerializableExtra("data");
            countLabels.get(count.type).setText(String.format("%d", count.count));
            // Save
            SPHelper.putLongSP(getActivity(), "Count" + count.type, count.count);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RightDownloadingFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
