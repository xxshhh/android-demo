package com.xxshhh.android.android_demo.function.practice.animation.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.adapter.BaseAdapter;

/**
 * Demo动画 adapter
 */
public class AnimationDemoAdapter extends BaseAdapter<String> {

    public AnimationDemoAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.animation_demo_msg_item, parent, false);
        return new AnimationDemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String data = mDataList.get(position);
        if (holder instanceof AnimationDemoViewHolder) {
            ((AnimationDemoViewHolder) holder).setData(data);
        }
    }

}
