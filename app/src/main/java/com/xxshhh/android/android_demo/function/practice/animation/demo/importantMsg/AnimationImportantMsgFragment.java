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
import android.widget.Button;
import android.widget.FrameLayout;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.common.activity.CommonContainerActivity;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.avatar.view.ImportantMsgAvatarView;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.dialog.view.ImportantMsgDialogView;

import butterknife.BindView;

/**
 * 重要消息动画界面
 * Created by xxshhh on 2017/12/22.
 */
public class AnimationImportantMsgFragment extends BaseFragment {

    @BindView(R.id.btn_click)
    Button mBtnClick;

    @Override
    protected int getLayoutResID() {
        return R.layout.animation_fragment_important_msg;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAvatar();
            }
        });
    }

    private void initAvatar() {
        // 获取容器
        ViewGroup viewGroup = (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        // 创建帧布局
        final FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewGroup.addView(frameLayout);
        // 添加头像View
        final ImportantMsgAvatarView avatarView = new ImportantMsgAvatarView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 1500;
        params.leftMargin = 100;
        avatarView.setLayoutParams(params);
        frameLayout.addView(avatarView);
        // 动画播放时机
        avatarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                frameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // 展示动画
                final Animator showAnimation = avatarView.getShowAnimation();
                showAnimation.start();
                // 点击事件
                avatarView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 添加弹窗View
                        final ImportantMsgDialogView dialogView = new ImportantMsgDialogView(getContext());
                        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params2.gravity = Gravity.CENTER;
                        dialogView.setLayoutParams(params2);
                        dialogView.setVisibility(View.INVISIBLE);
                        frameLayout.addView(dialogView);
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
                                        frameLayout.removeView(avatarView);
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
                                        CommonContainerActivity.start(getContext(), AnimationImportantMsg2Fragment.class);
                                        getActivity().overridePendingTransition(0, 0);
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
}
