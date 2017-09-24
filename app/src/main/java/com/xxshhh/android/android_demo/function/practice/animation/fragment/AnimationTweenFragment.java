package com.xxshhh.android.android_demo.function.practice.animation.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.function.practice.animation.view.AnimationSeekBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 补间动画界面
 * Created by xwh on 2017/9/19
 */
public class AnimationTweenFragment extends BaseFragment {

    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.btn_pause)
    Button mBtnPause;
    @BindView(R.id.btn_reset)
    Button mBtnReset;
    @BindView(R.id.rb_alpha)
    RadioButton mRbAlpha;
    @BindView(R.id.rb_rotate)
    RadioButton mRbRotate;
    @BindView(R.id.rb_scale)
    RadioButton mRbScale;
    @BindView(R.id.rb_translate)
    RadioButton mRbTranslate;
    @BindView(R.id.rb_set)
    RadioButton mRbSet;
    @BindView(R.id.rg_tween)
    RadioGroup mRgTween;
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;

    private AnimationSeekBarView mDurationView;
    private AnimationSeekBarView mFromAlphaView;
    private AnimationSeekBarView mToAlpha;
    private AnimationSeekBarView mFromDegreesView;
    private AnimationSeekBarView mToDegreesView;

    @Override
    protected int getLayoutResID() {
        return R.layout.animation_fragment_tween;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        initActionButton();
        initRgTween();


        AlphaAnimation alphaAnimation;
        RotateAnimation rotateAnimation;
        ScaleAnimation scaleAnimation;
        TranslateAnimation translateAnimation;
        AnimationSet animationSet;
    }

    private void initActionButton() {
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mBtnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initRgTween() {
        mRgTween.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_alpha:
                        initAlpha();
                        break;
                    case R.id.rb_rotate:
                        initRotate();
                        break;
                    case R.id.rb_scale:
                        break;
                    case R.id.rb_translate:
                        break;
                    case R.id.rb_set:
                        break;
                    default:
                }
            }
        });
        mRbAlpha.setChecked(true);
    }

    private void initAlpha() {
        mDurationView = new AnimationSeekBarView(getContext());
        mDurationView.init("duration", 0, 10000, 2000, 0);

        mFromAlphaView = new AnimationSeekBarView(getContext());
        mFromAlphaView.init("fromAlpha", 0, 1, 0, 2);

        mToAlpha = new AnimationSeekBarView(getContext());
        mToAlpha.init("toAlpha", 0, 1, 1, 2);

        mLlContainer.removeAllViews();
        mLlContainer.addView(mDurationView.mView);
        mLlContainer.addView(mFromAlphaView.mView);
        mLlContainer.addView(mToAlpha.mView);

        float fromAlpha = 0;
        float toAlpha = 1;
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(1000);
    }

    private void initRotate() {
        mDurationView = new AnimationSeekBarView(getContext());
        mDurationView.init("duration", 0, 10000, 2000, 0);

        mFromDegreesView = new AnimationSeekBarView(getContext());
        mFromDegreesView.init("fromDegrees", 0, 360, 0, 0);

        mToDegreesView = new AnimationSeekBarView(getContext());
        mToDegreesView.init("toDegrees", 0, 360, 360, 0);

        mLlContainer.removeAllViews();
        mLlContainer.addView(mDurationView.mView);
        mLlContainer.addView(mFromDegreesView.mView);
        mLlContainer.addView(mToDegreesView.mView);

//        float fromDegrees = 0;
//        float toDegrees = 360;
//        int pivotXType = Animation.RELATIVE_TO_SELF;
//        float pivotXValue = 0;
//        int pivotYType = Animation.RELATIVE_TO_SELF;
//        float pivotYValue = 0;
//        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees,
//                pivotXType, pivotXValue, pivotYType, pivotYValue);
//        rotateAnimation.setDuration(1000);
    }

}
