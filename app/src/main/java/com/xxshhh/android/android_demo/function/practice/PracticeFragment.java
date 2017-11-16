package com.xxshhh.android.android_demo.function.practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.common.activity.CommonContainerActivity;
import com.xxshhh.android.android_demo.function.practice.animation.demo.AnimationDemoFragment;
import com.xxshhh.android.android_demo.function.practice.animation.frame.AnimationFrameFragment;
import com.xxshhh.android.android_demo.function.practice.animation.property.AnimationPropertyFragment;
import com.xxshhh.android.android_demo.function.practice.animation.tween.AnimationTweenFragment;

import butterknife.BindView;

/**
 * 官方实践界面
 * Created by xxshhh on 2017/9/19.
 */
public class PracticeFragment extends BaseFragment {

    @BindView(R.id.btn_animation_frame)
    Button mBtnAnimationFrame;
    @BindView(R.id.btn_animation_tween)
    Button mBtnAnimationTween;
    @BindView(R.id.btn_animation_property)
    Button mBtnAnimationProperty;
    @BindView(R.id.btn_animation_demo)
    Button mBtnAnimationDemo;

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
        mBtnAnimationFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonContainerActivity.start(getContext(), AnimationFrameFragment.class);
            }
        });

        mBtnAnimationTween.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonContainerActivity.start(getContext(), AnimationTweenFragment.class);
            }
        });

        mBtnAnimationProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonContainerActivity.start(getContext(), AnimationPropertyFragment.class);
            }
        });

        mBtnAnimationDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonContainerActivity.start(getContext(), AnimationDemoFragment.class);
            }
        });
    }

}
