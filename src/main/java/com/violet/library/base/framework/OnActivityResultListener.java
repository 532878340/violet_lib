package com.violet.library.base.framework;

import android.content.Intent;

/**
 * description：fragment处理activity的result
 * author：JimG on 2017/6/14 14:38
 * e-mail：info@zijinqianbao@qq.com
 */

public interface OnActivityResultListener {
    void onActivityResultHandler(int requestCode, int resultCode, Intent data);
}
