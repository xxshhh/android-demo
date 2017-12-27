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
 * 重要消息弹窗动画界面
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
        ViewGroup viewGroup = (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);

        final FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewGroup.addView(frameLayout);

        final CircularRevealView revealView = new CircularRevealView(getContext());
        revealView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frameLayout.addView(revealView);

        final ImportantMsgDialogView dialogView = new ImportantMsgDialogView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        dialogView.setLayoutParams(params);
        frameLayout.addView(dialogView);

        dialogView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dialogView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                Animator showAnimation = dialogView.getShowAnimation();

                int[] logoLoc = new int[2];
                final ImageView logoView = dialogView.getIvLogo();
                logoView.getLocationOnScreen(logoLoc);
                int[] revealLoc = new int[2];
                revealView.getLocationOnScreen(revealLoc);
                float centerX = revealView.getWidth() / 2;
                float centerY = logoLoc[1] - revealLoc[1] + logoView.getHeight() / 2;
                float r1 = (float) Math.sqrt(centerX * centerX + centerY * centerY);
                float r2 = (float) Math.sqrt(centerX * centerX + (revealView.getHeight() - centerY) * (revealView.getHeight() - centerY));
                float startRadius = 0;
                float endRadius = Math.max(r1, r2);
                Animator revealAnimation = revealView.getCircularRevealAnimation(centerX, centerY, startRadius, endRadius, 500);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(showAnimation, revealAnimation);
                animatorSet.start();

                dialogView.getTvConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int[] targetLogoLoc = new int[2];
                        mIvLogo.getLocationOnScreen(targetLogoLoc);

                        int[] endLoc = new int[2];
                        endLoc[0] = targetLogoLoc[0] + mIvLogo.getWidth() / 2 - logoView.getWidth() / 2;
                        endLoc[1] = targetLogoLoc[1] + mIvLogo.getHeight() / 2 - logoView.getHeight() / 2;

                        Animator transformAnimation = dialogView.getTransformAnimation(endLoc, 1, (float) mIvLogo.getHeight() / (float) logoView.getHeight());
                        transformAnimation.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                mIvLogo.setVisibility(View.INVISIBLE);
                                frameLayout.removeView(revealView);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mIvLogo.setVisibility(View.VISIBLE);
                                frameLayout.removeView(dialogView);
                            }
                        });
                        transformAnimation.start();
                    }
                });
            }
        });
    }
}
