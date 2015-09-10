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
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.ui.fragment.jandan.JokeFragment;
import com.blazers.jandan.ui.fragment.jandan.MeiziFragment;
import com.blazers.jandan.ui.fragment.jandan.NewsFragment;
import com.blazers.jandan.ui.fragment.jandan.PicFragment;
import com.blazers.jandan.ui.fragment.huaban.PinFragment;
import com.blazers.jandan.util.Dppx;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {

    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.container) ViewPager viewPager;
    @Bind(R.id.left_nav) NavigationView navigationView;

    private ArrayList<Fragment> fragments;
    private ArrayList<Boolean> updates;
    private String[] titles = {"新鲜事" ,"段子", "妹子图"};

    private int nowSelectedNavId = R.id.nav_jandan;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDrawerWithToolbar();
        /* 根据需要填充主界面所加载的Fragment */
        fragments = new ArrayList<>();
        fragments.add(new NewsFragment());
//        fragments.add(new PicFragment());
        fragments.add(new JokeFragment());
        fragments.add(new MeiziFragment());
        viewPager.setAdapter(adapter = new FragmentAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        /* TODO: 根据屏幕尺寸设置TabMode */
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(Color.rgb(221, 221, 221), Color.WHITE);
        viewPager.setPageMargin(Dppx.Dp2Px(this, 12));
        /* 读取上次最后访问的 */
        navigationView.setCheckedItem(R.id.nav_jandan);
    }

    void initDrawerWithToolbar() {
        initToolbarByType(toolbar, ToolbarType.NORMAL);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);

        /* Navigation View */
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == nowSelectedNavId)
                return true;
            switch (menuItem.getItemId()) {
                case R.id.nav_jandan:

                    break;
                case R.id.nav_huaban:

                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        private FragmentManager fm;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
