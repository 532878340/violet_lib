package com.violet.library.base.framework;

import com.violet.library.manager.L;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * description：网络请求响应
 * author：JimG on 16/10/11 14:35
 * e-mail：info@zijinqianbao.com
 */

public class JSONResponse extends JSONCallBack{
    private ResponseListener listener;
    private RequestCall requestCall;

    @Override
    public void onError(Call call, Exception e, int id) {
        L.d("OkHttpResponse: " + e.getMessage());
        if(listener != null) listener.onFailed(call,e,id);

    }

    @Override
    public void onResponse(JSONObject response, int id) {
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

    public interface ResponseListener{
        /**
         * 响应成功回调
         * @param response
         * @param id
         */
        void onSuccess(RequestCall requestCall,JSONObject response,int id);

        /**
         * 响应失败回调
         * @param call
         * @param e
         * @param id
         */
        void onFailed(Call call,Exception e, int id);
    }
}
