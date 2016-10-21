package com.blazers.jandan.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.blazers.jandan.IOfflineDownloadInterface;
import com.blazers.jandan.R;
import com.blazers.jandan.common.Static;
import com.blazers.jandan.rxbus.event.CommentEvent;
import com.blazers.jandan.rxbus.event.DrawerEvent;
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
import com.blazers.jandan.util.Unique;
import com.facebook.drawee.view.SimpleDraweeView;
import butterknife.BindView;
import butterknife.ButterKnife;
import jonathanfinerty.once.Once;


/**
 * Update @ 2015 11-16
 * 1: Activity不在持有Toolbar 以及 Menu 全部交付 Fragment管理
 */
public class MainActivity extends BaseActivity {

    public static final String JANDAN_TAG = "fragment_jandan";
    public static final String FAV_TAG = "fragment_fav";
    public static final String SETTING_TAG = "fragment_setting";

//    @BindView(R.id.drawer_layout)
//    DrawerLayout drawerLayout;
//    @BindView(R.id.left_nav)
//    NavigationView navigationView;

    /* 缓存变量 */
    private int nowSelectedNavId = R.id.nav_jandan;
    private ReadingFragment defaultFragment;
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
        /* 绑定离线下载服务 */
        startService(new Intent(this, OfflineDownloadService.class));
        bindService(new Intent(this, OfflineDownloadService.class), serviceConnection, BIND_AUTO_CREATE);
        /* 根据需要填充主界面所加载的Fragment */
        initFragments();
        /* 初始化Drawer */
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        /* 设置NavigationView */
        setupNavigationView();
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
    void initFragments() {
        /* 显示阅读Fragment */
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_wrapper, defaultFragment = ReadingFragment.getInstance(), JANDAN_TAG)
                .commitAllowingStateLoss();
        /* 设置导航选中状态 */
//        navigationView.setCheckedItem(R.id.nav_jandan);
        /* 设置监听 */
//        navigationView.setNavigationItemSelectedListener(menuItem -> {
//            if (menuItem.getItemId() != nowSelectedNavId) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                switch (menuItem.getItemId()) {
//                    case R.id.nav_jandan:
//                        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//                            if (fragment != null)
//                                transaction.hide(fragment);
//                        }
//                        transaction.show(defaultFragment);
//                        nowSelectedNavId = R.id.nav_jandan;
//                        break;
//                    case R.id.nav_fav:
//                        if (getSupportFragmentManager().findFragmentByTag(FAV_TAG) == null) {
//                            transaction.add(R.id.fragment_wrapper, FavoriteFragment.getInstance(), FAV_TAG);
//                        } else {
//                            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//                                if (fragment != null)
//                                    transaction.hide(fragment);
//                            }
//                            transaction.show(FavoriteFragment.getInstance());
//                        }
//                        nowSelectedNavId = R.id.nav_fav;
//                        // 提示 长按取消收藏
//                        if (!Once.beenDone(Once.THIS_APP_INSTALL, Static.HINT_FAV)) {
//                            Toast.makeText(this, R.string.remove_fav_hint, Toast.LENGTH_SHORT).show();
//                            Once.markDone(Static.HINT_FAV);
//                        }
//                        break;
//                    case R.id.nav_setting:
//                        if (getSupportFragmentManager().findFragmentByTag(SETTING_TAG) == null) {
//                            transaction.add(R.id.fragment_wrapper, SettingFragment.getInstance(), SETTING_TAG);
//                        } else {
//                            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//                                if (fragment != null)
//                                    transaction.hide(fragment);
//                            }
//                            // TODO http://stackoverflow.com/questions/22489703/trying-to-remove-fragment-from-view-gives-me-nullpointerexception-on-mnextanim
//                            transaction.show(SettingFragment.getInstance());
//                        }
//                        nowSelectedNavId = R.id.nav_setting;
//                        break;
//                }
//                transaction.commitAllowingStateLoss();
//            }
//            drawerLayout.closeDrawer(GravityCompat.START);
//            return true;
//        });
    }

    /**
     * Setup NavigationView Background Color
     */
    void setupNavigationView() {
//        if (isNowNightModeOn) {
//            navigationView.setBackgroundColor(Color.rgb(44, 44, 44));
//        } else {
//            navigationView.setBackgroundColor(Color.rgb(250, 250, 250));
//        }
//        // 头像
//        if (Build.VERSION.SDK_INT >= 23) {
//            RxPermissions.getInstance(this)
//                    .request(Manifest.permission.READ_PHONE_STATE)
//                    .subscribe(granted -> {
//                        if (granted) {
//                            ((SimpleDraweeView) navigationView.getHeaderView(0).findViewById(R.id.user_head_round)).setImageURI(Uri.parse(Unique.generateGavatar(this, null)));
//                        } else {
//                            Toast.makeText(this, "XX", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else {
//            ((SimpleDraweeView) navigationView.getHeaderView(0).findViewById(R.id.user_head_round)).setImageURI(Uri.parse(Unique.generateGavatar(this, null)));
//        }
    }

    @Override
    public void onBackPressed() {
        // 1 若打开了右侧Drawer关闭之
//        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
//            drawerLayout.closeDrawer(GravityCompat.END);
//            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
//            return;
//        }
//        // 2 若当前BackStack回退栈有Fragment 退出之
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//            return;
//        }
        if (nowSelectedNavId != R.id.nav_jandan) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null)
                    transaction.hide(fragment);
            }
            transaction.show(defaultFragment).commitAllowingStateLoss();
            nowSelectedNavId = R.id.nav_jandan;
//            navigationView.setCheckedItem(R.id.nav_jandan);
            return;
        }
        // 3 点击退出
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
        } else if (event instanceof ViewImageEvent) {
            /* 查看图片请求 */
            ViewImageEvent imageEvent = ((ViewImageEvent) event);
            Intent intent = new Intent(this, ImageViewerActivity.class);
            intent.putExtra(ViewImageEvent.KEY, imageEvent);
            startActivity(intent);
        } else if (event instanceof NightModeEvent) {
            isNowNightModeOn = ((NightModeEvent) event).nightModeOn;
            // 处理Activity内部的相关View  目前暂无
        } else if (event instanceof DrawerEvent) {
//            DrawerEvent drawerEvent = (DrawerEvent) event;
//            switch (drawerEvent.messageType) {
//                case DrawerEvent.TOGGLE:
//                    if (drawerLayout.isDrawerOpen(drawerEvent.gravity))
//                        drawerLayout.closeDrawer(drawerEvent.gravity);
//                    else
//                        drawerLayout.openDrawer(drawerEvent.gravity);
//                    break;
//                case DrawerEvent.OPEN_DRAWER:
//                    drawerLayout.openDrawer(drawerEvent.gravity);
//                    break;
//                case DrawerEvent.OPEN_DRAWER_AND_LOCK:
//                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, drawerEvent.gravity);
//                    break;
//                case DrawerEvent.CLOSE_DRAWER:
//                    drawerLayout.closeDrawer(drawerEvent.gravity);
//                    break;
//                case DrawerEvent.CLOSE_DRAWER_AND_LOCK:
//                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, drawerEvent.gravity);
//                    break;
//            }
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
}
