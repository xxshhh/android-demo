package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.avatar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xxshhh.android.android_demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 重要消息头像View
 * Created by xwh on 2017/12/22.
 */
public class ImportantMsgAvatarView extends RelativeLayout {

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;

    public ImportantMsgAvatarView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.animation_view_important_msg_avatar, this, true);
        ButterKnife.bind(this);
    }

    /**
     * 展示用户头像
     */
    public void displayAvatar(String uri) {

    }

    /**
     * 设置点击事件
     */
    public void setClickEvent(OnClickListener onClickListener) {
        setOnClickListener(onClickListener);
    }

    /**
     * 获取展示动画
     */
    public Animator getShowAnimation() {
        Animator appearAnimation = getAppearAnimation();
        Animator beatAnimation = getBeatAnimation();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(appearAnimation, beatAnimation);
        return animatorSet;
    }

    /**
     * 获取变换动画
     */
    public Animator getTransformAnimation(int[] endLoc) {
        return getBezierCurveAnimation(endLoc);
    }

    private Animator getAppearAnimation() {
        PropertyValuesHolder pvhTranslationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -getHeight(), 0);
        PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0, 1);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this,
                pvhTranslationY, pvhAlpha);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }

    private Animator getBeatAnimation() {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.5f, 1.05f),
                Keyframe.ofFloat(1f, 1f));
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.5f, 0.92f),
                Keyframe.ofFloat(1f, 1f));

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this,
                pvhScaleX, pvhScaleY);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setPivotY(getHeight());
            }
        });
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        return animator;
    }

    private Animator getBezierCurveAnimation(int[] endLoc) {
        // 计算起点、终点、控制点
        int[] startLoc = new int[2];
        getLocationOnScreen(startLoc);
        startLoc[0] += getWidth() / 2;
        startLoc[1] += getHeight() / 2;
        float startX = 0;
        float startY = 0;
        float endX = endLoc[0] - startLoc[0];
        float endY = endLoc[1] - startLoc[1];
        float controlX = startX;
        float controlY = 2 * endY - startY;

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
                setTranslationX(currentPos[0]);
                setTranslationY(currentPos[1]);
            }
        });
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }
}
