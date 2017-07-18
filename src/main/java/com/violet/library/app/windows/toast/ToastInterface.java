package com.violet.library.app.windows.toast;

/**
 * description：Toast接口
 * author：JimG on 16/10/10 11:49
 * e-mail：info@zijinqianbao.com
 */

public interface ToastInterface {
    /**
     * 成功
     * @param msg
     */
    void success(CharSequence msg);

    /**
     * 失败
     * @param msg
     */
    void failed(CharSequence msg);

    /**
     * 警告
     * @param msg
     */
    void warning(CharSequence msg);
}
