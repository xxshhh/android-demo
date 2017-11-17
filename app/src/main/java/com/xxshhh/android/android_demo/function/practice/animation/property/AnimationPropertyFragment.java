package com.xxshhh.android.android_demo.function.practice.animation.property;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.common.utils.ToastUtils;

import butterknife.BindView;

/**
 * 属性动画界面
 * Created by xxshhh on 2017/9/22.
 */
public class AnimationPropertyFragment extends BaseFragment
        implements View.OnClickListener {

    @BindView(R.id.btn_alpha)
    Button mBtnAlpha;
    @BindView(R.id.btn_rotate)
    Button mBtnRotate;
    @BindView(R.id.btn_scale)
    Button mBtnScale;
    @BindView(R.id.btn_translate)
    Button mBtnTranslate;
    @BindView(R.id.btn_set)
    Button mBtnSet;
    @BindView(R.id.btn_argb)
    Button mBtnArgb;
    @BindView(R.id.btn_keyframe)
    Button mBtnKeyFrame;
    @BindView(R.id.btn_bezier)
    Button mBtnCustom;
    @BindView(R.id.gl_bottom)
    GridLayout mGlBottom;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;

    private Animator mAnimator;
    private IconicsDrawable mIconicsDrawable;

    @Override
    protected int getLayoutResID() {
        return R.layout.animation_fragment_property;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        initAvatar();
        initButton();
    }

    private void initAvatar() {
        mIconicsDrawable = new IconicsDrawable(getContext())
                .icon(CommunityMaterial.Icon.cmd_face)
                .colorRes(R.color.colorPrimary)
                .sizeDp(80);
        mIvAvatar.setImageDrawable(mIconicsDrawable);
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(getContext(), "click me");
            }
        });
    }

    private void initButton() {
        mBtnAlpha.setOnClickListener(this);
        mBtnRotate.setOnClickListener(this);
        mBtnScale.setOnClickListener(this);
        mBtnTranslate.setOnClickListener(this);
        mBtnSet.setOnClickListener(this);
        mBtnArgb.setOnClickListener(this);
        mBtnKeyFrame.setOnClickListener(this);
        mBtnCustom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mAnimator != null) {
            mAnimator.end();
            mAnimator = null;
        }
        switch (v.getId()) {
            case R.id.btn_alpha:
                mAnimator = getAlphaAnimator();
                break;
            case R.id.btn_rotate:
                mAnimator = getRotateAnimator();
                break;
            case R.id.btn_scale:
                mAnimator = getScaleAnimator();
                break;
            case R.id.btn_translate:
                mAnimator = getTranslateAnimator();
                break;
            case R.id.btn_set:
                mAnimator = getSetAnimator();
                break;
            case R.id.btn_argb:
                mAnimator = getColorAnimator();
                break;
            case R.id.btn_keyframe:
                mAnimator = getKeyFrameAnimator();
                break;
            case R.id.btn_bezier:
                mAnimator = getBezierCurveAnimator();
            default:
        }
        if (mAnimator != null) {
            mAnimator.start();
        }
    }

    private Animator getAlphaAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mIvAvatar, "alpha", 0, 1);
        animator.setDuration(2000);
        return animator;
    }

    private Animator getRotateAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mIvAvatar, "rotation", 0, 540);
        animator.setDuration(2000);
        return animator;
    }

    private Animator getScaleAnimator() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mIvAvatar, "scaleX", 0, 2);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mIvAvatar, "scaleY", 0, 2);

        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(2000);
        animator.play(animatorX).with(animatorY);
        return animator;
    }

    private Animator getTranslateAnimator() {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX",
                0, mIvAvatar.getWidth() * 2);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY",
                0, mIvAvatar.getHeight() * 2);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mIvAvatar,
                pvhX, pvhY);
        animator.setDuration(2000);
        return animator;
    }

    private Animator getSetAnimator() {
        Animator alphaAnimator = getAlphaAnimator();
        Animator rotateAnimator = getRotateAnimator();
        Animator scaleAnimator = getScaleAnimator();
        Animator translationAnimator = getTranslateAnimator();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alphaAnimator, rotateAnimator,
                scaleAnimator, translationAnimator);
        return animatorSet;
    }

    private Animator getColorAnimator() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int colorStart = ContextCompat.getColor(getContext(), R.color.colorPrimary);
            int colorEnd = ContextCompat.getColor(getContext(), R.color.colorAccent);
            ValueAnimator animator = ValueAnimator.ofArgb(colorStart, colorEnd);
            animator.setDuration(2000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int color = (int) animation.getAnimatedValue();
                    mIconicsDrawable.color(color);
                }
            });
            return animator;
        }
        return null;
    }

    private Animator getKeyFrameAnimator() {
        long totalTime = 1000;
        float k1 = 0;
        float k2 = 0.25f;
        float k3 = 0.5f;
        float k4 = 0.75f;
        float k5 = 1;

        float sx1 = 1 + 0.25f;
        float sx2 = 1 - 0.2f;
        float sx3 = 1 + 0.15f;
        float sx4 = 1 - 0.1f;
        float sx5 = 1;

        float sy1 = 1 - 0.2f;
        float sy2 = 1 + 0.15f;
        float sy3 = 1 - 0.1f;
        float sy4 = 1 + 0.05f;
        float sy5 = 1;

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(k1, sx1),
                Keyframe.ofFloat(k2, sx2),
                Keyframe.ofFloat(k3, sx3),
                Keyframe.ofFloat(k4, sx4),
                Keyframe.ofFloat(k5, sx5));

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(k1, sy1),
                Keyframe.ofFloat(k2, sy2),
                Keyframe.ofFloat(k3, sy3),
                Keyframe.ofFloat(k4, sy4),
                Keyframe.ofFloat(k5, sy5));

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mIvAvatar,
                pvhScaleX, pvhScaleY);
        animator.setDuration(totalTime);
        return animator;
    }

    private Animator getBezierCurveAnimator() {
        // 运动轨迹
        Path path = new Path();
        path.moveTo(0, 0);
        path.cubicTo(0, 500, 1000, 0, 500, 1000);
        // 创建动画
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        ValueAnimator animator = ValueAnimator.ofFloat(pathMeasure.getLength());
        animator.setInterpolator(new CustomInterpolator());
        animator.setDuration(2000);
        // 监听变化
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
        return animator;
    }

    class CustomInterpolator implements TimeInterpolator {

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.pow((4 * input - 2), 3) / 16f + 0.5f); // 两头变化快，中间变化慢
        }
    }

}
