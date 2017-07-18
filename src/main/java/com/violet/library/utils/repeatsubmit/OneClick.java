package com.violet.library.utils.repeatsubmit;

/**
 * description：
 * author：JimG on 2017/6/27 13:44
 * e-mail：info@zijinqianbao@qq.com
 */


public class OneClick {
    private String methodName;
    private static final int CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public OneClick(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean check() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return false;
        } else {
            return true;
        }
    }
}
