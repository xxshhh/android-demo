package com.xxshhh.android.android_demo.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 基类 adapter
 * Created by xxshhh on 2017/9/20.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter {

    protected Context mContext;
    protected List<T> mDataList;

    public BaseAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<>();
    }

    public void setDataList(List<T> dataList) {
        mDataList.clear();
        if (dataList != null) {
            mDataList.addAll(dataList);
        }
    }

    public List<T> getDataList() {
        return mDataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

}
