package com.blazers.jandan.ui.activity;

import android.app.FragmentTransaction;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.network.HttpParser;
import com.blazers.jandan.orm.HuabanPin;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.ui.fragment.JokeFragment;
import com.blazers.jandan.ui.fragment.MeiziFragment;
import com.blazers.jandan.ui.fragment.NewsFragment;
import com.blazers.jandan.ui.fragment.PicFragment;
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
    private String[] titles = {"新鲜事", "无聊图" ,"段子", "妹子图"};

    private int nowSelectedNavId = R.id.nav_jandan;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDrawerWithToolbar();
        /* 根据需要填充主界面所加载的Fragment */
        updates = new ArrayList<>();
        updates.add(false);
        updates.add(false);
        updates.add(false);
        updates.add(false);
        fragments = new ArrayList<>();
        fragments.add(new NewsFragment());
        fragments.add(new PicFragment());
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
                    updates = new ArrayList<Boolean>();
                    for (int i = 0 ; i < titles.length ;i ++) {
                        updates.add(true);
                    }
                    titles = new String[]{"新鲜事", "无聊图" ,"段子", "妹子图"};
                    fragments = new ArrayList<>();
                    fragments.add(new NewsFragment());
                    fragments.add(new PicFragment());
                    fragments.add(new JokeFragment());
                    fragments.add(new MeiziFragment());
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.nav_huaban:
                    updates = new ArrayList<Boolean>();
                    for (int i = 0 ; i < titles.length ;i ++) {
                        updates.add(true);
                    }
                    titles = new String[]{"妹子图"};
                    fragments = new ArrayList<>();
                    fragments.add(new PinFragment());
                    adapter.notifyDataSetChanged();
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

        /* TODO: WHY? */
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            String tag = fragment.getTag();
            if (updates.get(position)) {
                android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragment = fragments.get(position);
                fragmentTransaction.add(container.getId(), fragment, tag);
                fragmentTransaction.attach(fragment);
                fragmentTransaction.commit();
                updates.set(position, true);
            }
            return fragment;
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
