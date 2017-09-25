package com.xxshhh.android.android_demo.function.home.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.activity.BaseActivity;
import com.xxshhh.android.android_demo.function.home.utils.HomeNavIconUtils;
import com.xxshhh.android.android_demo.function.practice.PracticeFragment;

import butterknife.BindView;

/**
 * 首页界面
 * Created by xxshhh on 2017/9/14.
 */
public class HomeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fl_container)
    FrameLayout mFlContainer;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected int getLayoutResID() {
        return R.layout.home_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
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
        setupNavIcon();
        setupNavItemListener();
    }

    private void setupNavIcon() {
        Menu menu = mNavView.getMenu();
        menu.findItem(R.id.nav_mine).setIcon(HomeNavIconUtils.getSmileIcon(this));
        menu.findItem(R.id.nav_official_practice).setIcon(HomeNavIconUtils.getPhoneIcon(this));
        menu.findItem(R.id.nav_third_lib).setIcon(HomeNavIconUtils.getLightIcon(this));
        menu.findItem(R.id.nav_magic).setIcon(HomeNavIconUtils.getMagicIcon(this));
        menu.findItem(R.id.nav_setting).setIcon(HomeNavIconUtils.getSettingIcon(this));
    }

    private void setupNavItemListener() {
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_mine:
                        break;
                    case R.id.nav_official_practice:
                        switchPractice();
                        break;
                    case R.id.nav_third_lib:
                        break;
                    case R.id.nav_magic:
                        break;
                    case R.id.nav_setting:
                        break;
                    default:
                }

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(item.getTitle());
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
    }

    private void switchPractice() {
        PracticeFragment practiceFragment = new PracticeFragment();
        replaceFragment(practiceFragment);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
