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
                // 播放动画
                startAnimation();
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
    }

    private void removeMsg() {
        int size = mAdapter.getDataList().size();
        if (size > 0) {
            mAdapter.getDataList().remove(size - 1);
            mAdapter.notifyItemRemoved(size - 1);
        }
    }

    private void startAnimation() {
        mRvMsgList.post(new Runnable() {
            @Override
            public void run() {
                View itemView = findLastVisibleItemView();
                if (itemView != null) {
                    itemView.setVisibility(View.GONE);
                    AnimationRevealFrameLayout revealFrameLayout = (AnimationRevealFrameLayout) itemView.findViewById(R.id.cfl_container);
                    ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_avatar);
                    AnimationMsgManager.startAnimation(getContext(), mRlRoot, mBtnAdd, revealFrameLayout,
                            itemView, revealFrameLayout, imageView);
                }
            }
        });
    }

    private View findLastVisibleItemView() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRvMsgList.getLayoutManager();
        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
        return linearLayoutManager.findViewByPosition(lastPosition);
    }

}
