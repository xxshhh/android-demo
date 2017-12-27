package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.dialog.view.CircularRevealView;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.dialog.view.ImportantMsgDialogView;

import butterknife.BindView;

/**
 * 重要消息动画界面2
 * Created by xxshhh on 2017/12/22.
 */
public class AnimationImportantMsg2Fragment extends BaseFragment {

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.rl_content)
    RelativeLayout mRlContent;
    @BindView(R.id.iv_logo)
    ImageView mIvLogo;

    @Override
    protected int getLayoutResID() {
        return R.layout.animation_fragment_important_msg2;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        initDialog();
    }

    private void initDialog() {
        // 获取容器
        ViewGroup viewGroup = (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        // 创建帧布局
        final FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewGroup.addView(frameLayout);
        // 添加圆形揭露View
        final CircularRevealView revealView = new CircularRevealView(getContext());
        revealView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frameLayout.addView(revealView);
        // 添加弹窗View
        final ImportantMsgDialogView dialogView = new ImportantMsgDialogView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        dialogView.setLayoutParams(params);
        frameLayout.addView(dialogView);
        // 动画播放时机
        frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
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
                dialogView.setConfirmClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 变换动画
                        int[] endLoc = new int[2];
                        mIvLogo.getLocationOnScreen(endLoc);
                        endLoc[0] += mIvLogo.getWidth() / 2;
                        endLoc[1] += mIvLogo.getHeight() / 2;
                        Animator transformAnimation = dialogView.getTransformAnimation(endLoc, 1, (float) mIvLogo.getWidth() / (float) dialogView.getLogoWidth());
                        transformAnimation.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                mIvLogo.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mIvLogo.setVisibility(View.VISIBLE);
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
}
