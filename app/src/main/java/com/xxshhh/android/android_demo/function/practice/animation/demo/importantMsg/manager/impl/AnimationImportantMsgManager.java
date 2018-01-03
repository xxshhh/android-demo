package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.manager.impl;

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

import com.xxshhh.android.android_demo.common.activity.CommonContainerActivity;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.AnimationImportantMsg2Fragment;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.avatar.ImportantMsgAvatarView;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.dialog.CircularRevealView;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.dialog.ImportantMsgDialogView;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.manager.IAnimationImportantMsgManager;

/**
 * 重要消息动画管理类
 * Created by xwh on 2017/12/29.
 */
public class AnimationImportantMsgManager implements IAnimationImportantMsgManager {

    private Activity mActivity;
    private FrameLayout mFrameLayout;

    public AnimationImportantMsgManager(@NonNull Activity activity) {
        mActivity = activity;
    }

    @Override
    public void addAvatarView() {
        if (mFrameLayout == null) {
            mFrameLayout = initFrameLayout();
        }
        // 添加头像View
        final ImportantMsgAvatarView avatarView = new ImportantMsgAvatarView(mActivity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 1500;
        params.leftMargin = 100;
        avatarView.setLayoutParams(params);
        mFrameLayout.addView(avatarView);
        // 监听绘制
        avatarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                avatarView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                // 播放展示动画
                final Animator showAnimation = avatarView.getShowAnimation();
                showAnimation.start();
                // 设置点击事件
                avatarView.setClickEvent(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 添加弹窗View
                        final ImportantMsgDialogView dialogView = new ImportantMsgDialogView(mActivity);
                        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params2.gravity = Gravity.CENTER;
                        dialogView.setLayoutParams(params2);
                        dialogView.setVisibility(View.INVISIBLE);
                        mFrameLayout.addView(dialogView);
                        // 监听绘制
                        dialogView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                dialogView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                // 变换动画
                                Animator transformAnimation = avatarView.getTransformAnimation(dialogView.getLogoCenterLocation());
                                transformAnimation.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationStart(animation);
                                        showAnimation.end();
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        avatarView.setVisibility(View.INVISIBLE);
                                    }
                                });
                                // 过渡动画
                                Animator transitionAnimation = dialogView.getTransitionAnimation((float) avatarView.getWidth() / (float) dialogView.getLogoWidth(), 1);
                                transitionAnimation.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationStart(animation);
                                        dialogView.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        CommonContainerActivity.start(mActivity, AnimationImportantMsg2Fragment.class);
                                        mActivity.overridePendingTransition(0, 0);
                                    }
                                });
                                // 开始播放动画
                                AnimatorSet animatorSet = new AnimatorSet();
                                animatorSet.playSequentially(transformAnimation, transitionAnimation);
                                animatorSet.start();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void addDialogView(final View endLogoView) {
        if (mFrameLayout == null) {
            mFrameLayout = initFrameLayout();
        }
        // 添加圆形揭露View
        final CircularRevealView revealView = new CircularRevealView(mActivity);
        revealView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFrameLayout.addView(revealView);
        // 添加弹窗View
        final ImportantMsgDialogView dialogView = new ImportantMsgDialogView(mActivity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        dialogView.setLayoutParams(params);
        mFrameLayout.addView(dialogView);
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

    private FrameLayout initFrameLayout() {
        // 获取容器
        ViewGroup viewGroup = (ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        // 创建并添加帧布局
        FrameLayout frameLayout = new FrameLayout(mActivity);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewGroup.addView(frameLayout);
        return frameLayout;
    }
}
