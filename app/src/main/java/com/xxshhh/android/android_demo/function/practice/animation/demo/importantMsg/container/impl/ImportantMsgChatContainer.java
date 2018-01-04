package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.container.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.container.IImportantMsgChatContainer;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.view.CircularRevealView;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.view.ImportantMsgDialogView;

/**
 * 重要消息聊天界面容器
 * Created by xwh on 2018/1/3.
 */
public class ImportantMsgChatContainer implements IImportantMsgChatContainer {

    private Activity mActivity;
    private ViewGroup mViewGroup;

    private FrameLayout mFrameLayout;

    public ImportantMsgChatContainer(@NonNull Activity activity) {
        mActivity = activity;
        mViewGroup = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @Override
    public void showDialogView(Object data, View endLogoView) {
        if (mFrameLayout == null) {
            mFrameLayout = getFrameLayout();
        }
        mFrameLayout.removeAllViews();
        initDialogView(data, endLogoView);
    }

    @Override
    public void destroy() {
        if (mFrameLayout == null) {
            return;
        }
        mFrameLayout.removeAllViews();
        mViewGroup.removeView(mFrameLayout);
        mFrameLayout = null;
    }

    private void initDialogView(Object data, final View endLogoView) {
        final CircularRevealView revealView = getCircularRevealView();
        final ImportantMsgDialogView dialogView = getDialogView(data);
        // 监听绘制
        dialogView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dialogView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                // 展示动画
                Animator showAnimation = dialogView.getShowAnimation();
                // 揭露动画
                int[] revealLoc = new int[2];
                revealView.getLocationOnScreen(revealLoc);
                float centerX = dialogView.getLogoCenterLocation()[0] - revealLoc[0];
                float centerY = dialogView.getLogoCenterLocation()[1] - revealLoc[1];
                float r1 = (float) Math.sqrt(centerX * centerX + centerY * centerY);
                float r2 = (float) Math.sqrt(centerX * centerX + (revealView.getHeight() - centerY) * (revealView.getHeight() - centerY));
                float startRadius = 0;
                float endRadius = Math.max(r1, r2);
                Animator revealAnimation = revealView.getCircularRevealAnimation(centerX, centerY, startRadius, endRadius);
                // 开始播放动画
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(showAnimation, revealAnimation);
                animatorSet.start();
                // 点击事件
                dialogView.setConfirmEvent(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 变换动画
                        int[] endLoc = new int[2];
                        endLogoView.getLocationOnScreen(endLoc);
                        endLoc[0] += endLogoView.getWidth() / 2;
                        endLoc[1] += endLogoView.getHeight() / 2;
                        Animator transformAnimation = dialogView.getTransformAnimation(endLoc, 1, (float) endLogoView.getWidth() / (float) dialogView.getLogoWidth());
                        transformAnimation.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                endLogoView.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                endLogoView.setVisibility(View.VISIBLE);
                            }
                        });
                        // 渐变背景动画
                        Animator gradientBackgroundAnimation = revealView.getGradientBackgroundAnimation();
                        // 开始播放动画
                        AnimatorSet animator = new AnimatorSet();
                        animator.playTogether(transformAnimation, gradientBackgroundAnimation);
                        animator.start();
                    }
                });
            }
        });
    }

    private FrameLayout getFrameLayout() {
        // 添加帧布局
        FrameLayout frameLayout = new FrameLayout(mActivity);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mViewGroup.addView(frameLayout);
        return frameLayout;
    }

    private CircularRevealView getCircularRevealView() {
        // 添加圆形揭露View
        CircularRevealView revealView = new CircularRevealView(mActivity);
        revealView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFrameLayout.addView(revealView);
        return revealView;
    }

    private ImportantMsgDialogView getDialogView(Object data) {
        // 添加弹窗View
        ImportantMsgDialogView dialogView = new ImportantMsgDialogView(mActivity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        dialogView.setLayoutParams(params);
        mFrameLayout.addView(dialogView);
        return dialogView;
    }
}
