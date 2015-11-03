package com.blazers.jandan.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
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
    private boolean isNowNightModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 读取模式 */
        isNowNightModeOn = SPHelper.getBooleanSP(this, SPHelper.NIGHT_MODE_ON,false);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        /* 友盟统计 */
        MobclickAgent.openActivityDurationTrack(false);
        /* RxBus */
        registerEventReceiver();
        /* 绑定离线下载服务 */
        startService(new Intent(this, OfflineDownloadService.class));
        bindService(new Intent(this, OfflineDownloadService.class), serviceConnection, BIND_AUTO_CREATE);
        /* 根据需要填充主界面所加载的Fragment */
        initFragments();
        /* 初始化离线下载 */
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
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
        /* 设置监听 */
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() != nowSelectedNavId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                BaseFragment choosen = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_jandan:
                        for (Fragment fragment : getSupportFragmentManager().getFragments()){
                            if (fragment != null)
                                transaction.hide(fragment);
                        }
                        transaction.show(defaultFragment);
                        nowSelectedNavId = R.id.nav_jandan;
                        choosen = defaultFragment;
                        break;
                    case R.id.nav_fav:
                        if (getSupportFragmentManager().findFragmentByTag(FAV_TAG) == null) {
                            transaction.add(R.id.fragment_wrapper, FavoriteFragment.getInstance(), FAV_TAG);
                        } else {
                            for (Fragment fragment : getSupportFragmentManager().getFragments()){
                                if (fragment != null)
                                    transaction.hide(fragment);
                            }
                            transaction.show(choosen = FavoriteFragment.getInstance());
                        }
                        nowSelectedNavId = R.id.nav_fav;
                        break;
                    case R.id.nav_setting:
                        if (getSupportFragmentManager().findFragmentByTag(SETTING_TAG) == null) {
                            transaction.add(R.id.fragment_wrapper, SettingFragment.getInstance(), SETTING_TAG);
                        } else {
                            for (Fragment fragment : getSupportFragmentManager().getFragments()){
                                if (fragment != null && fragment != SettingFragment.getInstance())
                                    transaction.hide(fragment);
                            }
                            /*TODO
                            * http://stackoverflow.com/questions/22489703/trying-to-remove-fragment-from-view-gives-me-nullpointerexception-on-mnextanim
                            * */
                            transaction.show(choosen = SettingFragment.getInstance());
                        }
                        nowSelectedNavId = R.id.nav_setting;
                        break;
                }
                if (choosen != null)
                    choosen.reboundToolbar();
                transaction.commitAllowingStateLoss();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    /**
     * 将呈现的Fragment的Toolbar绑定到Drawer上去
     * */
    void initDrawerWithToolbar(Toolbar toolbar) {
        initToolbarByTypeWithShadow(null, toolbar, ToolbarType.NORMAL);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
        /* Navigation View */
    }


    /**
     * 滑入评论Fragment
     * */
    void pushInCommentFragment(long id) {
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.activity_slide_right_in, R.anim.activity_slide_right_out, R.anim.activity_slide_right_in, R.anim.activity_slide_right_out)
            .add(R.id.fragment_wrapper, CommentFragment.NewInstance(id))
            .addToBackStack(null)
            .commitAllowingStateLoss();
    }

    /**
     * 滑出Fragment
     * */
    void popupCommentFragment() {
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
            /* 重新绑定Toolbar */
            initDrawerWithToolbar(((InitToolbarEvent) event).toolbar);
            invalidateOptionsMenu();
        } else if (event instanceof ViewImageEvent) {
            /* 查看图片请求 */
            String url = ((ViewImageEvent) event).url;
            Bundle args = new Bundle();
            args.putString("url", url);
            DialogFragment fragment = new ImageViewerFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(fragment, null);
            fragment.setArguments(args);
//            fragment.show(getSupportFragmentManager(), "tag");
            // http://stackoverflow.com/questions/12105064/actions-in-onactivityresult-and-error-can-not-perform-this-action-after-onsavei
            ft.commitAllowingStateLoss();
        } else if (event instanceof NightModeEvent) {
            boolean isNightModeOn = ((NightModeEvent) event).nightModeOn;
            if (isNightModeOn || !isNowNightModeOn) {
                // 动态开启夜间模式
                
            } else if (isNowNightModeOn) {

            }
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
     * Umeng统计
     * */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
