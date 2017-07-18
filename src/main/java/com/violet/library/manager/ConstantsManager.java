package com.violet.library.manager;

/**
 * description：APP通用常量
 * author：JimG on 16/10/12 11:38
 * e-mail：info@zijinqianbao.com
 */

public class ConstantsManager {
    /**
     * 服务端版本
     */
    public static final String SERVER_VERSION = "user";

    /**
     * buildType配置,清单文件字段
     */
    public static final String SERVER_MODE = "server_mode";

    /**
     * 定义上传请求的tag
     */
    public static final String TAG_UPLOAD = "UPLOAD";

    /**
     * 定义下载请求的tag
     */
    public static final String TAG_DOWNLOAD = "DOWNLOAD";

    /**
     * 配置“沉浸式”状态栏的meta-data的key
     */
    public static final String META_DATA_TRANSLUCENT = "translucent_status";

    /**
     * listview分页标识
     */
    public static final int SINGLEPAGE_LENGTH = 15;

    /**
     * 图片缓存标识,记录缓存周期
     */
    public static final String CACHE_FLAG = "cache_flag";

    /**
     * 图片缓存目录名
     */
    public static final String CACHE_DIR = "/zjqb/cache_dir/";

    /**
     * 文件下载目录名
     */
    public static final String APP_DOWNLOAD_DIR = "/zjqb/download/";

    /**
     * 文件异常目录名
     */
    public static final String APP_CRASH_DIR = "/zjqb/crash/";

    /**
     * 保存密码时间标识,记录密码周期
     */
    public static final String SAVE_PWD_FLAG = "save_pwd_flag";

    /**
     * 上次登录的用户名
     */
    public static final String LAST_USER_NAME = "last_user_name";

    /**
     * SharedPreference保存token字段键名
     */
    public static final String TOKEN = "token";
    /**
     * 设备唯一id键名
     */
    public static final String UNIQUE_ID = "imei";
    /**
     * SharedPreference保存当前金价字段键名
     */
    public static final String GOLD_PRICE = "gold_price";
    /**
     * 开关常量(开启）
     */
    public static final String YES = "YES";
    /**
     * 开关常量(开启）
     */
    public static final String NO = "NO";
    /**
     * 标志登录名，用于记住登录名
     */
    public static final String CONST_LOGIN_NAME = "login_name";
    /**
     * 标志登录密码，用于记住登录密码
     */
    public static final String CONST_LOGIN_PWD = "login_pwd";
    /**
     * 空handler msg
     */
    public static final int EMPTY_MSG = 0;
    /**
     * handler处理延时
     */
    public static final long HANDLER_DELAY = 3000;
    /**
     * 验证码倒计时
     */
    public static final long DELAY_VERIFY_CODE = 60 * 1000;
}
