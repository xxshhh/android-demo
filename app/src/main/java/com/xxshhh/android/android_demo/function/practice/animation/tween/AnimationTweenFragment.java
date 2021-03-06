package com.xxshhh.android.android_demo.function.practice.animation.tween;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.common.utils.ToastUtils;

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
    @BindView(R.id.sw_fillAfter)
    Switch mSwFillAfter;
    @BindView(R.id.sw_repeat)
    Switch mSwRepeat;
    @BindView(R.id.sw_repeat_mode)
    Switch mSwRepeatMode;
    @BindView(R.id.sp_interpolator)
    Spinner mSpInterpolator;
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;

    // common
    private AnimationSeekBarView mDurationView;
    // alpha
    private AnimationSeekBarView mFromAlphaView;
    private AnimationSeekBarView mToAlphaView;
    // rotate
    private AnimationSeekBarView mFromDegreesView;
    private AnimationSeekBarView mToDegreesView;
    private AnimationSeekBarView mPivotXRotateView;
    private AnimationSeekBarView mPivotYRotateView;
    // scale
    private AnimationSeekBarView mFromXScaleView;
    private AnimationSeekBarView mToXScaleView;
    private AnimationSeekBarView mFromYScaleView;
    private AnimationSeekBarView mToYScaleView;
    private AnimationSeekBarView mPivotXScaleView;
    private AnimationSeekBarView mPivotYScaleView;
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
        initContainerAnimation();
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
            case R.id.rb_set:
                animation = getSetAnimation();
                break;
            default:
        }
        if (animation == null) {
            return;
        }

        if (mSwFillAfter == null || mSwRepeat == null || mSwRepeatMode == null ||
                mSpInterpolator == null || mDurationView == null) {
            return;
        }

        boolean fillAfter = mSwFillAfter.isChecked();
        boolean repeat = mSwRepeat.isChecked();
        boolean repeatMode = mSwRepeatMode.isChecked();
        int interpolatorPos = mSpInterpolator.getSelectedItemPosition();
        long duration = (long) mDurationView.getValue();

        animation.setFillAfter(fillAfter);
        if (repeat) {
            animation.setRepeatCount(Animation.INFINITE);
        }
        if (repeatMode) {
            animation.setRepeatMode(Animation.REVERSE);
        }
        animation.setInterpolator(getInterpolator(interpolatorPos));
        animation.setDuration(duration);

        mIvAvatar.startAnimation(animation);
    }

    private void stopAnimation() {
        mIvAvatar.clearAnimation();
    }

    private void initRadioGroup() {
        mRgTween.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                resetCheckedId(checkedId);
                // 播放容器动画
                mLlContainer.startLayoutAnimation();
            }
        });
        // 设置默认选中
        mRbAlpha.setChecked(true);
    }

    private void initContainerAnimation() {
        AnimationSet animationSet = new AnimationSet(false);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setDuration(500);
        LayoutAnimationController controller = new LayoutAnimationController(animationSet);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        controller.setDelay(0.2f);
        mLlContainer.setLayoutAnimation(controller);
    }

    private void resetCheckedId(@IdRes int checkedId) {
        resetContainerCommon();
        resetContainerIndividual(checkedId);
    }

    private void resetContainerCommon() {
        mSwFillAfter.setChecked(false);
        mSwRepeat.setChecked(false);
        mSwRepeatMode.setChecked(false);
        mSpInterpolator.setSelection(0);

        if (mDurationView == null) {
            mDurationView = new AnimationSeekBarView(getContext());
            mLlContainer.addView(mDurationView.mView);
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
            case R.id.rb_set:
                views = getSetViews();
                break;
            default:
        }
        if (views == null || views.size() == 0) {
            return;
        }

        int childCount = mLlContainer.getChildCount();
        if (childCount > 5) {
            mLlContainer.removeViews(5, childCount - 5);
        }

        for (View view : views) {
            mLlContainer.addView(view);
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
        mFromDegreesView.init("fromDegrees", 0, 1440, 0, 0);

        if (mToDegreesView == null) {
            mToDegreesView = new AnimationSeekBarView(getContext());
        }
        mToDegreesView.init("toDegrees", 0, 1440, 360, 0);

        if (mPivotXRotateView == null) {
            mPivotXRotateView = new AnimationSeekBarView(getContext());
        }
        mPivotXRotateView.init("pivotXRotate", 0, 1, 0.5f, 2);

        if (mPivotYRotateView == null) {
            mPivotYRotateView = new AnimationSeekBarView(getContext());
        }
        mPivotYRotateView.init("pivotYRotate", 0, 1, 0.5f, 2);

        List<View> views = new ArrayList<>();
        views.add(mFromDegreesView.mView);
        views.add(mToDegreesView.mView);
        views.add(mPivotXRotateView.mView);
        views.add(mPivotYRotateView.mView);
        return views;
    }

    private Animation getRotateAnimation() {
        if (mFromDegreesView == null || mToDegreesView == null ||
                mPivotXRotateView == null || mPivotYRotateView == null) {
            return null;
        }

        float fromDegrees = mFromDegreesView.getValue();
        float toDegrees = mToDegreesView.getValue();
        int pivotXType = Animation.RELATIVE_TO_SELF;
        float pivotXValue = mPivotXRotateView.getValue();
        int pivotYType = Animation.RELATIVE_TO_SELF;
        float pivotYValue = mPivotYRotateView.getValue();
        return new RotateAnimation(fromDegrees, toDegrees, pivotXType,
                pivotXValue, pivotYType, pivotYValue);
    }

    private List<View> getScaleViews() {
        if (mFromXScaleView == null) {
            mFromXScaleView = new AnimationSeekBarView(getContext());
        }
        mFromXScaleView.init("fromXScale", 0, 2.5f, 0, 2);

        if (mToXScaleView == null) {
            mToXScaleView = new AnimationSeekBarView(getContext());
        }
        mToXScaleView.init("toXScale", 0, 2.5f, 2, 2);

        if (mFromYScaleView == null) {
            mFromYScaleView = new AnimationSeekBarView(getContext());
        }
        mFromYScaleView.init("fromYScale", 0, 2.5f, 0, 2);

        if (mToYScaleView == null) {
            mToYScaleView = new AnimationSeekBarView(getContext());
        }
        mToYScaleView.init("toYScale", 0, 2.5f, 2, 2);

        if (mPivotXScaleView == null) {
            mPivotXScaleView = new AnimationSeekBarView(getContext());
        }
        mPivotXScaleView.init("pivotXScale", 0, 1, 0.5f, 2);

        if (mPivotYScaleView == null) {
            mPivotYScaleView = new AnimationSeekBarView(getContext());
        }
        mPivotYScaleView.init("pivotYScale", 0, 1, 0.5f, 2);

        List<View> views = new ArrayList<>();
        views.add(mFromXScaleView.mView);
        views.add(mToXScaleView.mView);
        views.add(mFromYScaleView.mView);
        views.add(mToYScaleView.mView);
        views.add(mPivotXScaleView.mView);
        views.add(mPivotYScaleView.mView);
        return views;
    }

    private Animation getScaleAnimation() {
        if (mFromXScaleView == null || mToXScaleView == null ||
                mFromYScaleView == null || mToYScaleView == null ||
                mPivotXScaleView == null || mPivotYScaleView == null) {
            return null;
        }

        float fromX = mFromXScaleView.getValue();
        float toX = mToXScaleView.getValue();
        float fromY = mFromYScaleView.getValue();
        float toY = mToYScaleView.getValue();
        int pivotXType = Animation.RELATIVE_TO_SELF;
        float pivotXValue = mPivotXScaleView.getValue();
        int pivotYType = Animation.RELATIVE_TO_SELF;
        float pivotYValue = mPivotYScaleView.getValue();
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

    private List<View> getSetViews() {
        List<View> views = new ArrayList<>();
        views.addAll(getAlphaViews());
        views.addAll(getRotateViews());
        views.addAll(getScaleViews());
        views.addAll(getTranslateViews());
        return views;
    }

    private Animation getSetAnimation() {
        Animation alphaAnimation = getAlphaAnimation();
        if (alphaAnimation == null) {
            return null;
        }
        Animation rotateAnimation = getRotateAnimation();
        if (rotateAnimation == null) {
            return null;
        }
        Animation scaleAnimation = getScaleAnimation();
        if (scaleAnimation == null) {
            return null;
        }
        Animation translateAnimation = getTranslateAnimation();
        if (translateAnimation == null) {
            return null;
        }

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }

    private Interpolator getInterpolator(int pos) {
        Interpolator interpolator = null;
        switch (pos) {
            case 0:
                interpolator = new AccelerateDecelerateInterpolator();
                break;
            case 1:
                interpolator = new AccelerateInterpolator();
                break;
            case 2:
                interpolator = new AnticipateInterpolator();
                break;
            case 3:
                interpolator = new AnticipateOvershootInterpolator();
                break;
            case 4:
                interpolator = new BounceInterpolator();
                break;
            case 5:
                interpolator = new CycleInterpolator(2);
                break;
            case 6:
                interpolator = new DecelerateInterpolator();
                break;
            case 7:
                interpolator = new FastOutLinearInInterpolator();
                break;
            case 8:
                interpolator = new FastOutSlowInInterpolator();
                break;
            case 9:
                interpolator = new LinearInterpolator();
                break;
            case 10:
                interpolator = new LinearOutSlowInInterpolator();
                break;
            case 11:
                interpolator = new OvershootInterpolator();
                break;
            default:
        }
        return interpolator;
    }

}
