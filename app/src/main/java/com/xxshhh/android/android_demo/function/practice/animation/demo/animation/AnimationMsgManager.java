package com.xxshhh.android.android_demo.function.practice.animation.demo.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.xxshhh.android.android_demo.R;

/**
 * 消息动画管理类
 * Created by xxshhh on 2017/10/25.
 */
public class AnimationMsgManager {

    public static void startAnimation(Context context, final ViewGroup parentView,
                                      float startX, float startY, float toX, float toY,
                                      final View itemView, final AnimationRevealFrameLayout revealFrameLayout, final ImageView imageView) {
        // 播放发送动画
        // 创建一个小球并加到父容器中
        final ImageView circleView = new ImageView(context);
        circleView.setImageResource(R.drawable.animation_demo_circle);
        parentView.addView(circleView);

        // 设置小球运动轨迹
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(startX, (startY + toY) / 2, toX, toY);

        // 播放动画
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.setDuration(500);
        final float[] currentPos = new float[2];
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                pathMeasure.getPosTan(value, currentPos, null);
                circleView.setTranslationX(currentPos[0]);
                circleView.setTranslationY(currentPos[1]);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                parentView.removeView(circleView);
                // 播放消息动画
                startInsertAction(itemView, revealFrameLayout, imageView);
            }
        });
        valueAnimator.start();
    }

    private static void startInsertAction(View itemView, AnimationRevealFrameLayout revealFrameLayout, ImageView imageView) {
        itemView.setVisibility(View.VISIBLE);
        addTvMsgAnimation(revealFrameLayout);
        addAvatarAnimation(imageView);
    }

    private static void addTvMsgAnimation(final AnimationRevealFrameLayout revealFrameLayout) {
        // 1.1裁剪
        revealFrameLayout.setRevealListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                // 1.2旋转
                addTvMsgRotateAnimation(revealFrameLayout);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 2.抖动
                addTvMsgShakeAnimation(revealFrameLayout);
            }
        });
        revealFrameLayout.startRevealAnimation();
    }

    private static void addTvMsgRotateAnimation(AnimationRevealFrameLayout revealFrameLayout) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(8 * 40);

        RotateAnimation rotateAnimation = new RotateAnimation(-5, 0);
        rotateAnimation.setDuration(8 * 40);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        revealFrameLayout.startAnimation(animationSet);
    }

    private static void addTvMsgShakeAnimation(AnimationRevealFrameLayout revealFrameLayout) {
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

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(revealFrameLayout, pvhScaleX, pvhScaleY);
        objectAnimator.setDuration(totalTime);
        objectAnimator.start();
    }

    private static void addAvatarAnimation(ImageView imageView) {
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
        imageView.startAnimation(animationSet);
    }

}
