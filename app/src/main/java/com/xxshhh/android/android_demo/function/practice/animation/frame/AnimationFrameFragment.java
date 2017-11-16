package com.xxshhh.android.android_demo.function.practice.animation.frame;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;

import butterknife.BindView;

/**
 * 逐帧动画界面
 * Created by xxshhh on 2017/10/10.
 */
public class AnimationFrameFragment extends BaseFragment {

    @BindView(R.id.iv_animation)
    ImageView mIvAnimation;
    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.btn_stop)
    Button mBtnStop;

    private AnimationDrawable mAnimationDrawable;

    @Override
    protected int getLayoutResID() {
        return R.layout.animation_fragment_frame;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        Drawable drawable = mIvAnimation.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            mAnimationDrawable = (AnimationDrawable) drawable;
        }

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnimationDrawable != null) {
                    mAnimationDrawable.start();
                }
            }
        });

        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnimationDrawable != null) {
                    mAnimationDrawable.stop();
                }
            }
        });
    }

}
