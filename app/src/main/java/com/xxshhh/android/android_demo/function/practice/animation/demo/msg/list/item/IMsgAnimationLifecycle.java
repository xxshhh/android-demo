package com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.item;

import android.graphics.Canvas;

/**
 * 消息动画生命周期
 * Created by xxshhh on 2017/11/15.
 */
public interface IMsgAnimationLifecycle {
    void onDraw(Canvas canvas);

    void onDestroy();
}
