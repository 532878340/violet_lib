package com.violet.library.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.violet.library.BaseApplication;
import com.violet.library.utils.PhoneUtils;
import com.violet.library.utils.StringUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * description：网络请求管理
 * author：JimG on 16/10/8 15:15
 * e-mail：info@zijinqianbao.com
 */

public class NetManager {
    /**************************网络配置*****************************/
    private enum Mode{
        DEBUG , TEST , RELEASE
    }

    private static Mode USE_SERVER_MODE;

    static{
        final Context context = BaseApplication.getInstance();
        USE_SERVER_MODE = Enum.valueOf(Mode.class, PhoneUtils.getMetaValue(context, "server_mode"));
    }

    /**
     * 获取服务器BaseUrl
     * @return baseUrl
     */
    public static String getServerRoot(){
        if (USE_SERVER_MODE == Mode.DEBUG) {
            return ConfigsManager.ROOT_DEBUG;
        } else if (USE_SERVER_MODE == Mode.TEST) {
            return ConfigsManager.ROOT_TEST;
        } else {
            return ConfigsManager.ROOT_RELEASE;
        }
    }

    /**************************网络状态监听**************************/
    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    /**
     * 获取网络状态标识
     * @param context
     * @return
     */
    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        }
        return NETWORK_NONE;
    }

    /**
     * 网络状态发生改变回调
     */
    public interface NetEvent{
        void onNetChange(int netStatus);
    }



    /**************************网络请求*****************************/
    /**
     * 网络请求方式
     */
    public enum METHOD{
        GET,POST
    }

    /**
     * 初始化构建请求参数,默认请求方法为post
     * @param constants
     * @return
     */
    public static Map<String,String> getParameters(final String constants){
        return getParameters(constants, METHOD.GET);
    }

    /**
     * 初始化构建请求参数
     * @param constants
     * @param method
     * @return
     */
    public static Map<String,String> getParameters(@NonNull final String constants, METHOD method){
        Map<String,String> param = new HashMap<>();
        if(TextUtils.isEmpty(constants)){
            throw new IllegalArgumentException("action can not be empty,please check arguments");
        }
        param.put("action",constants);
        param.put("method",method.toString());
        return param;
    }

    /**
     * 构建OkHttpRequestBuilder参数(不带header信息)
     * @param builder
     * @param parameters
     * @return
     */
    public static OkHttpRequestBuilder buildRequestParams(Context ctx,OkHttpRequestBuilder builder, Map<String,String> parameters, Object tag){
        return buildRequestParams(ctx,builder, parameters,null, tag);
    }

    /**
     * 构建OkHttpRequestBuilder参数(带header信息)
     * @param builder
     * @param parameters
     * @param headers
     * @return
     */
    public static OkHttpRequestBuilder buildRequestParams(@NonNull Context ctx,OkHttpRequestBuilder builder, Map<String,String> parameters, Map<String,String> headers,@NonNull Object tag){
        if(parameters.containsKey("action")){
            builder.url(NetManager.getServerRoot() + parameters.get("action"));
        }

        parameters.remove("action");
        parameters.remove("method");

        if(headers == null) headers = new HashMap<>();
        headers.put(ConstantsManager.TOKEN,SharedPreferenceManager.getString(ctx,ConstantsManager.TOKEN));
        headers.put(ConstantsManager.UNIQUE_ID,PhoneUtils.getUniqueDeviceId());//设备唯一id，设备发生改变则需要重新登录

        builder.headers(headers).tag(tag);

        if(builder instanceof PostFormBuilder){//post请求
            ((PostFormBuilder) builder).params(parameters);
        }else{//默认为Get请求
            ((GetBuilder) builder).params(parameters);
        }

        return builder;
    }

    /**
     * 静态集合，用于存储请求的url+param，防重复提交处理
     */
    public static ArrayList<String> requestSet = new ArrayList<>();
}
