package com.violet.library.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.violet.library.utils.PhoneUtils;

/**
 * description：
 * author：JimG on 17/4/6 15:12
 * e-mail：info@zijinqianbao@qq.com
 */


public class ExToolBar extends Toolbar{
    public ExToolBar(Context context) {
        super(context);
        initUI();
    }

    public ExToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public ExToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setPadding(0, PhoneUtils.getStatusBarHeight(getContext()), 0, 0);
        }
    }
}
