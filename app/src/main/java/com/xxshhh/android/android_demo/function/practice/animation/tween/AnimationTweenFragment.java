package com.xxshhh.android.android_demo.function.practice.animation.tween;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 补间动画界面
 * Created by xxshhh on 2017/9/19.
 */
public class AnimationTweenFragment extends BaseFragment {

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.btn_stop)
    Button mBtnStop;
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
    @BindView(R.id.rg_tween)
    RadioGroup mRgTween;
    @BindView(R.id.sw_fillAfter)
    Switch mSwFillAfter;
    @BindView(R.id.sw_repeatMode)
    Switch mSwRepeatMode;
    @BindView(R.id.ll_container_common)
    LinearLayout mLlContainerCommon;
    @BindView(R.id.ll_container_individual)
    LinearLayout mLlContainerIndividual;

    // common
    private AnimationSeekBarView mDurationView;
    // alpha
    private AnimationSeekBarView mFromAlphaView;
    private AnimationSeekBarView mToAlphaView;

    // rotate & scale
    private AnimationSeekBarView mPivotXView;
    private AnimationSeekBarView mPivotYView;
    // rotate
    private AnimationSeekBarView mFromDegreesView;
    private AnimationSeekBarView mToDegreesView;
    // scale
    private AnimationSeekBarView mFromXScaleView;
    private AnimationSeekBarView mToXScaleView;
    private AnimationSeekBarView mFromYScaleView;
    private AnimationSeekBarView mToYScaleView;
    // translate
    private AnimationSeekBarView mFromXDeltaView;
    private AnimationSeekBarView mToXDeltaView;
    private AnimationSeekBarView mFromYDeltaView;
    private AnimationSeekBarView mToYDeltaView;

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
        initAvatar();
        initButton();
        initRadioGroup();
    }

    private void initAvatar() {
        Drawable drawable = new IconicsDrawable(getContext())
                .icon(CommunityMaterial.Icon.cmd_face)
                .colorRes(R.color.colorPrimary)
                .sizeDp(80);
        mIvAvatar.setImageDrawable(drawable);
    }

    private void initButton() {
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });

        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAnimation();
            }
        });

        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAnimation();
            }
        });
    }

    private void startAnimation() {
        Animation animation = null;
        int checkedId = mRgTween.getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.rb_alpha:
                animation = getAlphaAnimation();
                break;
            case R.id.rb_rotate:
                animation = getRotateAnimation();
                break;
            case R.id.rb_scale:
                animation = getScaleAnimation();
                break;
            case R.id.rb_translate:
                animation = getTranslateAnimation();
                break;
            default:
        }
        if (animation == null) {
            return;
        }

        if (mSwFillAfter == null || mSwRepeatMode == null || mDurationView == null) {
            return;
        }

        boolean fillAfter = mSwFillAfter.isChecked();
        boolean repeatMode = mSwRepeatMode.isChecked();
        long duration = (long) mDurationView.getValue();
        animation.setFillAfter(fillAfter);
        if (repeatMode) {
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.INFINITE);
        }
        animation.setDuration(duration);
        mIvAvatar.startAnimation(animation);
    }

    private void stopAnimation() {
        mIvAvatar.clearAnimation();
    }

    private void resetAnimation() {
        stopAnimation();
        resetCheckedId(mRgTween.getCheckedRadioButtonId());
    }

    private void initRadioGroup() {
        mRgTween.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                resetCheckedId(checkedId);
            }
        });
        // 设置默认选中
        mRbAlpha.setChecked(true);
    }

    private void resetCheckedId(@IdRes int checkedId) {
        resetContainerCommon();
        resetContainerIndividual(checkedId);
    }

    private void resetContainerCommon() {
        mSwFillAfter.setChecked(false);
        mSwRepeatMode.setChecked(false);

        if (mDurationView == null) {
            mDurationView = new AnimationSeekBarView(getContext());
            mLlContainerCommon.addView(mDurationView.mView);
        }
        mDurationView.init("duration", 0, 5000, 1000, 0);
    }

    private void resetContainerIndividual(@IdRes int checkedId) {
        List<View> views = null;
        switch (checkedId) {
            case R.id.rb_alpha:
                views = getAlphaViews();
                break;
            case R.id.rb_rotate:
                views = getRotateViews();
                break;
            case R.id.rb_scale:
                views = getScaleViews();
                break;
            case R.id.rb_translate:
                views = getTranslateViews();
                break;
            default:
        }
        if (views == null || views.size() == 0) {
            return;
        }

        mLlContainerIndividual.removeAllViews();
        for (View view : views) {
            mLlContainerIndividual.addView(view);
        }
    }

    private List<View> getAlphaViews() {
        if (mFromAlphaView == null) {
            mFromAlphaView = new AnimationSeekBarView(getContext());
        }
        mFromAlphaView.init("fromAlpha", 0, 1, 0, 2);

        if (mToAlphaView == null) {
            mToAlphaView = new AnimationSeekBarView(getContext());
        }
        mToAlphaView.init("toAlpha", 0, 1, 1, 2);

        List<View> views = new ArrayList<>();
        views.add(mFromAlphaView.mView);
        views.add(mToAlphaView.mView);
        return views;
    }

    private Animation getAlphaAnimation() {
        if (mFromAlphaView == null || mToAlphaView == null) {
            return null;
        }

        float fromAlpha = mFromAlphaView.getValue();
        float toAlpha = mToAlphaView.getValue();
        return new AlphaAnimation(fromAlpha, toAlpha);
    }

    private List<View> getRotateViews() {
        if (mFromDegreesView == null) {
            mFromDegreesView = new AnimationSeekBarView(getContext());
        }
        mFromDegreesView.init("fromDegrees", 0, 1080, 0, 0);

        if (mToDegreesView == null) {
            mToDegreesView = new AnimationSeekBarView(getContext());
        }
        mToDegreesView.init("toDegrees", 0, 1080, 360, 0);

        if (mPivotXView == null) {
            mPivotXView = new AnimationSeekBarView(getContext());
        }
        mPivotXView.init("pivotX", 0, 1, 0.5f, 2);

        if (mPivotYView == null) {
            mPivotYView = new AnimationSeekBarView(getContext());
        }
        mPivotYView.init("pivotY", 0, 1, 0.5f, 2);

        List<View> views = new ArrayList<>();
        views.add(mFromDegreesView.mView);
        views.add(mToDegreesView.mView);
        views.add(mPivotXView.mView);
        views.add(mPivotYView.mView);
        return views;
    }

    private Animation getRotateAnimation() {
        if (mFromDegreesView == null || mToDegreesView == null ||
                mPivotXView == null || mPivotYView == null) {
            return null;
        }

        float fromDegrees = mFromDegreesView.getValue();
        float toDegrees = mToDegreesView.getValue();
        int pivotXType = Animation.RELATIVE_TO_SELF;
        float pivotXValue = mPivotXView.getValue();
        int pivotYType = Animation.RELATIVE_TO_SELF;
        float pivotYValue = mPivotYView.getValue();
        return new RotateAnimation(fromDegrees, toDegrees, pivotXType,
                pivotXValue, pivotYType, pivotYValue);
    }

    private List<View> getScaleViews() {
        if (mFromXScaleView == null) {
            mFromXScaleView = new AnimationSeekBarView(getContext());
        }
        mFromXScaleView.init("fromXScale", 0, 3, 0, 2);

        if (mToXScaleView == null) {
            mToXScaleView = new AnimationSeekBarView(getContext());
        }
        mToXScaleView.init("toXScale", 0, 3, 2, 2);

        if (mFromYScaleView == null) {
            mFromYScaleView = new AnimationSeekBarView(getContext());
        }
        mFromYScaleView.init("fromYScale", 0, 3, 0, 2);

        if (mToYScaleView == null) {
            mToYScaleView = new AnimationSeekBarView(getContext());
        }
        mToYScaleView.init("toYScale", 0, 3, 2, 2);

        if (mPivotXView == null) {
            mPivotXView = new AnimationSeekBarView(getContext());
        }
        mPivotXView.init("pivotX", 0, 1, 0.5f, 2);

        if (mPivotYView == null) {
            mPivotYView = new AnimationSeekBarView(getContext());
        }
        mPivotYView.init("pivotY", 0, 1, 0.5f, 2);

        List<View> views = new ArrayList<>();
        views.add(mFromXScaleView.mView);
        views.add(mToXScaleView.mView);
        views.add(mFromYScaleView.mView);
        views.add(mToYScaleView.mView);
        views.add(mPivotXView.mView);
        views.add(mPivotYView.mView);
        return views;
    }

    private Animation getScaleAnimation() {
        if (mFromXScaleView == null || mToXScaleView == null ||
                mFromYScaleView == null || mToYScaleView == null ||
                mPivotXView == null || mPivotYView == null) {
            return null;
        }

        float fromX = mFromXScaleView.getValue();
        float toX = mToXScaleView.getValue();
        float fromY = mFromYScaleView.getValue();
        float toY = mToYScaleView.getValue();
        int pivotXType = Animation.RELATIVE_TO_SELF;
        float pivotXValue = mPivotXView.getValue();
        int pivotYType = Animation.RELATIVE_TO_SELF;
        float pivotYValue = mPivotYView.getValue();
        return new ScaleAnimation(fromX, toX, fromY, toY,
                pivotXType, pivotXValue, pivotYType, pivotYValue);
    }

    private List<View> getTranslateViews() {
        if (mFromXDeltaView == null) {
            mFromXDeltaView = new AnimationSeekBarView(getContext());
        }
        mFromXDeltaView.init("fromXDelta", -1, 1, -1, 2);

        if (mToXDeltaView == null) {
            mToXDeltaView = new AnimationSeekBarView(getContext());
        }
        mToXDeltaView.init("toXDelta", -1, 1, 1, 2);

        if (mFromYDeltaView == null) {
            mFromYDeltaView = new AnimationSeekBarView(getContext());
        }
        mFromYDeltaView.init("fromYDelta", -1, 1, -1, 2);

        if (mToYDeltaView == null) {
            mToYDeltaView = new AnimationSeekBarView(getContext());
        }
        mToYDeltaView.init("toYDelta", -1, 1, 1, 2);

        List<View> views = new ArrayList<>();
        views.add(mFromXDeltaView.mView);
        views.add(mToXDeltaView.mView);
        views.add(mFromYDeltaView.mView);
        views.add(mToYDeltaView.mView);
        return views;
    }

    private Animation getTranslateAnimation() {
        if (mFromXDeltaView == null || mToXDeltaView == null ||
                mFromYDeltaView == null || mToYDeltaView == null) {
            return null;
        }

        int fromXType = Animation.RELATIVE_TO_SELF;
        float fromXValue = mFromXDeltaView.getValue();
        int toXType = Animation.RELATIVE_TO_SELF;
        float toXValue = mToXDeltaView.getValue();
        int fromYType = Animation.RELATIVE_TO_SELF;
        float fromYValue = mFromYDeltaView.getValue();
        int toYType = Animation.RELATIVE_TO_SELF;
        float toYValue = mToYDeltaView.getValue();
        return new TranslateAnimation(fromXType, fromXValue, toXType, toXValue,
                fromYType, fromYValue, toYType, toYValue);
    }

}
