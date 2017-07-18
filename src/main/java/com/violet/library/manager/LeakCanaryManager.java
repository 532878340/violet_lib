package com.violet.library.manager;

import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.violet.library.BaseApplication;

/**
 * description：内存检测工具
 * author：JimG on 16/11/11 09:37
 * e-mail：info@zijinqianbao@qq.com
 */

public class LeakCanaryManager{
    private RefWatcher mRefWatcher;

    private LeakCanaryManager(){}

    private static final class LeakHolder{
        private static final LeakCanaryManager INSTANCE = new LeakCanaryManager();
    }

    public static LeakCanaryManager getInstance(){
        return LeakHolder.INSTANCE;
    }

    /**
     * 初始化leakcanary
     * @param ctx
     */
    public void initLeak(Context ctx){
        if(LeakCanary.isInAnalyzerProcess(ctx)){
            return;
        }

        mRefWatcher = LeakCanary.install(BaseApplication.getInstance());
    }

    /**
     * 获取Leak检测类
     * @return
     */
    public RefWatcher getRefWatcher(){
        return mRefWatcher;
    }
}
