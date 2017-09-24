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

    public AnimationSeekBarView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.animation_view_seekbar, null);
        ButterKnife.bind(this, mView);
    }

    public void init(String label, final float min, final float max, float defaultValue, final int scale) {
        mTvLabel.setText(label);
        mTvValue.setText(String.valueOf(defaultValue));
        int defaultProgress = (int) ((defaultValue - min) / (max - min));
        mSbValue.setMax(100);
        mSbValue.setProgress(defaultProgress);
        mSbValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = min + progress / 100 * (max - min);
                value = new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
                mTvValue.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

}
