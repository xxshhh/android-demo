package com.xxshhh.android.android_demo.function.practice.animation.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxshhh.android.android_demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Demo动画 viewHolder
 * Created by xxshhh on 2017/9/20.
 */
public class AnimationDemoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rl_root)
    RelativeLayout mRlRoot;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.cfl_container)
    AnimationDemoCustomFrameLayout mCflContainer;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;

    public AnimationDemoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(String msg) {
        mTvMsg.setText(msg);

        setupAnimation();
    }

    private void setupAnimation() {
        mRlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addAvatarAnimation();
                addTvMsgAnimation();
                mRlRoot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void addAvatarAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(8 * 40);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0);
        scaleAnimation.setDuration(8 * 40);

        ScaleAnimation bounceAnimation = new ScaleAnimation(0.85f, 1, 0.85f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        bounceAnimation.setInterpolator(new BounceInterpolator());
        bounceAnimation.setStartOffset(40);
        bounceAnimation.setDuration(18 * 40);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(bounceAnimation);
        mIvAvatar.startAnimation(animationSet);
    }

    private void addTvMsgAnimation() {
        // 1.1裁剪
        mCflContainer.setAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                // 1.2旋转
                addTvMsgRotateAnimation();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 2.抖动
                addTvMsgShakeAnimation();
            }
        });
        mCflContainer.startAnimation();
    }

    private void addTvMsgShakeAnimation() {
        long totalTime = 17 * 40;
        float k1 = 0f;
        float k2 = 3.0f / 17.0f;
        float k3 = 5.0f / 17.0f;
        float k4 = 1f;

        float sx1 = 1 + 0.1f;
        float sx2 = 1 - 0.07f;
        float sx3 = 1 + 0.04f;
        float sx4 = 1f;

        float sy1 = 1 - 0.08f;
        float sy2 = 1 + 0.03f;
        float sy3 = 1 - 0.01f;
        float sy4 = 1f;

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(k1, sx1),
                Keyframe.ofFloat(k2, sx2),
                Keyframe.ofFloat(k3, sx3),
                Keyframe.ofFloat(k4, sx4));

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(k1, sy1),
                Keyframe.ofFloat(k2, sy2),
                Keyframe.ofFloat(k3, sy3),
                Keyframe.ofFloat(k4, sy4));

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mTvMsg, pvhScaleX, pvhScaleY);
        objectAnimator.setDuration(totalTime);
        objectAnimator.start();
    }

    private void addTvMsgRotateAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(8 * 40);

        RotateAnimation rotateAnimation = new RotateAnimation(-5, 0);
        rotateAnimation.setDuration(8 * 40);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        mTvMsg.startAnimation(animationSet);
    }

}
