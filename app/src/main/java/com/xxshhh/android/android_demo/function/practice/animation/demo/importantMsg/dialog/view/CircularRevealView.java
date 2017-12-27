package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.dialog.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆形揭露View
 * Created by xwh on 2017/12/21.
 */
public class CircularRevealView extends View {

    private Paint mPaint;
    private float mCenterX;
    private float mCenterY;
    private float mCurrentRadius;

    public CircularRevealView(Context context) {
        this(context, null);
    }

    public CircularRevealView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularRevealView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.argb(153, 0, 0, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mCenterX, mCenterY, mCurrentRadius, mPaint);
    }

    /**
     * 获取圆形揭露动画
     */
    public Animator getCircularRevealAnimation(float centerX, float centerY, float startRadius, float endRadius, long duration) {
        mCenterX = centerX;
        mCenterY = centerY;

        ValueAnimator animator = ValueAnimator.ofFloat(startRadius, endRadius);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentRadius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        return animator;
    }
}
