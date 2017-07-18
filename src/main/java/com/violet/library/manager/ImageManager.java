package com.violet.library.manager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.violet.library.R;


public class ImageManager {
    /**
     * 获取图片加载
     */
    public static ImageLoader getInstance() {
        return ImageLoader.getInstance();
    }

    /**
     * 新闻图片缓存设置
     */

    private static DisplayImageOptions newsHeadOptions;

    public static DisplayImageOptions getNewsHeadOptions() {
        if (newsHeadOptions == null) {
            newsHeadOptions = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.icon_default)
                    .showImageOnFail(R.drawable.icon_default)
                    .showImageOnLoading(R.drawable.icon_default)
                    .cacheInMemory(true) // 缓存内存
                    .cacheOnDisc(true)// 缓存文件
                    .build();
        }
        return newsHeadOptions;
    }
}