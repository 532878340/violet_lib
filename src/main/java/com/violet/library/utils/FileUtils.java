package com.violet.library.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

/**
 * 文件操作工具类，包括存储卡相关的操作
 * Created by perley on 16/4/7.
 */
public class FileUtils {

    /**
     * Check if the primary "external" storage device is available.
     */
    public static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param path 文件路径
     * @return 可用的存储空间
     */
    @TargetApi(VERSION_CODES.GINGERBREAD)
    public static long getUsableSpace(File path) {
        if (path == null) {
            return -1;
        }
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        } else {
            if (!path.exists()) {
                return 0;
            } else {
                final StatFs stats = new StatFs(path.getPath());
                return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
            }
        }
    }


    /**
     * 保存图片到文件
     */
    public static boolean saveBitmap2File(Bitmap bmp, String filename) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 通知MediaScanner扫描，以便保存的多媒体文件被系统发现
     */
    public static void notifyScanMedia(Context context, File target) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
            Uri.fromFile(target)));
    }


}
