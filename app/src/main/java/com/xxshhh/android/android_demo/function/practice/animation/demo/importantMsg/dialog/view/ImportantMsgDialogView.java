package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.dialog.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxshhh.android.android_demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 重要消息弹窗View
 * Created by xwh on 2017/12/22.
 */
public class ImportantMsgDialogView extends RelativeLayout {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl_container)
    RelativeLayout mRlContainer;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    @BindView(R.id.ll_dialog)
    LinearLayout mLlDialog;
    @BindView(R.id.iv_logo)
    ImageView mIvLogo;

    public ImportantMsgDialogView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.animation_view_important_msg_dialog, this, true);
        ButterKnife.bind(this);
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public RelativeLayout getRlContainer() {
        return mRlContainer;
    }

    public TextView getTvConfirm() {
        return mTvConfirm;
    }

    public LinearLayout getLlDialog() {
        return mLlDialog;
    }

    public ImageView getIvLogo() {
        return mIvLogo;
    }

    /**
     * 获取过渡动画
     */
    public Animator getTransitionAnimation(float startScale, float endScale) {
        return getLogoScaleAnimation(startScale, endScale);
    }

    /**
     * 获取展示动画
     */
    public Animator getShowAnimation() {
        return getDialogScaleAnimation();
    }

    /**
     * 获取变换动画
     */
    public Animator getTransformAnimation(int[] endLoc, float startScale, float endScale) {
        return getLogoBezierCurveAndScaleAnimation(endLoc, startScale, endScale);
    }

    private Animator getLogoScaleAnimation(float startScale, float endScale) {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, startScale, endScale);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, startScale, endScale);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mIvLogo,
                pvhScaleX, pvhScaleY);
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mIvLogo.setVisibility(VISIBLE);
                mLlDialog.setVisibility(INVISIBLE);
            }
        });
        return animator;
    }

    private Animator getDialogScaleAnimation() {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.4f, 1);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.4f, 1);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mLlDialog,
                pvhScaleX, pvhScaleY);
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setPivotY(0);
                // 隐藏内容
                mTvTitle.setVisibility(INVISIBLE);
                mRlContainer.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 显示内容
                mTvTitle.setVisibility(VISIBLE);
                mRlContainer.setVisibility(VISIBLE);
            }
        });
        return animator;
    }

    private Animator getLogoBezierCurveAndScaleAnimation(int[] endLoc, final float startScale, final float endScale) {
        // 计算起点、终点、控制点
        int[] startLoc = new int[2];
        mIvLogo.getLocationOnScreen(startLoc);
        float startX = 0;
        float startY = 0;
        float endX = endLoc[0] - startLoc[0];
        float endY = endLoc[1] - startLoc[1];
        float controlX = startX;
        float controlY = endY;

        // 二阶贝塞尔曲线
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(controlX, controlY, endX, endY);
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        ValueAnimator animator = ValueAnimator.ofFloat(pathMeasure.getLength());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mIvLogo.setVisibility(VISIBLE);
                mLlDialog.setVisibility(INVISIBLE);
            }
        });
        final float[] currentPos = new float[2];
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                pathMeasure.getPosTan(value, currentPos, null);
                setTranslationX(currentPos[0]);
                setTranslationY(currentPos[1]);

                float fraction = animation.getAnimatedFraction();
                float currentScale = (endScale - startScale) * fraction + startScale;
                mIvLogo.setScaleX(currentScale);
                mIvLogo.setScaleY(currentScale);
            }
        });
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }
}
