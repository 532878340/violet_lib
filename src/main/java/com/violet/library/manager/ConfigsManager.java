package com.violet.library.manager;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.violet.library.BaseApplication;
import com.violet.library.base.framework.HP_Fragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * description：配置管理类
 * author：JimG on 16/10/8 17:42
 * e-mail：info@zijinqianbao.com
 */

public class ConfigsManager {

    public static final String TAG = "ConfigsManager";

    /**********************  项目域名 *************************/

    /**
     * 本地链接
     */
    public static final String ROOT_DEBUG = "http://106.14.112.233/app/"; //http://192.168.10.187:8080/app/
    /**
     * 测试域名
     */
    public static final String ROOT_TEST = "http://106.14.112.233/app/"; //http://106.14.112.233/app/
    /**
     * 正式站点
     */
    public static final String ROOT_RELEASE = "http://app.zijinqianbao.com/app/"; //http://app.zijinqianbao.com/app/

    /**
     * 基础路径
     */
    public static final String URL_BASE = "https://www.zijinqianbao.com/";

    /**
     * des加密盐
     */
    public static final String CREDENTIALS_SALT = "12345678";


    /**********************  UI配置  *************************/
    /**
     * 网络请求是否自动打开 {@link HP_Fragment#initRequest()}
     */
    public static final boolean AUTO_REQUEST = true;

    /**
     * 获取实时金价周期(1分钟)
     */
    public static final long GOLD_PRICE_CYCLE = 1 * 60 * 1000;

    /**
     * 图片缓存周期,单位(天) ,-1表示不清除图片缓存
     */
    public static final long CACHE_TIME = -1;

    /**
     * APK更新对应的网络路径
     */
    public static String APK_DOWNLOAD = "http://gdown.baidu.com/data/wisegame/df65a597122796a4/weixin_821.apk";

    /******************* 调试配置  ********************/
    /**
     * 是否开启调试模式
     */
    public static final boolean DEBUG_ENABLE = false;

    /******************* OKHttp初始化配置  ********************/
    /**
     * 是否启用cookie持久化
     */
    public static final boolean COOKIE_PESISTENCE = false;

    public static final int READ_TIMEOUT = 20;
    public static final int WRITE_TIMEOUT = 20;
    public static final int CONNECT_TIMEOUT = 20;

    /******************* 第三方分享初始化配置  ********************/

    /******************* 推送功能配置  ********************/

    private ConfigsManager(){}

    private static class ConfigHolder{
        private static final ConfigsManager INSTANCE = new ConfigsManager();
    }

    public static ConfigsManager getInstance(){
        return ConfigHolder.INSTANCE;
    }

    /**
     * 初始化配置信息
     */
    public void initConfigs(Context ctx){
        CrashHandlerManager.getInstance(ctx).startCatching();//异常捕获

        initOkhttp(ctx);

//        cacheManager(ctx);

        LeakCanaryManager.getInstance().initLeak(ctx);

        initImageLoader(ctx);
    }

    /**
     * 初始化okhttp
     */
    private void initOkhttp(Context ctx){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if(COOKIE_PESISTENCE){//cookie持久化
            builder.cookieJar(new CookieJarImpl(new PersistentCookieStore(ctx)));
        }


        //配置超时时间
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);

        //设置可访问所有的https网站 1、证书的inputstream 2、本地证书的inputstream 3、本地证书的密码
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null,null,null);
        builder.sslSocketFactory(sslParams.sSLSocketFactory,sslParams.trustManager);

        OkHttpUtils.initClient(builder.build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0权限问题
            StrictMode.VmPolicy.Builder vmBuilder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(vmBuilder.build());
        }
    }

    /**
     * 缓存管理,是否需要图片清理缓存
     * {@link ConfigsManager#CACHE_TIME}
     */
    private void cacheManager(Context ctx){
        long preCache = SharedPreferenceManager.getLong(ctx, ConstantsManager.CACHE_FLAG);
        if(ConfigsManager.CACHE_TIME != -1 && Math.abs(System.currentTimeMillis() - preCache) > 24 * 3600 * 1000 * ConfigsManager.CACHE_TIME){
            GlideManager.clearGlideCache(BaseApplication.getInstance());
            SharedPreferenceManager.setLong(ctx,ConstantsManager.CACHE_FLAG,System.currentTimeMillis());
        }
    }

    /**
     * 初始化imageloader
     * @param context
     * @Description:
     */
    public void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                //.writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
