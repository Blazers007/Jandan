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
import com.blazers.jandan.ui.fragment.SettingFragment;
import com.blazers.jandan.ui.fragment.jandan.*;


public class MainActivity extends BaseActivity {

    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.left_nav) NavigationView navigationView;

    private int nowSelectedNavId = R.id.nav_jandan;

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
                .add(R.id.fragment_wrapper, new JandanFragmentHolder(), JANDAN_TAG)
            .commit();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() != nowSelectedNavId){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.nav_jandan: // Bottom Fragment
                        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                            if (fragment != getSupportFragmentManager().findFragmentByTag(JANDAN_TAG))
                                transaction.remove(fragment);
                        }
                        nowSelectedNavId = R.id.nav_jandan;
                        break;
                    case R.id.nav_fav:
                        transaction.setCustomAnimations(R.anim.activity_slide_right_in, R.anim.activity_slide_right_out,
                            R.anim.activity_slide_right_in, R.anim.activity_slide_right_out);
                        transaction.add(R.id.fragment_wrapper, new FavoriteFragment(), FAV_TAG);
                        transaction.addToBackStack(null);
                        nowSelectedNavId = R.id.nav_fav;
                        break;
                    case R.id.nav_setting:
                        transaction.setCustomAnimations(R.anim.activity_slide_right_in, R.anim.activity_slide_right_out,
                            R.anim.activity_slide_right_in, R.anim.activity_slide_right_out);
                        transaction.add(R.id.fragment_wrapper, new SettingFragment(), FAV_TAG);
                        transaction.addToBackStack(null);
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
            .add(R.id.fragment_wrapper, JandanCommentFragment.NewInstance(id))
            .addToBackStack(null)
            .commit();
    }

    public void popupFragment() {
        getSupportFragmentManager().popBackStack();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
