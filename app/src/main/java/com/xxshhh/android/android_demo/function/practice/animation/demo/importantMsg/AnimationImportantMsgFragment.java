package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.container.IImportantMsgMainContainer;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.container.impl.ImportantMsgMainContainer;

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
        IImportantMsgMainContainer container = new ImportantMsgMainContainer(getActivity());
        container.addAvatarView();
    }
}
