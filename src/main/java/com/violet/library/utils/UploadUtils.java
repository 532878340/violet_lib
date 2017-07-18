package com.violet.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.violet.library.manager.ConstantsManager;
import com.violet.library.manager.NetManager;
import com.violet.library.manager.SharedPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * description：okhttp文件上传工具
 * author：JimG on 16/10/12 11:18
 * e-mail：info@zijinqianbao.com
 */

public class UploadUtils{
    public static final String TAG = "UploadUtils";

    /**
     * 上传成功回调
     */
    public interface UploadCallBack{

        void uploadError(Call call,Exception e);

        void uploadResponse(String response);
    }

    /**
     * 文件上传
     * @param file
     * @param requestUrl
     */
    public static void uploadMultiFile(Context context,File file, String requestUrl, final UploadCallBack callBack) {
        if(file == null || TextUtils.isEmpty(requestUrl)){
            throw new IllegalArgumentException("Arguments can not be illegal,please try again");
        }

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .url(NetManager.getServerRoot() + requestUrl)
                .header(ConstantsManager.TOKEN,SharedPreferenceManager.getString(context,ConstantsManager.TOKEN))
                .header(ConstantsManager.UNIQUE_ID,PhoneUtils.getUniqueDeviceId())
                .post(requestBody)
                .build();

        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient  = httpBuilder
                //设置超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "uploadMultiFile() e=" + e);
                if(callBack != null){
                    callBack.uploadError(call,e);
                }
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String rspStr = response.body().string();
                Log.i(TAG, "uploadMultiFile() response=" + rspStr);
                if(callBack != null){
                    callBack.uploadResponse(rspStr);
                }
            }
        });
    }
}
