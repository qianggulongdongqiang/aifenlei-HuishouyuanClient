package com.arcfun.aifenx.utils;

import android.os.CountDownTimer;
import android.widget.Button;

public class CountDownTimerUtils {
    public interface OnFinishListener {
        public void finish();
    }

    private Button mView;
    private CountDownTimer mTimer;
    private OnFinishListener listener;
    private int mFinishRes;
    private String mSuffix;

    public CountDownTimerUtils(Button view, long millisInFuture,
            long countDownInterval, int resId, String suffix) {
        this.mView = view;
        this.mFinishRes = resId;
        this.mSuffix = suffix;
        mTimer = new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long time) {
                mView.setEnabled(false);
                mView.setText(time / 1000 + mSuffix);
            }

            @Override
            public void onFinish() {
                mView.setEnabled(true);
                mView.setText(mFinishRes);
                if (listener != null) {
                    listener.finish();
                }
            }
        };
    }

    public void start() {
        mTimer.start();
    }

    public void stop() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mView.setEnabled(true);
        mView.setText(mFinishRes);
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.listener = listener;
    }
}