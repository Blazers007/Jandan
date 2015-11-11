package com.blazers.jandan.ui.activity;

import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.IOfflineDownloadInterface;
import com.blazers.jandan.R;
import com.blazers.jandan.rxbus.event.*;
import com.blazers.jandan.services.OfflineDownloadService;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.ui.fragment.*;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.util.ClipboardHelper;
import com.blazers.jandan.util.SPHelper;
import com.umeng.analytics.MobclickAgent;


public class MainActivity extends BaseActivity {

    public static final String JANDAN_TAG = "fragment_jandan";
    public static final String FAV_TAG = "fragment_fav";
    public static final String SETTING_TAG = "fragment_setting";

    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.left_nav) NavigationView navigationView;

    /* 缓存变量 */
    private int nowSelectedNavId = R.id.nav_jandan;
    private ReadingFragment defaultFragment;

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
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        /* 设置NavigationView */
        setupNavigationView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* 仅仅当程序在前台的时候 注册监听 */
        ClipboardHelper.registerClipboardListener(this);
    }

    /**
     * 初始化各个Fragment 并采用懒加载的方式
     * */
    void initFragments() {
        /* 显示阅读Fragment */
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.fragment_wrapper, defaultFragment = ReadingFragment.getInstance(), JANDAN_TAG)
            .commitAllowingStateLoss();
        /* 设置导航选中状态 */
        navigationView.setCheckedItem(R.id.nav_jandan);
        navigationView.postDelayed(defaultFragment::reboundToolbar, 200);  // 为何需要重新bind一下？
        /* 设置监听 */
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() != nowSelectedNavId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                BaseFragment choose = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_jandan:
                        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                            if (fragment != null)
                                transaction.hide(fragment);
                        }
                        transaction.show(defaultFragment);
                        nowSelectedNavId = R.id.nav_jandan;
                        choose = defaultFragment;
                        break;
                    case R.id.nav_fav:
                        if (getSupportFragmentManager().findFragmentByTag(FAV_TAG) == null) {
                            transaction.add(R.id.fragment_wrapper, choose = FavoriteFragment.getInstance(), FAV_TAG);
                        } else {
                            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                                if (fragment != null)
                                    transaction.hide(fragment);
                            }
                            transaction.show(choose = FavoriteFragment.getInstance());
                        }
                        nowSelectedNavId = R.id.nav_fav;
                        break;
                    case R.id.nav_setting:
                        if (getSupportFragmentManager().findFragmentByTag(SETTING_TAG) == null) {
                            transaction.add(R.id.fragment_wrapper, choose = SettingFragment.getInstance(), SETTING_TAG);
                        } else {
                            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                                if (fragment != null)
                                    transaction.hide(fragment);
                            }
                            /*TODO
                            * http://stackoverflow.com/questions/22489703/trying-to-remove-fragment-from-view-gives-me-nullpointerexception-on-mnextanim
                            * */
                            transaction.show(choose = SettingFragment.getInstance());
                        }
                        nowSelectedNavId = R.id.nav_setting;
                        break;
                }
                transaction.commitAllowingStateLoss();
                if (choose != null)
                    choose.reboundToolbar();
