package com.xxshhh.android.android_demo.function.practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.common.activity.CommonContainerActivity;
import com.xxshhh.android.android_demo.function.practice.animation.fragment.AnimationTweenFragment;

import butterknife.BindView;

/**
 * 官方实践界面
 * Created by xwh on 2017/9/19
 */
public class PracticeFragment extends BaseFragment {

    @BindView(R.id.btn_animation)
    Button mBtnAnimation;

    @Override
    protected int getLayoutResID() {
        return R.layout.practice_fragment;
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
        mBtnAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToAnimation();
            }
        });
    }

    private void jumpToAnimation() {
        CommonContainerActivity.start(getContext(), AnimationTweenFragment.class);
    }

}
