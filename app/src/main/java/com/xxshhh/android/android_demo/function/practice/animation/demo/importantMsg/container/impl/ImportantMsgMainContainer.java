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

import com.xxshhh.android.android_demo.common.activity.CommonContainerActivity;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.AnimationImportantMsg2Fragment;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.container.IImportantMsgMainContainer;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.widget.ImportantMsgAvatarView;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.widget.ImportantMsgDialogView;

/**
 * 重要消息主界面容器
 * Created by xwh on 2018/1/3.
 */
public class ImportantMsgMainContainer implements IImportantMsgMainContainer {

    private Activity mActivity;
    private ViewGroup mViewGroup;

    private FrameLayout mFrameLayout;

    public ImportantMsgMainContainer(@NonNull Activity activity) {
        mActivity = activity;
        mViewGroup = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @Override
    public void showAvatarView(Object data) {
        if (mFrameLayout == null) {
            mFrameLayout = getFrameLayout();
        }
        mFrameLayout.removeAllViews();
        initAvatarView(data);
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

    private void initAvatarView(final Object data) {
        final ImportantMsgAvatarView avatarView = getAvatarView(data);
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
                        final ImportantMsgDialogView dialogView = getDialogView(data);
                        dialogView.setVisibility(View.INVISIBLE); // 默认不可见
                        dialogView.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                // 变换动画
                                int[] endLoc = dialogView.getLogoCenterLocation();
                                Animator transformAnimation = avatarView.getTransformAnimation(endLoc);
                                // 过渡动画
                                float startScale = (float) avatarView.getWidth() / (float) dialogView.getLogoWidth();
                                int endScale = 1;
                                Animator transitionAnimation = dialogView.getTransitionAnimation(startScale, endScale);
                                // 开始播放动画
                                AnimatorSet animatorSet = new AnimatorSet();
                                animatorSet.playSequentially(transformAnimation, transitionAnimation);
                                animatorSet.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationStart(animation);
                                        showAnimation.end();
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        CommonContainerActivity.start(mActivity, AnimationImportantMsg2Fragment.class);
                                        mActivity.overridePendingTransition(0, 0);
                                    }
                                });
                                animatorSet.start();
                            }
                        });
                        dialogView.show();
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

    private ImportantMsgAvatarView getAvatarView(Object data) {
        // 添加头像View
        ImportantMsgAvatarView avatarView = new ImportantMsgAvatarView(mActivity);
        avatarView.setData(data);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 1500;
        params.leftMargin = 100;
        avatarView.setLayoutParams(params);
        mFrameLayout.addView(avatarView);
        return avatarView;
    }

    private ImportantMsgDialogView getDialogView(Object data) {
        // 添加弹窗View
        ImportantMsgDialogView dialogView = new ImportantMsgDialogView(mActivity);
        dialogView.setData(data);
        return dialogView;
    }
}
