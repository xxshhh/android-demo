package com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.recyclerView;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.animation.IMsgAnimation;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.animation.impl.MsgAnimation_Rollover;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.item.IMsgAnimationView;

/**
 * RecyclerView动画
 * Created by xxshhh on 2017/11/15.
 */
public class MsgAnimationItemAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        View itemView = holder.itemView;
        if (itemView instanceof IMsgAnimationView) {
            IMsgAnimation msgAnimation = new MsgAnimation_Rollover(); // 使用气泡翻转动效
            msgAnimation.playAnimation((IMsgAnimationView) itemView);
        }
        return super.animateAdd(holder);
    }
}
