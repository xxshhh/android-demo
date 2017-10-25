package com.xxshhh.android.android_demo.function.practice.animation.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.function.practice.animation.demo.animation.AnimationMsgManager;
import com.xxshhh.android.android_demo.function.practice.animation.demo.animation.AnimationRevealFrameLayout;

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

    private AnimationDemoAdapter mAdapter;

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
        mAdapter = new AnimationDemoAdapter(getContext());
        mRvMsgList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvMsgList.setItemAnimator(new DefaultItemAnimator());
        mRvMsgList.setHasFixedSize(true);
        mRvMsgList.setAdapter(mAdapter);
    }

    private void initSendButton() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMsg();
            }
        });

        mBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMsg();
            }
        });
    }

    private void insertMsg() {
        int size = mAdapter.getDataList().size();
        String msg = getString(R.string.place_text) + size;
        mAdapter.getDataList().add(msg);
        mAdapter.notifyItemInserted(size);
        mRvMsgList.scrollToPosition(size);

        startAnimation();
    }

    private void removeMsg() {
        int size = mAdapter.getDataList().size();
        if (size > 0) {
            mAdapter.getDataList().remove(size - 1);
            mAdapter.notifyItemRemoved(size - 1);
        }
    }

    private void startAnimation() {
        int[] startLoc = getStartLoc();
        int[] toLoc = getToLoc();

        View itemView = findLastVisibleItemView();

        if (itemView != null) {
            itemView.setVisibility(View.GONE);
            AnimationRevealFrameLayout revealFrameLayout = (AnimationRevealFrameLayout) itemView.findViewById(R.id.cfl_container);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_avatar);
            AnimationMsgManager.startAnimation(getContext(), mRlRoot,
                    startLoc[0], startLoc[1], toLoc[0], toLoc[1],
                    itemView, revealFrameLayout, imageView);
        }
    }

    private int[] getStartLoc() {
        int[] startLoc = new int[2];
        mBtnAdd.getLocationInWindow(startLoc);
        startLoc[0] += mBtnAdd.getWidth() / 2;
        startLoc[1] += mBtnAdd.getHeight() / 2;
        return startLoc;
    }

    private int[] getToLoc() {
        int[] toLoc = new int[2];

        View itemView = findLastVisibleItemView();
        if (itemView != null) {
            AnimationRevealFrameLayout revealFrameLayout = (AnimationRevealFrameLayout) itemView.findViewById(R.id.cfl_container);
            revealFrameLayout.getLocationInWindow(toLoc);
            toLoc[0] += revealFrameLayout.getWidth() / 2;
            toLoc[1] += revealFrameLayout.getHeight() / 2;
        }
        return toLoc;
    }

    private View findLastVisibleItemView() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRvMsgList.getLayoutManager();
        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
        return linearLayoutManager.findViewByPosition(lastPosition);
    }

}
