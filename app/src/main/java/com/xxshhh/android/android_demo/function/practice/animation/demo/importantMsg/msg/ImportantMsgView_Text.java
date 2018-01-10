package com.xxshhh.android.android_demo.function.practice.animation.demo.importantMsg.msg;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.xxshhh.android.android_demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文本重要消息View
 * Created by xwh on 2018/1/5.
 */
public class ImportantMsgView_Text extends MaxHeightScrollView {

    @BindView(R.id.tv_content)
    TextView mTvContent;

    public ImportantMsgView_Text(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.animation_view_important_msg_text, this, true);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
    }

    public void setData(Object data) {
        // ...
        mTvContent.setText("2\n22\n222\n2222\n22222\n222222\n2222222\n22222222\n222222222\n2222222222\n22222222222\n222222222222\n2222222222222\n22222222222222\n222222222222222\n2222222222222222");
    }
}
