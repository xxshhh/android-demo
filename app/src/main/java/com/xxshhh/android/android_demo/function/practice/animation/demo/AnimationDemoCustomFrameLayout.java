package com.xxshhh.android.android_demo.function.practice.animation.demo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.xxshhh.android.android_demo.R;

/**
 * Demo动画自定义容器
 * Created by xxshhh on 2017/10/23.
 */
public class AnimationDemoCustomFrameLayout extends FrameLayout {

    private Path mPath; //路径
    private RectF mRectF; //矩形
    private float mRound; //矩形圆角

    private boolean mStartAnimation; //是否播放动画
    private Animator.AnimatorListener mAnimatorListener; //动画回调

    public AnimationDemoCustomFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public AnimationDemoCustomFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationDemoCustomFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false); //加上这句ViewGroup才会调用onDraw
        init();
    }

    private void init() {
        mPath = new Path();
        mRectF = new RectF(0, 0, 0, 0);
        mRound = getResources().getDimensionPixelOffset(R.dimen.animation_demo_rect_round_size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStartAnimation) {
            mPath.reset();
            mPath.addRoundRect(mRectF, mRound, mRound, Path.Direction.CW);
            canvas.clipPath(mPath);
        }
    }

    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        mAnimatorListener = animatorListener;
    }

    public void startAnimation() {
        mStartAnimation = true;

        final float centerX = getMeasuredWidth() / 2.0f;
        final float centerY = getMeasuredHeight() / 2.0f;
        final int minRadius = getResources().getDimensionPixelOffset(R.dimen.animation_demo_circle_size) / 2;
        final float maxRadiusX = centerX;
        final float maxRadiusY = centerY;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(8*40);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                float marginX = minRadius + (maxRadiusX - minRadius) * value;
                float marginY = minRadius + (maxRadiusY - minRadius) * value;

                float left = centerX - marginX;
                float top = centerY - marginY;
                float right = centerX + marginX;
                float bottom = centerY + marginY;
                mRectF = new RectF(left, top, right, bottom);

                invalidate();
            }
        });
        valueAnimator.addListener(mAnimatorListener);
        valueAnimator.start();
    }
}
