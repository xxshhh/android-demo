package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.view.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
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
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.view.msg.ImportantMsgView_Text;

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

    /**
     * 设置数据
     */
    public void setData(Object data) {
        // ...
        ImportantMsgView_Text textView = new ImportantMsgView_Text(getContext());
        textView.setData(data);
        mRlContainer.addView(textView);
    }

    /**
     * 获取Logo的中心位置
     */
    public int[] getLogoCenterLocation() {
        int[] logoLoc = new int[2];
        mIvLogo.getLocationOnScreen(logoLoc);
        logoLoc[0] += mIvLogo.getWidth() / 2;
        logoLoc[1] += mIvLogo.getHeight() / 2;
        return logoLoc;
    }

    /**
     * 获取Logo的宽度
     */
    public int getLogoWidth() {
        return mIvLogo.getWidth();
    }

    /**
     * 获取Logo的高度
     */
    public int getLogoHeight() {
        return mIvLogo.getHeight();
    }

    /**
     * 设置确定事件
     */
    public void setConfirmEvent(OnClickListener onClickListener) {
        mTvConfirm.setOnClickListener(onClickListener);
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
        Animator logoBezierCurveAndScaleAnimation = getLogoBezierCurveAndScaleAnimation(endLoc, startScale, endScale);
        Animator dialogAlphaAnimation = getDialogAlphaAnimation();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(logoBezierCurveAndScaleAnimation, dialogAlphaAnimation);
        return animatorSet;
    }

    private Animator getLogoScaleAnimation(float startScale, float endScale) {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, startScale, endScale);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, startScale, endScale);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mIvLogo,
                pvhScaleX, pvhScaleY);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
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
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
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
        startLoc[0] += mIvLogo.getWidth() / 2;
        startLoc[1] += mIvLogo.getHeight() / 2;
        float startX = 0;
        float startY = 0;
        float endX = endLoc[0] - startLoc[0];
        float endY = endLoc[1] - startLoc[1];
        // 根据起点和终点的上下位置来决定控制点的位置
        float controlX;
        float controlY;
        if (startY > endY) {
            controlX = startX;
            controlY = endY;
        } else {
            controlX = endX;
            controlY = startY;
        }

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
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIvLogo.setVisibility(INVISIBLE);
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

    private Animator getDialogAlphaAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLlDialog, View.ALPHA, 1, 0);
        animator.setDuration(100);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }
}
