package com.xxshhh.android.android_demo.function.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.activity.BaseActivity;

import butterknife.BindView;

/**
 * 通用容器界面
 * Created by xwh on 2017/9/19
 */
public class CommonContainerActivity extends BaseActivity {

    @BindView(R.id.fl_container)
    FrameLayout mFlContainer;

    private static final String KEY_FRAGMENT_NAME = "fragment_name";
    private static final String KEY_FRAGMENT_BUNDLE = "fragment_bundle";

    /**
     * 跳转到通用容器界面
     *
     * @param context       上下文
     * @param fragmentClazz 目标fragment类
     */
    public static void start(Context context, Class<? extends Fragment> fragmentClazz) {
        start(context, fragmentClazz, null);
    }

    /**
     * 跳转到通用容器界面
     *
     * @param context        上下文
     * @param fragmentClazz  目标fragment类
     * @param fragmentBundle 目标fragment数据
     */
    public static void start(Context context, Class<? extends Fragment> fragmentClazz, Bundle fragmentBundle) {
        Intent intent = new Intent(context, CommonContainerActivity.class);
        intent.putExtra(KEY_FRAGMENT_NAME, fragmentClazz.getName());
        intent.putExtra(KEY_FRAGMENT_BUNDLE, fragmentBundle);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.common_activity_container;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initContainer();
    }

    private void initContainer() {
        String className = getIntent().getStringExtra(KEY_FRAGMENT_NAME);
        Bundle bundle = getIntent().getBundleExtra(KEY_FRAGMENT_BUNDLE);
        try {
            Fragment fragment = (Fragment) Class.forName(className).newInstance();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
