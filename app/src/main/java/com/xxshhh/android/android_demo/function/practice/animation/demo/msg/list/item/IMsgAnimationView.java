package com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.item;

import android.view.View;

/**
 * 支持消息动画的View
 * Created by xxshhh on 2017/11/15.
 */
public interface IMsgAnimationView {
    void setMsgAnimationLifecycle(IMsgAnimationLifecycle animationLifecycle);

    View getItemView();

    View getMsgView();

    View getAvatarView();
}
