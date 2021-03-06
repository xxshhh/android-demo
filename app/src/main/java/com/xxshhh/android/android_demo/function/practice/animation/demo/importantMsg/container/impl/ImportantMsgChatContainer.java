package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.container.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.container.IImportantMsgChatContainer;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.widget.CircularRevealView;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.widget.ImportantMsgDialogView;

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

    private void initDialogView(final Object data, final View endLogoView) {
        final ImportantMsgDialogView dialogView = getDialogView(data);
        dialogView.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final CircularRevealView revealView = getCircularRevealView();
                revealView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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
                                if (endLogoView == null) {
                                    dialogView.dismiss();
                                    return;
                                }
                                // 变换动画
                                int[] endLoc = new int[2];
                                endLogoView.getLocationOnScreen(endLoc);
                                endLoc[0] += endLogoView.getWidth() / 2;
                                endLoc[1] += endLogoView.getHeight() / 2;
                                int startScale = 1;
                                float endScale = (float) endLogoView.getWidth() / (float) dialogView.getLogoWidth();
                                Animator transformAnimation = dialogView.getTransformAnimation(endLoc, startScale, endScale);
                                // 渐变背景动画
                                Animator gradientBackgroundAnimation = revealView.getGradientBackgroundAnimation();
                                // 开始播放动画
                                AnimatorSet animator = new AnimatorSet();
                                animator.playTogether(transformAnimation, gradientBackgroundAnimation);
                                animator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        dialogView.dismiss();
                                    }
                                });
                                animator.start();
                            }
                        });
                    }
                });
            }
        });
        dialogView.show();
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
        dialogView.setData(data);
        return dialogView;
    }
}
