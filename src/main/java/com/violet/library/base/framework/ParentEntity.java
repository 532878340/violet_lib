package com.violet.library.base.framework;

/**
 * description：Bean基类
 * author：JimG on 16/10/9 16:12
 * e-mail：info@zijinqianbao.com
 */

public class ParentEntity {
    /**
     * 网络请求标识,可相当于tag,用于区分不同的请求
     */
    public final String identifier = getClass().getSimpleName();

    /**
     * 正确返回
     */
    public static final String OK = "000000";

    /**
     * 未完善个人信息
     */
    public static final String OK_WITHOUT_INFO = "2000078";

    /**
     * 表示需要跳转登录界面
     */
    public static final String CODE_LOGIN = "200001";

    /**
     * 提示消息
     */
    public String description;

    /**
     * 错误返回码
     */
    public String code;

    /**
     * 是否成功返回
     *
     * @return
     */
    public boolean isSuccessful() {
        return code.equals(OK) || code.equals(OK_WITHOUT_INFO);
    }

    /**
     * 判断是否需要跳转登录
     * @return
     */
    public boolean go2Login(){
        return code == CODE_LOGIN;
    }


    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ParentEntity{" + "description='" + description + '\'' + ", code=" + code + '}';
    }
}
