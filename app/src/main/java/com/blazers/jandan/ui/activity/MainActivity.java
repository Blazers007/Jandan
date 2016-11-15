package com.blazers.jandan.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.ViewPropertyAnimator;
import android.widget.Toast;

import com.blazers.jandan.R;
import com.blazers.jandan.common.Static;
import com.blazers.jandan.presenter.MainPresenter;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.ui.fragment.FavoriteFragment;
import com.blazers.jandan.ui.fragment.ReadingFragment;
import com.blazers.jandan.ui.fragment.SettingFragment;
import com.blazers.jandan.util.ClipboardHelper;
import com.blazers.jandan.util.rxbus.event.ViewArticleEvent;
import com.blazers.jandan.util.rxbus.event.ViewCommentEvent;
import com.blazers.jandan.util.rxbus.event.ViewImageEvent;

import butterknife.BindView;
import jonathanfinerty.once.Once;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * Update @ 2015 11-16
 * 1: Activity不在持有Toolbar 以及 Menu 全部交付 Fragment管理
 */
@RuntimePermissions
public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    public static final String JANDAN_TAG = "fragment_jandan";
    public static final String FAV_TAG = "fragment_fav";
    public static final String SETTING_TAG = "fragment_setting";

    @BindView(R.id.coord)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.navigation_view)
    BottomNavigationView mBottomNavigationView;

    /* 缓存变量 */
    private Fragment mCurrentFragment;
    private ViewPropertyAnimator mHide, mShow;

    /**
     * 处理回退键
     */
    private long lastClickTime;

    @Override
    public void initPresenter() {
        mPresenter = new MainPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragmentsAndBottomNavigationView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* 仅仅当程序在前台的时候 注册监听 */
        ClipboardHelper.registerClipboardListener(this);
    }

    /**
     * 初始化各个Fragment 并采用懒加载的方式
     */
    void initFragmentsAndBottomNavigationView() {
        /* 显示阅读Fragment */
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_wrapper, mCurrentFragment = ReadingFragment.getInstance(), JANDAN_TAG)
                .commitAllowingStateLoss();
        // 是否记录上此位置？
        // 设置底部导航回调
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_jandan:
                    switchCurrentFragment(R.id.fragment_wrapper, JANDAN_TAG, ReadingFragment.getInstance());
                    break;
                case R.id.nav_fav:
                    switchCurrentFragment(R.id.fragment_wrapper, FAV_TAG, FavoriteFragment.getInstance());
                    if (!Once.beenDone(Once.THIS_APP_INSTALL, Static.HINT_FAV)) {
                        Snackbar.make(findViewById(R.id.coord), R.string.remove_fav_hint, Snackbar.LENGTH_SHORT)
                                .setActionTextColor(getResources().getColor(R.color.yellow500))
                                .setAction("不再提示", v -> {
                                    Once.markDone(Static.HINT_FAV);
                                })
                                .show();
                    }
                    break;
                case R.id.nav_downloading:
                    Snackbar.make(findViewById(R.id.coord), R.string.remove_fav_hint, Snackbar.LENGTH_SHORT)
                            .setActionTextColor(getResources().getColor(R.color.yellow500))
                            .setAction("不再提示", v -> {
                                Once.markDone(Static.HINT_FAV);
                            })
                            .show();
                    break;
                case R.id.nav_mine:
                    switchCurrentFragment(R.id.fragment_wrapper, SETTING_TAG, SettingFragment.getInstance());
                    break;
            }
            return true;
        });

        // Test CoordinatorLayout
    }

    @Override
    public void showBottomNavigationView() {

    }

    @Override
    public void hideBottomNavigationView() {

    }

    @Override
    public void gotoCommentActivity(ViewCommentEvent commentEvent) {
        startActivity(new Intent(this, CommentActivity.class).putExtra(ViewCommentEvent.KEY, commentEvent));
    }

    @Override
    public void gotoViewArticleActivity(ViewArticleEvent viewArticleEvent) {
        startActivity(new Intent(this, ArticleReadActivity.class).putExtra(ViewArticleEvent.KEY, viewArticleEvent));
        overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    public void gotoViewImageActivity(ViewImageEvent viewImageEvent) {
        startActivity(new Intent(this, ImageInspectActivity.class).putExtra(ViewImageEvent.KEY, viewImageEvent));
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    void showReadPhoneState() {
        Toast.makeText(this, "已经允许,马上来新的头像", Toast.LENGTH_SHORT).show();
        // 权限已经被允许
//        ((SimpleDraweeView) navigationView.getHeaderView(0).findViewById(R.id.user_head_round))
//                .setImageURI(Uri.parse(Unique.generateGavatar(this, null)));
    }

    @OnShowRationale(Manifest.permission.READ_PHONE_STATE)
    void showRationalReadPhoneState(PermissionRequest request) {
        // TODO 耐心解释过后的允许情况
    }

    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    void onDeniedForReadPhoneState() {
        // TODO 被拒绝的情况
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    void onNeverAskForReadPhoneState() {
        // TODO 点击永不显示请求权限的情况
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @Override
    public void onBackPressed() {
        // 1 若当前的Fragment的BackStack回退栈有叠加的Fragment则退出之
        if (mCurrentFragment != null && mCurrentFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            mCurrentFragment.getChildFragmentManager().popBackStack();
            return;
        }
        // 2 点击退出
        if (System.currentTimeMillis() - lastClickTime > 2000) {
            Toast.makeText(this, "再次点击退出~", Toast.LENGTH_SHORT).show();
            lastClickTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 解除监听Clipboard
     */
    @Override
    protected void onPause() {
        super.onPause();
        ClipboardHelper.unregisterListener();
    }

    /**
     *
     *
     // 打开时的时候如果SDK 》= 23 且没有允许权限 则询问权限
     if (Build.VERSION.SDK_INT >= 23
     && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
     MainActivityPermissionsDispatcher.showReadPhoneStateWithCheck(MainActivity.this);
     }

     // Transitions
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     Fade fade = new Fade();
     fade.setDuration(200);
     getWindow().setExitTransition(fade);
     getWindow().setReenterTransition(fade);
     }

     private void startAndBindDownloadService() {
     startService(new Intent(this, OfflineDownloadService.class));
     bindService(new Intent(this, OfflineDownloadService.class), serviceConnection, BIND_AUTO_CREATE);
     }

     private void unbindDownloadService() {
     unbindService(serviceConnection);
     }

     private IOfflineDownloadInterface offlineBinder;
     private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override public void onServiceConnected(ComponentName name, IBinder service) {
    offlineBinder = (IOfflineDownloadInterface) service;
    }

    @Override public void onServiceDisconnected(ComponentName name) {
    offlineBinder = null;
    }
    };

     */
}
