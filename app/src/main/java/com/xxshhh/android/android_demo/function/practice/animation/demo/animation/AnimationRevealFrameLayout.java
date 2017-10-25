package com.xxshhh.android.android_demo.function.practice.animation.demo.animation;

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
 * 揭露动画容器
 * Created by xxshhh on 2017/10/23.
 */
public class AnimationRevealFrameLayout extends FrameLayout implements AnimationRevealInterface {

    private Path mPath; //路径
    private RectF mRectF; //矩形
    private float mRound; //矩形圆角

    private boolean mIsClip; //是否裁剪，即播放动画
    private Animator.AnimatorListener mAnimatorListener; //动画回调

    public AnimationRevealFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public AnimationRevealFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationRevealFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
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

        if (mIsClip) {
            mIsClip = false;
            mPath.reset();
            mPath.addRoundRect(mRectF, mRound, mRound, Path.Direction.CW);
            canvas.clipPath(mPath);
        }
    }

    @Override
    public void setRevealListener(Animator.AnimatorListener listener) {
        mAnimatorListener = listener;
    }

    @Override
    public void startRevealAnimation() {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth == 0 || measuredHeight == 0) {
            return;
        }
        final float centerX = measuredWidth / 2f;
        final float centerY = measuredHeight / 2f;
        final int minRadius = getResources().getDimensionPixelOffset(R.dimen.animation_demo_circle_size) / 2;
        final float maxRadiusX = centerX;
        final float maxRadiusY = centerY;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(8 * 40);
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

                mIsClip = true;
                invalidate();
            }
        });
        valueAnimator.addListener(mAnimatorListener);
        valueAnimator.start();
    }
}
