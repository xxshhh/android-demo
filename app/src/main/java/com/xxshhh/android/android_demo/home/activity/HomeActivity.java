package com.xxshhh.android.android_demo.home.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.home.utils.HomeNavIconUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页界面
 * Created by xwh on 2017/9/14
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        initToolbar();
        initDrawerLayout();
        initNavigationView();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        mNavView.setNavigationItemSelectedListener(this);
        Menu menu = mNavView.getMenu();
        menu.findItem(R.id.nav_mine).setIcon(HomeNavIconUtils.getSmileIcon(this));
        menu.findItem(R.id.nav_official_practice).setIcon(HomeNavIconUtils.getPhoneIcon(this));
        menu.findItem(R.id.nav_third_lib).setIcon(HomeNavIconUtils.getLightIcon(this));
        menu.findItem(R.id.nav_magic).setIcon(HomeNavIconUtils.getMagicIcon(this));
        menu.findItem(R.id.nav_setting).setIcon(HomeNavIconUtils.getSettingIcon(this));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mine) {
            // Handle the camera action
        } else if (id == R.id.nav_official_practice) {

        } else if (id == R.id.nav_third_lib) {

        } else if (id == R.id.nav_magic) {

        } else if (id == R.id.nav_setting) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
