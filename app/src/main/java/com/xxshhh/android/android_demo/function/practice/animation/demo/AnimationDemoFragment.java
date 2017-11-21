package com.xxshhh.android.android_demo.function.practice.animation.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.callback.IMsgAnimationCallback;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.adapter.MsgAnimationAdapter;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.recyclerView.MsgAnimationItemAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Demo动画界面
 * Created by xxshhh on 2017/9/19.
 */
public class AnimationDemoFragment extends BaseFragment
        implements IMsgAnimationCallback {

    @BindView(R.id.btn_add)
    Button mBtnAdd;
    @BindView(R.id.btn_multi_add)
    Button mBtnMultiAdd;
    @BindView(R.id.btn_remove)
    Button mBtnRemove;
    @BindView(R.id.btn_clean)
    Button mBtnClean;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.rv_msg_list)
    RecyclerView mRvMsgList;
    @BindView(R.id.rl_content)
    RelativeLayout mRlContent;
    @BindView(R.id.rl_root)
    RelativeLayout mRlRoot;

    private MsgAnimationAdapter mAdapter;

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
        initActionButton();
    }

    private void initMsgList() {
        mAdapter = new MsgAnimationAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setRecycleChildrenOnDetach(true);
        mRvMsgList.setLayoutManager(linearLayoutManager);
        mRvMsgList.setItemAnimator(new MsgAnimationItemAnimator());
        mRvMsgList.setHasFixedSize(true);
        mRvMsgList.setAdapter(mAdapter);
    }

    private void initActionButton() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMsg();
            }
        });

        mBtnMultiAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMultiMsg(9);
            }
        });

        mBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMsg();
            }
        });

        mBtnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanMsg();
            }
        });
    }

    private void insertMsg() {
        insertMultiMsg(1);
    }

    private void insertMultiMsg(int count) {
        int size = mAdapter.getItemCount();
        List<String> msgs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int position = size + i;
            msgs.add(getString(R.string.place_text) + position);
        }
        mAdapter.getDataList().addAll(msgs);
        mAdapter.notifyItemRangeInserted(size, count);
        mRvMsgList.scrollToPosition(size + count - 1);
    }

    private void removeMsg() {
        int size = mAdapter.getDataList().size();
        if (size > 0) {
            mAdapter.getDataList().remove(size - 1);
            mAdapter.notifyItemRemoved(size - 1);
        }
    }

    private void cleanMsg() {
        mAdapter.getDataList().clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public ViewGroup getAnimationParentView() {
        return mRlContent;
    }

    @Override
    public View getAnimationBottomView() {
        return mLlBottom;
    }
}
