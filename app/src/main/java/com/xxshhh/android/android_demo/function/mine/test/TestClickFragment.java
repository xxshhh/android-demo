package com.xxshhh.android.android_demo.function.mine.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xxshhh.android.android_demo.R;
import com.xxshhh.android.android_demo.base.fragment.BaseFragment;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 测试点击
 * Created by xxshhh on 2017/11/15.
 */
public class TestClickFragment extends BaseFragment {

    @BindView(R.id.btn_reset)
    Button mBtnReset;
    @BindView(R.id.tv_timer)
    TextView mTvTimer;
    @BindView(R.id.btn_click)
    Button mBtnClick;

    private long mStartTime;
    private long mEndTime;

    @Override
    protected int getLayoutResID() {
        return R.layout.test_fragment_click;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTimer();
            }
        });
        initTimer();
    }

    private void initTimer() {
        mBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEndTime = System.currentTimeMillis();
                long count = mEndTime - mStartTime;
                Toast.makeText(getContext(), "你的抢购用时：" + count + " ms", Toast.LENGTH_LONG).show();
                mBtnClick.setEnabled(false);
            }
        });

        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(10)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return 10 - aLong;
                    }
                })
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mBtnClick.setEnabled(false);
                        mBtnClick.setBackgroundColor(Color.GRAY);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mBtnClick.setBackgroundColor(Color.GREEN);
                        mStartTime = System.currentTimeMillis();
                        mBtnClick.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        mTvTimer.setText(String.valueOf(aLong));
                    }
                });
    }

}
