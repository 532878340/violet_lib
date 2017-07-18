package com.violet.library.base;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * description：handle基类,处理内存泄漏
 * author：JimG on 16/11/14 11:48
 * e-mail：info@zijinqianbao@qq.com
 */

public abstract class BaseHandler extends Handler {
    protected WeakReference<Activity> mWeakReference;

    public BaseHandler(Activity activity){
        mWeakReference = new WeakReference<>(activity);
    }

    @Override
    public final void handleMessage(Message msg) {
        final Activity activity = mWeakReference.get();
        if(activity != null){
            handMessage(msg);
        }
    }

    public abstract void handMessage(Message msg);
}
