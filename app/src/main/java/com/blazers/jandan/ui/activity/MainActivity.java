package com.blazers.jandan.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.blazers.jandan.R;
import com.blazers.jandan.common.Static;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.ui.fragment.FavoriteFragment;
import com.blazers.jandan.ui.fragment.MineFragment;
import com.blazers.jandan.ui.fragment.ReadingFragment;
import com.blazers.jandan.util.ClipboardHelper;

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
public class MainActivity extends BaseActivity {

    private static final String KEY_CURRENT_FRAGMENT_TAG = "current_fragment";
    private static final String TAG_READING = "fragment_reading";
    private static final String TAG_FAVORITE = "fragment_favorite";
    private static final String TAG_MINE = "fragment_mine";
    private String mCurrentFragmentTag;

    @BindView(R.id.coord)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.navigation_view)
    BottomNavigationView mBottomNavigationView;

    /* 缓存变量 */
    private Fragment mCurrentFragment, mReadingFragment, mFavoriteFragment, mMineFragment;
    /**
     * 处理回退键
     */
    private long lastClickTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomNavigationView();
        if (savedInstanceState != null) {
            mCurrentFragmentTag = savedInstanceState.getString(KEY_CURRENT_FRAGMENT_TAG, null);
        }
        if (mCurrentFragmentTag != null) {
            switchToFragmentByTag(mCurrentFragmentTag);
        } else {
            /* 显示阅读Fragment */
            switchToFragmentByTag(TAG_READING);
        }
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
    void initBottomNavigationView() {
        // 设置底部导航回调
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_jandan:
                    switchToFragmentByTag(TAG_READING);
                    break;
                case R.id.nav_fav:
                    switchToFragmentByTag(TAG_FAVORITE);
                    if (!Once.beenDone(Once.THIS_APP_INSTALL, Static.HINT_FAV)) {
                        Snackbar.make(findViewById(R.id.coord), R.string.remove_fav_hint, Snackbar.LENGTH_SHORT)
                                .setActionTextColor(getResources().getColor(R.color.yellow500))
                                .setAction("不再提示", v -> {
                                    Once.markDone(Static.HINT_FAV);
                                })
                                .show();
                    }
                    break;
                case R.id.nav_mine:
                    switchToFragmentByTag(TAG_MINE);
                    break;
            }
            return true;
        });

        // Test CoordinatorLayout
    }


    /**
     * 根据TAG切换至指定Fragment
     * @param tag
     */
    private void switchToFragmentByTag(String tag) {
        switch (tag) {
            case TAG_READING:
                if (mReadingFragment == null && (mReadingFragment = findFragmentByTag(TAG_READING)) == null) {
                    mReadingFragment = new ReadingFragment();
                }
                mCurrentFragment = switchCurrentFragment(R.id.fragment_wrapper, TAG_READING, mCurrentFragment, mReadingFragment);
                break;
            case TAG_FAVORITE:
                if (mFavoriteFragment == null && (mFavoriteFragment = findFragmentByTag(TAG_FAVORITE)) == null) {
                    mFavoriteFragment = new FavoriteFragment();
                }
                mCurrentFragment = switchCurrentFragment(R.id.fragment_wrapper, TAG_FAVORITE, mCurrentFragment, mFavoriteFragment);
                break;
            case TAG_MINE:
                if (mMineFragment == null && (mMineFragment = findFragmentByTag(TAG_MINE)) == null) {
                    mMineFragment = new MineFragment();
                }
                mCurrentFragment = switchCurrentFragment(R.id.fragment_wrapper, TAG_MINE, mCurrentFragment, mMineFragment);
                break;
        }
        mCurrentFragmentTag = tag;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_CURRENT_FRAGMENT_TAG, mCurrentFragmentTag);
        super.onSaveInstanceState(outState);
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
