package com.xxshhh.android.android_demo.function.practice.animation.demo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.function.practice.animation.demo.animation.AnimationRevealFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Demo动画 viewHolder
 * Created by xxshhh on 2017/9/20.
 */
public class AnimationDemoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rl_root)
    RelativeLayout mRlRoot;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.cfl_container)
    AnimationRevealFrameLayout mCflContainer;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;

    public AnimationDemoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(String msg) {
        mTvMsg.setText(msg);
    }

}
