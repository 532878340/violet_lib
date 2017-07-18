package com.violet.library.base.framework;

import android.text.TextUtils;

import com.violet.library.BaseApplication;
import com.violet.library.manager.ConstantsManager;
import com.violet.library.manager.L;
import com.violet.library.manager.SharedPreferenceManager;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp3.Response;

/**
 * description：返回JSONObject
 * author：JimG on 16/10/11 14:14
 * e-mail：info@zijinqianbao.com
 */

public abstract class JSONCallBack extends Callback<JSONObject>{
    @Override
    public JSONObject parseNetworkResponse(Response response, int id) throws Exception {
        String token = response.header(ConstantsManager.TOKEN);
        String responseStr = response.body().string();

        if(!TextUtils.isEmpty(token)){
            SharedPreferenceManager.setString(BaseApplication.getInstance(),ConstantsManager.TOKEN,token);
        }

        L.d("请求响应：" + responseStr);

        return new JSONObject(responseStr);
    }
}
