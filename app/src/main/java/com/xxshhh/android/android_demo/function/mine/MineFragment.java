package com.xxshhh.android.android_demo.function.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.common.activity.CommonContainerActivity;
import com.xxshhh.android.android_demo.function.mine.test.TestClickFragment;

import butterknife.BindView;

/**
 * 我的界面
 * Created by xxshhh on 2017/11/15.
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.btn_test_click)
    Button mBtnTestClick;

    @Override
    protected int getLayoutResID() {
        return R.layout.mine_fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mBtnTestClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonContainerActivity.start(getContext(), TestClickFragment.class);
            }
        });
    }
}
