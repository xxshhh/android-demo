package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.container.IImportantMsgChatContainer;
import com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.container.impl.ImportantMsgChatContainer;

import butterknife.BindView;

/**
 * 重要消息动画界面2
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

    private IImportantMsgChatContainer mContainer;

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
        if (mContainer == null) {
            mContainer = new ImportantMsgChatContainer(getActivity());
        }
        mContainer.showDialogView("", mIvLogo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mContainer != null) {
            mContainer.destroy();
        }
    }
}
