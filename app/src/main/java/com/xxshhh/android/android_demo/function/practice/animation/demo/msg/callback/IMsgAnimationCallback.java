package com.xxshhh.android.android_demo.function.practice.animation.demo.msg.callback;

import android.view.View;
import android.view.ViewGroup;

/**
 * 消息动画回调
 * Created by xxshhh on 2017/11/15.
 */
public interface IMsgAnimationCallback {
    /**
     * 获取动画播放的父容器
     */
    ViewGroup getAnimationParentView();

    /**
     * 获取动画播放的底部View
     */
    View getAnimationBottomView();
}
