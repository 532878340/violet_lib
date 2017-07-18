package com.violet.library.base.framework;

import com.violet.library.manager.L;
import com.zhy.http.okhttp.request.RequestCall;

import okhttp3.Call;

/**
 * description：网络请求响应
 * author：JimG on 16/10/9 15:04
 * e-mail：info@zijinqianbao.com
 */

public class OkHttpResponse<T extends ParentEntity> extends BeanCallBack<T>{
    private ResponseListener listener;
    private RequestCall requestCall;

    public OkHttpResponse(T type){
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        L.d("OkHttpResponse: " + e.getMessage());
        if(listener != null) listener.onFailed(call,e,id);
    }

    @Override
    public void onResponse(T response, int id) {
        L.d("OkHttpResponse: " + response);

        if(listener != null){
            listener.onSuccess(requestCall,response,id);
        }
    }

    public void setRequestCall(RequestCall requestCall){
        this.requestCall = requestCall;
    }

    /**
     * 设置回调监听
     * @param l
     */
    public void setResponseListener(ResponseListener l){
        listener = l;
    }

    public interface ResponseListener<T>{
        /**
         * 响应成功回调
         * @param response
         * @param id
         */
        void onSuccess(RequestCall requestCall,T response,int id);

        /**
         * 响应失败回调
         * @param call
         * @param e
         * @param id
         */
        void onFailed(Call call,Exception e, int id);
    }
}
