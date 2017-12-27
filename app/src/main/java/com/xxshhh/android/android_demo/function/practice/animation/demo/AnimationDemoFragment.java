package com.xxshhh.android.android_demo.function.practice.animation.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.common.activity.CommonContainerActivity;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.AnimationImportantMsgFragment;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.AnimationMsgFragment;

import butterknife.BindView;

/**
 * Demo动画界面
 * Created by xxshhh on 2017/9/19.
 */
public class AnimationDemoFragment extends BaseFragment {

    @BindView(R.id.btn_msg_demo)
    Button mBtnMsgDemo;
    @BindView(R.id.btn_important_msg_demo)
    Button mBtnImportantMsgDemo;

    @Override
    protected int getLayoutResID() {
        return R.layout.animation_fragment_demo;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        initAnimation();
    }

    private void initAnimation() {
        mBtnMsgDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonContainerActivity.start(getContext(), AnimationMsgFragment.class);
            }
        });

        mBtnImportantMsgDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonContainerActivity.start(getContext(), AnimationImportantMsgFragment.class);
            }
        });
    }
}
