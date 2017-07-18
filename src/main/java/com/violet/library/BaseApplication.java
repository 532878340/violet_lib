package com.violet.library;

import android.app.Application;

import com.violet.library.manager.ConfigsManager;

/**
 * description：application对象类
 * author：JimG on 16/10/8 17:10
 * e-mail：info@zijinqianbao.com
 */

public class BaseApplication extends Application {
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //初始化平台配置信息
        ConfigsManager.getInstance().initConfigs(this);
    }

    public static BaseApplication getInstance(){
        return mInstance;
    }
}
