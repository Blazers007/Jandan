package com.blazers.jandan.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.IOfflineDownloadInterface;
import com.blazers.jandan.R;
import com.blazers.jandan.services.OfflineDownloadService;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.ui.fragment.FavoriteFragment;
import com.blazers.jandan.ui.fragment.CommentFragment;
import com.blazers.jandan.ui.fragment.ReadingFragment;
import com.blazers.jandan.ui.fragment.SettingFragment;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.umeng.analytics.MobclickAgent;


public class MainActivity extends BaseActivity {

    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.left_nav) NavigationView navigationView;

    private int nowSelectedNavId = R.id.nav_jandan;
    private ReadingFragment defaultFragment;
    public static final String JANDAN_TAG = "fragment_jandan";
    public static final String FAV_TAG = "fragment_fav";
    public static final String SETTING_TAG = "fragment_setting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobclickAgent.openActivityDurationTrack(false);
        ButterKnife.bind(this);
        /* 绑定离线下载服务 */
        startService(new Intent(this, OfflineDownloadService.class));
        bindService(new Intent(this, OfflineDownloadService.class), serviceConnection, BIND_AUTO_CREATE);
        /* 根据需要填充主界面所加载的Fragment */
        getSupportFragmentManager()
            .beginTransaction()
                .add(R.id.fragment_wrapper, defaultFragment = ReadingFragment.getInstance(), JANDAN_TAG)
            .commit();
        navigationView.setCheckedItem(R.id.nav_jandan);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() != nowSelectedNavId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                BaseFragment choosen = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_jandan:
                        for (Fragment fragment : getSupportFragmentManager().getFragments())
                            transaction.hide(fragment);
                        transaction.show(defaultFragment);
                        nowSelectedNavId = R.id.nav_jandan;
                        choosen = defaultFragment;
                        break;
                    case R.id.nav_fav:
                        if (getSupportFragmentManager().findFragmentByTag(FAV_TAG) == null) {
                            transaction.add(R.id.fragment_wrapper, FavoriteFragment.getInstance(), FAV_TAG);
                        } else {
                            for (Fragment fragment : getSupportFragmentManager().getFragments())
                                transaction.hide(fragment);
                            transaction.show(choosen = FavoriteFragment.getInstance());
                        }
                        nowSelectedNavId = R.id.nav_fav;
                        break;
                    case R.id.nav_setting:
                        if (getSupportFragmentManager().findFragmentByTag(SETTING_TAG) == null) {
                            transaction.add(R.id.fragment_wrapper, SettingFragment.getInstance(), SETTING_TAG);
                        } else {
                            for (Fragment fragment : getSupportFragmentManager().getFragments())
                                transaction.hide(fragment);
                            transaction.show(choosen = SettingFragment.getInstance());
                        }
                        nowSelectedNavId = R.id.nav_setting;
                        break;
                }
                if (choosen != null)
                    choosen.reboundToolbar();
                transaction.commit();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    /**
     * Umeng统计
     * */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    /**
     * 将呈现的Fragment的Toolbar绑定到Drawer上去
     * */
    public void initDrawerWithToolbar(Toolbar toolbar) {
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
    public void pushInCommentFragment(long id) {
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.activity_slide_right_in, R.anim.activity_slide_right_out, R.anim.activity_slide_right_in, R.anim.activity_slide_right_out)
            .add(R.id.fragment_wrapper, CommentFragment.NewInstance(id))
            .addToBackStack(null)
            .commit();
    }

    /**
     * 滑出Fragment
     * */
    public void popupCommentFragment() {
        getSupportFragmentManager().popBackStack();
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
