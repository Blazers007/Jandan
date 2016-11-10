package com.blazers.jandan.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.blazers.jandan.IOfflineDownloadInterface;
import com.blazers.jandan.R;
import com.blazers.jandan.common.Static;
import com.blazers.jandan.rxbus.event.CommentEvent;
import com.blazers.jandan.rxbus.event.NightModeEvent;
import com.blazers.jandan.rxbus.event.ViewArticleEvent;
import com.blazers.jandan.rxbus.event.ViewImageEvent;
import com.blazers.jandan.services.OfflineDownloadService;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.ui.fragment.FavoriteFragment;
import com.blazers.jandan.ui.fragment.ReadingFragment;
import com.blazers.jandan.ui.fragment.SettingFragment;
import com.blazers.jandan.util.ClipboardHelper;
import com.blazers.jandan.util.DBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    public static final String JANDAN_TAG = "fragment_jandan";
    public static final String FAV_TAG = "fragment_fav";
    public static final String SETTING_TAG = "fragment_setting";

    @BindView(R.id.navigation_view)
    BottomNavigationView mBottomNavigationView;

    /* 缓存变量 */
    private Fragment mCurrentFragment;
    private ReadingFragment mReadingFragment;
    private FavoriteFragment mFavoriteFragment;
    private SettingFragment mSettingFragment;

    /**
     * 处理回退键
     */
    private long lastClickTime;
    /**
     * 绑定离线下载服务 TODO:修改为懒绑定
     */
    private IOfflineDownloadInterface offlineBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            offlineBinder = (IOfflineDownloadInterface) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            offlineBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        /* RxBus 与Fragment中部分 进行代码整合  */
        setHasRegisterDemand(true);
        /* 绑定离线下载服务 todo: 重新整理结构 */
        startService(new Intent(this, OfflineDownloadService.class));
        bindService(new Intent(this, OfflineDownloadService.class), serviceConnection, BIND_AUTO_CREATE);
        /* 根据需要填充主界面所加载的Fragment */
        initFragmentsAndBottomNavigationView();
        /**
         *
         * // 打开时的时候如果SDK 》= 23 且没有允许权限 则询问权限
         if (Build.VERSION.SDK_INT >= 23
         && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
         MainActivityPermissionsDispatcher.showReadPhoneStateWithCheck(MainActivity.this);
         }
         */
        /* Setup Transition */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Fade fade = new Fade();
//            fade.setDuration(200);
//            getWindow().setExitTransition(fade);
//            getWindow().setReenterTransition(fade);
//        }
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
        /* 设置导航选中状态 */
        /**
         * if (getSupportFragmentManager().findFragmentByTag(FAV_TAG) == null) {
         transaction.add(R.id.fragment_wrapper, FavoriteFragment.getInstance(), FAV_TAG);
         } else {
         Stream.of(getSupportFragmentManager().getFragments())
         .filter(n -> n != null)
         .forEach(transaction::hide);
         transaction.show(FavoriteFragment.getInstance());
         }
         nowSelectedNavId = R.id.nav_fav;
         // 提示 长按取消收藏
         if (!Once.beenDone(Once.THIS_APP_INSTALL, Static.HINT_FAV)) {
         Snackbar.make(navigationView, R.string.remove_fav_hint, Snackbar.LENGTH_SHORT)
         .setActionTextColor(getResources().getColor(R.color.yellow500))
         .setAction("不再提示", v -> {
         Once.markDone(Static.HINT_FAV);
         })
         .show();
         }
         *
         *
         */
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_jandan:
                    switchCurrentFragment(JANDAN_TAG, ReadingFragment.getInstance());
                    break;
                case R.id.nav_fav:
                    switchCurrentFragment(FAV_TAG, FavoriteFragment.getInstance());
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
                    break;
                case R.id.nav_mine:
                    switchCurrentFragment(SETTING_TAG, SettingFragment.getInstance());
                    break;
            }
            return true;
        });

    }

    private void switchCurrentFragment(String tag, Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Stream.of(getSupportFragmentManager().getFragments())
                .filter(n -> n != null)
                .forEach(transaction::hide);
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            transaction.add(R.id.fragment_wrapper, targetFragment, tag);
        } else {
            transaction.show(targetFragment);
        }
        transaction.commit();
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
     * 处理Event消息  TODO: 尽量精简 避免不必要的Event通信
     */
    @Override
    public void handleRxEvent(Object event) {
        if (event instanceof CommentEvent) {
            /* 查看评论页面 或离 开评 论页面 */
            long id = ((CommentEvent) event).id;
            startActivity(new Intent(this, CommentActivity.class).putExtra("commentId", id));
        } else if (event instanceof ViewArticleEvent) {
            ViewArticleEvent v = (ViewArticleEvent) event;
            startActivity(new Intent(this, NewsReadActivity.class)
                    .putExtra("id", v.id)
                    .putExtra("title", v.title)
            );
            overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
        } else if (event instanceof ViewImageEvent) {
            /* 查看图片请求 */
            ViewImageEvent imageEvent = ((ViewImageEvent) event);
            Intent intent = new Intent(this, ImageViewerActivity.class);
            intent.putExtra(ViewImageEvent.KEY, imageEvent);
            startActivity(intent);
        } else if (event instanceof NightModeEvent) {
            isNowNightModeOn = ((NightModeEvent) event).nightModeOn;
            // 处理Activity内部的相关View  目前暂无
        }
    }

    /**
     * 由于Bind是异步的 建议提前绑定
     */
    public IOfflineDownloadInterface getOfflineBinder() {
        if (offlineBinder == null)
            Toast.makeText(this, "离线下载服务还木有准备完毕", Toast.LENGTH_SHORT).show();
        return offlineBinder;
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
     * 释放
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        DBHelper.releaseAllTempRealm();
        // Marked as closed so Service need to finish by itself
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
