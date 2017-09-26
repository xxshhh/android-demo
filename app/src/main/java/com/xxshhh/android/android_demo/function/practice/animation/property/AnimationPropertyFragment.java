package com.xxshhh.android.android_demo.function.practice.animation.property;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;

/**
 * 属性动画界面
 * Created by xxshhh on 2017/9/22.
 */
public class AnimationPropertyFragment extends BaseFragment {

    @Override
    protected int getLayoutResID() {
        return R.layout.animation_fragment_property;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {

    }

}
