package com.xxshhh.android.android_demo.function.practice.animation.property;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @BindView(R.id.btn_1)
    Button mBtn1;
    @BindView(R.id.btn_2)
    Button mBtn2;
    @BindView(R.id.btn_3)
    Button mBtn3;
    @BindView(R.id.btn_4)
    Button mBtn4;
    @BindView(R.id.btn_5)
    Button mBtn5;
    @BindView(R.id.btn_6)
    Button mBtn6;
    @BindView(R.id.btn_7)
    Button mBtn7;
    @BindView(R.id.btn_8)
    Button mBtn8;
    @BindView(R.id.gl_bottom)
    GridLayout mGlBottom;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    private Animator mAnimator;

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
        Drawable drawable = new IconicsDrawable(getContext())
                .icon(CommunityMaterial.Icon.cmd_face)
                .colorRes(R.color.colorPrimary)
                .sizeDp(80);
        mIvAvatar.setImageDrawable(drawable);
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(getContext(), "click me");
            }
        });
    }

    private void initButton() {
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mBtn5.setOnClickListener(this);
        mBtn6.setOnClickListener(this);
        mBtn7.setOnClickListener(this);
        mBtn8.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mAnimator != null) {
            mAnimator.end();
            mAnimator = null;
        }
        switch (v.getId()) {
            case R.id.btn_1:
                mAnimator = getAlphaAnimator();
                break;
            case R.id.btn_2:
                mAnimator = getRotateAnimator();
                break;
            case R.id.btn_3:
                mAnimator = getScaleAnimator();
                break;
            case R.id.btn_4:
                mAnimator = getTranslationAnimator();
                break;
            case R.id.btn_5:
                mAnimator = getSetAnimator();
                break;
            case R.id.btn_6:
                mAnimator = getBezierCurveAnimator();
                break;
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

    private Animator getTranslationAnimator() {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX",
                0, 500);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY",
                0, 500);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mIvAvatar,
                pvhX, pvhY);
        animator.setDuration(2000);
        return animator;
    }

    private Animator getSetAnimator() {
        Animator alphaAnimator = getAlphaAnimator();
        Animator rotateAnimator = getRotateAnimator();
        Animator scaleAnimator = getScaleAnimator();
        Animator translationAnimator = getTranslationAnimator();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alphaAnimator, rotateAnimator,
                scaleAnimator, translationAnimator);
        return animatorSet;
    }

    private Animator getColorAnimator() {
//        ValueAnimator valueAnimator = ValueAnimator.ofArgb();


        return null;
    }


    private void getKeyFrameAnimator() {

    }

    private void getViewPropertyAnimator() {

    }

    private Animator getBezierCurveAnimator() {
        // 运动轨迹
        Path path = new Path();
        path.moveTo(0, 0);
        path.quadTo(500, 0, 500, 1000);
        // 创建动画
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        ValueAnimator animator = ValueAnimator.ofFloat(pathMeasure.getLength());
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

}
