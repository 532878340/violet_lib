package com.violet.library.utils;

import android.os.CountDownTimer;

/**
 * Description :倒计时工具
 * Created by Jim on 2016/11/13.
 * E-mail : violetskinjim@outlook.com
 */
public class CountDownHelper extends CountDownTimer{
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownHelper(long millisInFuture, long countDownInterval, CountDownListener lis) {
        super(millisInFuture, countDownInterval);
        this._lis = lis;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(_lis != null){
            _lis.onTick(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if(_lis != null){
            _lis.onFinish();
        }
    }

    private CountDownListener _lis;

    public void setCountDownListener(CountDownListener lis){
        _lis = lis;
    }

    public interface CountDownListener{
        void onTick(long millisUntilFinished);
        void onFinish();
    }
}
