package com.blazers.jandan.ui.activity;

import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.ui.fragment.jandan.*;
import com.blazers.jandan.util.Dppx;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {


    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.left_nav) NavigationView navigationView;

    private int nowSelectedNavId = R.id.nav_jandan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        initDrawerWithToolbar();
        /* 根据需要填充主界面所加载的Fragment */
        getSupportFragmentManager()
            .beginTransaction()
                .add(R.id.fragment_wrapper, new JandanFragmentHolder())
            .commit();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == nowSelectedNavId)
                return true;
            switch (menuItem.getItemId()) {
                case R.id.nav_jandan:

                    break;
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
