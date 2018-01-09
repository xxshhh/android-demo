package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.msg;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxshhh.android.android_demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文本重要消息View
 * Created by xwh on 2018/1/5.
 */
public class ImportantMsgView_Text extends LinearLayout {

    @BindView(R.id.tv_content)
    TextView mTvContent;

    public ImportantMsgView_Text(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.animation_view_important_msg_text, this, true);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance()); // 设置文本支持滚动
    }

    public void setData(Object data) {
        // ...
    }
}
