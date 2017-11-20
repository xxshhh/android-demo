package com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xxshhh.android.android_demo.base.adapter.BaseAdapter;
import com.xxshhh.android.android_demo.function.practice.animation.demo.msg.list.item.impl.MsgAnimationItemView;

/**
 * 消息动画 adapter
 * Created by xxshhh on 2017/11/15.
 */
public class MsgAnimationAdapter extends BaseAdapter<String> {
    public MsgAnimationAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = new MsgAnimationItemView(mContext);
        return new MsgAnimationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        View itemView = holder.itemView;
        if (itemView instanceof MsgAnimationItemView) {
            ((MsgAnimationItemView) itemView).setData(mDataList.get(position));
        }
    }

    private class MsgAnimationViewHolder extends RecyclerView.ViewHolder {
        private MsgAnimationViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.itemView instanceof MsgAnimationItemView) {
            ((MsgAnimationItemView) holder.itemView).recycled();
        }
    }
}
