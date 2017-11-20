package com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.item.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.item.IMsgAnimationLifecycle;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.item.IMsgAnimationView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 动画消息项
 * Created by xxshhh on 2017/11/15.
 */
public class MsgAnimationItemView extends LinearLayout
        implements IMsgAnimationView {

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.rl_content)
    RelativeLayout mRlContent;
    @BindView(R.id.rl_root)
    RelativeLayout mRlRoot;

    private IMsgAnimationLifecycle mAnimationLifecycle; // 消息动画生命周期

    public MsgAnimationItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.animation_view_msg_item, this, true);
        ButterKnife.bind(this);

        setWillNotDraw(false); // 加上这句ViewGroup才会调用onDraw，此处播放动画需要用到
    }

    public void setData(String msg) {
        mTvMsg.setText(msg);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimationLifecycle != null) {
            mAnimationLifecycle.onDraw(canvas);
        }
    }

    public void recycled() {
        if (mAnimationLifecycle != null) {
            mAnimationLifecycle.onDestroy();
        }
    }

    @Override
    public void setMsgAnimationLifecycle(IMsgAnimationLifecycle animationLifecycle) {
        mAnimationLifecycle = animationLifecycle;
    }

    @Override
    public View getItemView() {
        return this;
    }

    @Override
    public View getMsgView() {
        return mRlContent;
    }

    @Override
    public View getAvatarView() {
        return mIvAvatar;
    }
}
