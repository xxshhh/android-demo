package com.xxshhh.android.android_demo.function.practice.animation.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.function.practice.animation.demo.AnimationAdapter;

import butterknife.BindView;

/**
 * Demo动画界面
 * Created by xxshhh on 2017/9/19.
 */
public class AnimationDemoFragment extends BaseFragment {

    @BindView(R.id.rl_root)
    RelativeLayout mRlRoot;
    @BindView(R.id.btn_add)
    Button mBtnAdd;
    @BindView(R.id.btn_remove)
    Button mBtnRemove;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.rv_msg_list)
    RecyclerView mRvMsgList;

    private AnimationAdapter mAdapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.animation_fragment_demo;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        initMsgList();
        initSendButton();
    }

    private void initMsgList() {
        mAdapter = new AnimationAdapter(getContext());
        mRvMsgList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvMsgList.setItemAnimator(new DefaultItemAnimator());
        mRvMsgList.setHasFixedSize(true);
        mRvMsgList.setAdapter(mAdapter);
    }

    private void initSendButton() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRvMsgList.getLayoutManager();
                View endView = linearLayoutManager.findViewByPosition(mAdapter.getDataList().size() - 1);
                if (endView == null) {
                    insertMsg();
                } else {
                    endView = endView.findViewById(R.id.tv_msg);
                    startCircleAnimation(v, endView);
                }
            }
        });

        mBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mAdapter.getDataList().size();
                mAdapter.getDataList().remove(size - 1);
                mAdapter.notifyItemRemoved(size - 1);
            }
        });
    }

    private void insertMsg() {
        int size = mAdapter.getDataList().size();
        String msg = size + getString(R.string.place_text);
        mAdapter.getDataList().add(msg);
        mAdapter.notifyItemInserted(size);
        mRvMsgList.scrollToPosition(size);
    }

    private void startCircleAnimation(View startView, View endView) {
        Drawable drawable = new IconicsDrawable(getContext())
                .icon(CommunityMaterial.Icon.cmd_circle)
                .sizeDp(22)
                .color(Color.parseColor("#38adff"));
        final ImageView circle = new ImageView(getContext());
        circle.setImageDrawable(drawable);
        mRlRoot.addView(circle);

        int[] parentLoc = new int[2];
        mRlRoot.getLocationInWindow(parentLoc);
        int[] startLoc = new int[2];
        startView.getLocationInWindow(startLoc);
        startLoc[0] += startView.getWidth() / 2;
        startLoc[1] += startView.getHeight() / 2;
        int[] endLoc = new int[2];
        endView.getLocationInWindow(endLoc);
        endLoc[0] += endView.getWidth() / 2;
        endLoc[1] += endView.getHeight() * 11 / 7;

        float startX = startLoc[0] - parentLoc[0] + circle.getWidth() / 2;
        float startY = startLoc[1] - parentLoc[1] + circle.getHeight() / 2;
        float toX = endLoc[0] - parentLoc[0] + circle.getWidth() / 2;
        float toY = endLoc[1] - parentLoc[1] + circle.getHeight() / 2;

        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(startX, (startY + toY) / 2, toX, toY);

        final PathMeasure pathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        final float[] currentPos = new float[2];
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                pathMeasure.getPosTan(value, currentPos, null);
                circle.setTranslationX(currentPos[0]);
                circle.setTranslationY(currentPos[1]);
            }
        });
        valueAnimator.start();

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRlRoot.removeView(circle);
                insertMsg();
            }
        });
    }

}
