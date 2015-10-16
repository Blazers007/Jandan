package com.blazers.jandan.ui.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.ui.fragment.FavoriteFragment;
import com.blazers.jandan.ui.fragment.CommentFragment;
import com.blazers.jandan.ui.fragment.ReadingFragment;
import com.blazers.jandan.ui.fragment.SettingFragment;


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
        ButterKnife.bind(this);
//        initDrawerWithToolbar();
        /* 根据需要填充主界面所加载的Fragment */
        getSupportFragmentManager()
            .beginTransaction()
                .add(R.id.fragment_wrapper, defaultFragment = ReadingFragment.getInstance(), JANDAN_TAG)
            .commit();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() != nowSelectedNavId){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.nav_jandan:
                        for (Fragment fragment : getSupportFragmentManager().getFragments())
                            transaction.hide(fragment);
                        transaction.show(defaultFragment);
                        nowSelectedNavId = R.id.nav_jandan;
                        break;
                    case R.id.nav_fav:
                        if (getSupportFragmentManager().findFragmentByTag(FAV_TAG) == null) {
                            transaction.add(R.id.fragment_wrapper, FavoriteFragment.getInstance(), FAV_TAG);
                        } else {
                            for (Fragment fragment : getSupportFragmentManager().getFragments())
                                transaction.hide(fragment);
                            transaction.show(FavoriteFragment.getInstance());
                        }
                        nowSelectedNavId = R.id.nav_fav;
                        break;
                    case R.id.nav_setting:
                        if (getSupportFragmentManager().findFragmentByTag(SETTING_TAG) == null) {
                            transaction.add(R.id.fragment_wrapper, SettingFragment.getInstance(), SETTING_TAG);
                        } else {
                            for (Fragment fragment : getSupportFragmentManager().getFragments())
                                transaction.hide(fragment);
                            transaction.show(SettingFragment.getInstance());
                        }
                        nowSelectedNavId = R.id.nav_setting;
                        break;
                }
                transaction.commit();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    public void initDrawerWithToolbar(Toolbar toolbar) {
        initToolbarByTypeWithShadow(null, toolbar, ToolbarType.NORMAL);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
        /* Navigation View */
    }


    public void pushInCommentFragment(long id) {
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.activity_slide_right_in, R.anim.activity_slide_right_out, R.anim.activity_slide_right_in, R.anim.activity_slide_right_out)
            .add(R.id.fragment_wrapper, CommentFragment.NewInstance(id))
            .addToBackStack(null)
            .commit();
    }

    public void popupCommentFragment() {
        getSupportFragmentManager().popBackStack();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
