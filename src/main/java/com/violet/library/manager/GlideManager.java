package com.violet.library.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.violet.library.BaseApplication;
import com.violet.library.utils.PhoneUtils;
import com.violet.library.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

import okhttp3.CacheControl;
import okhttp3.Call;

/**
 * description：图片加载工具 Glide
 * author：JimG on 16/10/12 14:17
 * e-mail：info@zijinqianbao.com
 */

public class GlideManager {

    public static void displayImg(Context ctx,String imgUrl, ImageView imgView){
        configGlide(Glide.with(ctx).load(StringUtils.parseImageUrl(imgUrl)),imgView);
    }

    public static void displayImg(Context ctx,Uri uri, ImageView imgView){
        configGlide(Glide.with(ctx).load(uri),imgView);
    }

    public static void displayImg(Context ctx,URL url, ImageView imgView){
        configGlide(Glide.with(ctx).load(url),imgView);
    }

    public static void displayImg(Context ctx,File file, ImageView imgView){
        configGlide(Glide.with(ctx).load(file),imgView);
    }

    /**
     * 根据load方法返回DrawableTypeRequest对象,进行基础配置
     * @param request
     * @param imgView
     */
    private static void configGlide(DrawableTypeRequest request,ImageView imgView){
        if(request != null && imgView != null){
            int versionCode = Integer.valueOf(PhoneUtils.getVersionCode(BaseApplication.getInstance()));

            request.placeholder(android.R.drawable.stat_notify_error)
                    .error(android.R.drawable.stat_notify_error)
                    .centerCrop()
                    .signature(new IntegerVersionSignature(versionCode))
                    .into(imgView);
        }
    }

    /**
     * 显示okhttp图片
     * @param url
     * @param imgView
     */
    public static void displayImgWithOkhttp(String url,final ImageView imgView){
        RequestCall requestCall = OkHttpUtils.get().url(url).build();
        requestCall.execute(new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if(imgView != null) imgView.setImageResource(android.R.drawable.stat_notify_error);
            }

            @Override
            public void onResponse(Bitmap response, int id) {
                if(imgView != null) imgView.setImageBitmap(response);
            }
        });
        requestCall.getRequest().newBuilder().cacheControl(CacheControl.FORCE_NETWORK);
    }

    /**
     * 清理Glide磁盘缓存
     * {@link ConfigsManager#cacheManager(Context)}}
     */
    public static void clearGlideCache(final Context ctx){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(ctx).clearDiskCache();
            }
        }).start();
        Glide.get(ctx).clearMemory();
    }

    /**
     * 设置缓存的版本标识,实现软删除(版本发生改变,会自动清除缓存)
     */
    static class IntegerVersionSignature implements Key {
        private int currentVersion;

        public IntegerVersionSignature(int currentVersion){
            this.currentVersion = currentVersion;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof IntegerVersionSignature){
                IntegerVersionSignature other = (IntegerVersionSignature) obj;
                return currentVersion == other.currentVersion;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return currentVersion;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
            messageDigest.update(ByteBuffer.allocate(Integer.SIZE).putInt(currentVersion).array());
        }
    }
}
