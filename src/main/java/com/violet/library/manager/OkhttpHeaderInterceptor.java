package com.violet.library.manager;

import com.violet.library.BaseApplication;
import com.violet.library.utils.PhoneUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * description：okhttp拦截器
 * author：JimG on 17/4/20 14:44
 * e-mail：info@zijinqianbao@qq.com
 */


public class OkhttpHeaderInterceptor implements Interceptor {
    private Map<String,String> headerMap;

    public OkhttpHeaderInterceptor() {
        initHeader();
    }

    void initHeader(){
        headerMap = new HashMap<>();
        headerMap.put(ConstantsManager.UNIQUE_ID, PhoneUtils.getUniqueDeviceId(BaseApplication.getInstance()));
        headerMap.put(ConstantsManager.TOKEN,SharedPreferenceManager.getString(BaseApplication.getInstance(),ConstantsManager.TOKEN));
        headerMap.put("Accept","application/json");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder reqBuilder = original.newBuilder();

        Headers.Builder headerBuild = original.headers().newBuilder();
        if(headerMap.size() > 0){
            Iterator iterator = headerMap.keySet().iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                headerBuild.add(key, headerMap.get(key));
            }
        }

        reqBuilder.headers(headerBuild.build());
        return chain.proceed(reqBuilder.build());
    }

    /**
     * 添加header参数
     * @param name
     * @param head
     */
    public void addHeader(String name,String head){
        headerMap.put(name,head);
    }

    /**
     * 添加header集合
     * @param headers
     */
    public void addHeaders(Map<String,String> headers){
        headerMap.putAll(headers);
    }
}
