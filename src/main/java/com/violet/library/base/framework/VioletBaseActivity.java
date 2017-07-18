package com.violet.library.base.framework;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.violet.library.R;
import com.violet.library.manager.ActivityManager;
import com.violet.library.manager.ConfigsManager;
import com.violet.library.manager.ConstantsManager;
import com.violet.library.manager.LeakCanaryManager;
import com.violet.library.utils.PhoneUtils;

/**
 * Created by perley on 16/2/17.
 */
public class VioletBaseActivity extends FragmentActivity {
    protected Activity mThis = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this, getClass());

        if (enableTranslucentStatus()) {
            translucentStatus();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //调试模式,开启内存检测
        if(ConfigsManager.DEBUG_ENABLE){
            LeakCanaryManager.getInstance().getRefWatcher().watch(this);
        }
    }

    private void translucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(getStatusBarTintResource());//通知栏所需颜色
        }

        // 5.0以上的版本处理
        final Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

    }

    /**
     * 是否启用“沉浸式”状态栏，默认开启
     * @return
     */
    protected boolean enableTranslucentStatus() {
        String translucent = PhoneUtils.getMetaValue(this, ConstantsManager.META_DATA_TRANSLUCENT);
        return ConstantsManager.YES.equalsIgnoreCase(translucent);
    }

    /**
     * 是否设置Activity的layout为fitsSystemWindows
     * @return
     */
    protected boolean isFitSystemWindows() {
        return enableTranslucentStatus();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        if (isFitSystemWindows()) {
            // 设置所有布局为fitsSystemWindows
            ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
            if (content != null && content.getChildCount() > 0) {
                content.getChildAt(0).setFitsSystemWindows(true);
            }
        }
    }

    /**
     * 状态栏颜色值
     * @return
     */
    protected int getStatusBarTintResource() {
        return R.color.status_bar;
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