//                    navigationView.postDelayed(choose::reboundToolbar, 300);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    /**
     * Setup NavigationView
     * */
    void setupNavigationView() {
        if (isNowNightModeOn) {
            navigationView.setBackgroundColor(Color.rgb(44,44,44));
        }else {
            navigationView.setBackgroundColor(Color.rgb(250,250,250));
        }
    }

    /**
     * 将呈现的Fragment的Toolbar绑定到Drawer上去 TODO:重新整理代码结构 减少耦合度 Apply代码放入正确的Cla
     * */
    private void initDrawerWithToolbar(Toolbar toolbar) {
        initToolbarByTypeWithShadow(null, toolbar, ToolbarType.NORMAL);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
        /* 貌似上面的方法会还原默认ICON 所以需要重新更换NavIcon */
        if (isNowNightModeOn) {
            final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_menu_grey600_24dp);
            upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(upArrow);
        } else {
            final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_menu_grey600_24dp);
            upArrow.setColorFilter(Color.parseColor("#3c4043"), PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(upArrow);
        }
        /* 需不需要放在此处? */
        invalidateOptionsMenu();
    }

    /**
     * 滑入评论Fragment
     * */
    private void pushInCommentFragment(long id) {
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.activity_slide_right_in, R.anim.activity_slide_right_out, R.anim.activity_slide_right_in, R.anim.activity_slide_right_out)
            .add(R.id.fragment_wrapper, CommentFragment.NewInstance(id))
            .addToBackStack(null)
            .commitAllowingStateLoss();
    }

    /**
     * 滑出Fragment
     * */
    private void popupCommentFragment() {
        getSupportFragmentManager().popBackStack();
    }

    /**
     * 初始化菜单
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jandan, menu);
        return true;
    }

    /**
     * 由于此Activity本身没有任何UI 所以仅仅管理Menu 以及Nav 的颜色 其余的主题由各个Fragment处理
     * */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for (int i = 0 ; i < menu.size() ; i ++) {
            MenuItem item = menu.getItem(i);
            Drawable icon = item.getIcon();
            if (isNowNightModeOn) {
                icon.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
            } else {
                icon.setColorFilter(Color.parseColor("#3c4043"), PorterDuff.Mode.SRC_ATOP);
            }
            item.setIcon(icon);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.offline:
                drawerLayout.openDrawer(GravityCompat.END);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, GravityCompat.END);
                break;
        }
        return true;
    }


    /**
     * 处理回退键
     * */
    private long lastClickTime;
    @Override
    public void onBackPressed() {
        // 1 若打开了右侧Drawer关闭之
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
            return;
        }
        // 2 若当前BackStack回退栈有Fragment 退出之
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
            return;
        }
        // 3 点击退出
        if (System.currentTimeMillis() - lastClickTime > 2000) {
            Toast.makeText(this, "再次点击退出~", Toast.LENGTH_SHORT).show();
            lastClickTime = System.currentTimeMillis();
        }else{
            super.onBackPressed();
        }
    }

    /**
     * 处理Event消息
     * */
    @Override
    public void handleRxEvent(Object event) {
        if (event instanceof CommentEvent) {
            /* 查看评论页面 或离 开评 论页面 */
            long id = ((CommentEvent) event).id;
            if (id >= 0){
                pushInCommentFragment(id);
            } else {
                popupCommentFragment();
            }
        } else if (event instanceof InitToolbarEvent) {
            initDrawerWithToolbar(((InitToolbarEvent) event).toolbar);
        } else if (event instanceof ViewImageEvent) {
            /* 查看图片请求 */
            String url = ((ViewImageEvent) event).url;
            Intent intent = new Intent(this, ImageViewerActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
//            Bundle args = new Bundle();
//            args.putString("url", url);
//            DialogFragment fragment = new ImageViewerFragment();
//            fragment.setArguments(args);
//            getSupportFragmentManager().beginTransaction()
//                .add(fragment, null)
//                .addToBackStack(null)
//                .commitAllowingStateLoss();
//            fragment.show(getSupportFragmentManager(), "tag");
            // http://stackoverflow.com/questions/12105064/actions-in-onactivityresult-and-error-can-not-perform-this-action-after-onsavei
        } else if (event instanceof NightModeEvent) {
            /* 更新Menu */
            isNowNightModeOn = ((NightModeEvent)event).nightModeOn;
            invalidateOptionsMenu();
            /* 更新Nav */
            setupNavigationView();
        } else if (event instanceof DrawerEvent) {
            switch (((DrawerEvent)event).messageType) {
                case DrawerEvent.CLOSE_DRAWER_AND_LOCK:
                    drawerLayout.closeDrawer(GravityCompat.END);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
                    break;
            }
        }
    }

    /**
     * 绑定离线下载服务 TODO:修改为懒绑定
     * */
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

    /**
     * 由于Bind是异步的 建议提前绑定
     * */
    public IOfflineDownloadInterface getOfflineBinder() {
        if (offlineBinder == null)
            Toast.makeText(this, "离线下载服务还木有准备完毕", Toast.LENGTH_SHORT).show();
        return offlineBinder;
    }


    /**
     * 解除监听Clipboard
     * */
    @Override
    protected void onPause() {
        super.onPause();
        ClipboardHelper.unregisterListener();
    }

    /**
     * 释放
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
