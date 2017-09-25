package com.xxshhh.android.android_demo.function.practice.animation.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xxshhh.android.android_demo.R;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 动画拖动条
 * Created by xxshhh on 2017/9/24.
 */
public class AnimationSeekBarView {

    public View mView;

    @BindView(R.id.tv_label)
    TextView mTvLabel;
    @BindView(R.id.tv_value)
    TextView mTvValue;
    @BindView(R.id.sb_value)
    SeekBar mSbValue;

    private String mLabelString;
    private float mMinValue;
    private float mMaxValue;
    private float mDefaultValue;
    private int mValueScale;

    public AnimationSeekBarView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.animation_view_seekbar, null);
        ButterKnife.bind(this, mView);
    }

    public void init(String labelString, final float minValue, final float maxValue, float defaultValue, final int valueScale) {
        mLabelString = labelString;
        mMinValue = minValue;
        mMaxValue = maxValue;
        mDefaultValue = defaultValue;
        mValueScale = valueScale;

        initLabel();
        initValueText();
        initSeekBar();
    }

    private void initLabel() {
        mTvLabel.setText(mLabelString);
    }

    private void initValueText() {
        setValue(mDefaultValue);
    }

    private void initSeekBar() {
        int defaultProgress = (int) ((mDefaultValue - mMinValue) / (mMaxValue - mMinValue) * 100);
        mSbValue.setMax(100);
        mSbValue.setProgress(defaultProgress);
        mSbValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = (float) (mMinValue + progress / 100.0 * (mMaxValue - mMinValue));
                setValue(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setValue(float value) {
        if (mValueScale > 0) {
            value = new BigDecimal(value).setScale(mValueScale, BigDecimal.ROUND_HALF_UP).floatValue();
            mTvValue.setText(String.valueOf(value));
        } else {
            mTvValue.setText(String.valueOf((int) value));
        }
    }

    public float getValue() {
        return Float.parseFloat(mTvValue.getText().toString());
    }

}
