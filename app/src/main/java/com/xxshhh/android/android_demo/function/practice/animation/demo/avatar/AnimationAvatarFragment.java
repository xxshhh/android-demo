package com.xxshhh.android.android_demo.function.practice.animation.demo.avatar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;

import butterknife.BindView;

/**
 * 头像动画
 * Created by xxshhh on 2017/11/28.
 */
public class AnimationAvatarFragment extends BaseFragment {

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.rl_root)
    RelativeLayout mRlRoot;

    @Override
    protected int getLayoutResID() {
        return R.layout.animation_fragment_avatar;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
    }

    private void play() {
        Animator appearAnimation = getAppearAnimation();
        Animator beatAnimation = getBeatAnimation();
        Animator moveAnimation = getMoveAnimation();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(appearAnimation, beatAnimation, moveAnimation);
        animatorSet.start();
    }

    private void zoomAnimation() {
        Rect startBounds = new Rect();
        Rect finalBounds = new Rect();

        mIvAvatar.getGlobalVisibleRect(startBounds);
        mRlRoot.getGlobalVisibleRect(finalBounds);

        float scale = finalBounds.width() / startBounds.width();

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, scale);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scale);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mIvAvatar,
                pvhScaleX, pvhScaleY);
        int duration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private Animator getAppearAnimation() {
        PropertyValuesHolder pvhTranslationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -mIvAvatar.getWidth(), 0);
        PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0, 1);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mIvAvatar,
                pvhTranslationY, pvhAlpha);
        int duration = getResources().getInteger(android.R.integer.config_longAnimTime);
        animator.setDuration(duration * 2);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }

    private Animator getBeatAnimation() {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, 1.015f),
                Keyframe.ofFloat(.2f, 1.03f),
                Keyframe.ofFloat(.3f, 1.03f),
                Keyframe.ofFloat(.4f, 1.015f),
                Keyframe.ofFloat(.5f, 1f),
                Keyframe.ofFloat(.6f, 0.985f),
                Keyframe.ofFloat(.7f, 0.97f),
                Keyframe.ofFloat(.8f, 0.97f),
                Keyframe.ofFloat(.9f, 0.985f),
                Keyframe.ofFloat(1f, 1f));
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, 0.94f),
                Keyframe.ofFloat(.2f, 0.88f),
                Keyframe.ofFloat(.3f, 0.88f),
                Keyframe.ofFloat(.4f, 0.94f),
                Keyframe.ofFloat(.5f, 1f),
                Keyframe.ofFloat(.6f, 1.06f),
                Keyframe.ofFloat(.7f, 1.12f),
                Keyframe.ofFloat(.8f, 1.12f),
                Keyframe.ofFloat(.9f, 1.06f),
                Keyframe.ofFloat(1f, 1f));

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mIvAvatar,
                pvhScaleX, pvhScaleY);
        animator.setRepeatCount(3);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        int duration = getResources().getInteger(android.R.integer.config_longAnimTime);
        animator.setDuration(duration * 3);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private Animator getMoveAnimation() {
        int[] avatarLoc = new int[2];
        mIvAvatar.getLocationOnScreen(avatarLoc);
        int[] rootLoc = new int[2];
        mRlRoot.getLocationOnScreen(rootLoc);

        float startX = 0;
        float startY = 0;
        float endX = mRlRoot.getWidth() / 2 - avatarLoc[0] - mIvAvatar.getWidth() / 2;
        float endY = mRlRoot.getHeight() / 2 - avatarLoc[1] - mIvAvatar.getHeight() / 2;
        float controlX = startX;
        float controlY = (endY - startY) * 2;

        // 二阶贝塞尔曲线
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(controlX, controlY, endX, endY);

        final PathMeasure pathMeasure = new PathMeasure(path, false);

        ValueAnimator animator = ValueAnimator.ofFloat(pathMeasure.getLength());
        final float[] currentPos = new float[2];
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                pathMeasure.getPosTan(value, currentPos, null);
                mIvAvatar.setTranslationX(currentPos[0]);
                mIvAvatar.setTranslationY(currentPos[1]);
            }
        });
        int duration = getResources().getInteger(android.R.integer.config_longAnimTime);
        animator.setDuration(duration * 2);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }
}
