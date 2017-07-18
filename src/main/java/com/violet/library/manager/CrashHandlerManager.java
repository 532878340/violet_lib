package com.violet.library.manager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.violet.library.utils.PhoneUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description：异常处理
 * author：JimG on 16/11/9 11:45
 * e-mail：info@zijinqianbao@qq.com
 */

public class CrashHandlerManager implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandlerManager";

    /**
     * 系统异常处理器
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * 是否调试模式
     */
    private boolean debug = true;
    /**
     * 需要用到的上下文
     */
    private Context ctx;
    /**
     * 错误时提示信息
     */
    private String tip = "I am sorry.";
    /**
     * 文件名
     */
    private String fileName = "crash_log";
    /**
     * 文件后缀
     */
    private String suffix = ".trace";

    private CrashHandlerManager(Context ctx){
        this.ctx = ctx;
    }

    private static CrashHandlerManager mInstance;

    public static CrashHandlerManager getInstance(Context ctx){
        if(mInstance == null){
            synchronized (CrashHandlerManager.class){
                mInstance = new CrashHandlerManager(ctx);
            }
        }
        return mInstance;
    }

    /**
     * 设置是否开启调试模式,默认开启
     * @param debug
     */
    public CrashHandlerManager debug(boolean debug){
        this.debug = debug;
        return this;
    }

    /**
     * 设置错误信息提示
     * @param tips
     * @return
     */
    public CrashHandlerManager tips(String tips){
        this.tip = tips;
        return this;
    }

    /**
     * 设置保存文件名
     * @param fileDir 文件目录
     * @param fileName 文件名
     * @param suffix 文件后缀
     * @return
     */
    public CrashHandlerManager file(String fileDir,String fileName,String suffix){
        this.fileName = fileName;
        this.suffix = suffix;
        return this;
    }

    /**
     * 开始捕捉异常
     */
    public void startCatching(){
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if(debug){
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }

        if(!handleException(e) && mDefaultHandler != null){//异常处理失败
            mDefaultHandler.uncaughtException(t,e);
        }else{//处理成功,重启app
//            restartApplication();
        }

        //退出程序
//        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 异常处理
     * @param ex
     * @return
     */
    private boolean handleException(Throwable ex){
        if(ex == null){
            return false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(ctx, tip, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();

        write2File(ex);
        upload2Server(ex);

        return true;
    }

    /**
     * 把错误信息写入文件
     *
     * @param ex
     */
    private void write2File(Throwable ex) {
        // 如果没有存储卡则返回
        String state = Environment.getExternalStorageState();
        if (!TextUtils.equals(Environment.MEDIA_MOUNTED, state)) {
            return;
        }
        // 取得存储目录
        File directory = getStoreFile();
        if (!directory.exists()) {
            directory.mkdir();
        }
        // 当前时间
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);

        File file = new File(directory + File.separator + fileName + "_" + time + suffix);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fw == null) return;
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
        // 写时间
        pw.println(time);
        writePhoneInfo(pw);
        pw.println();
        ex.printStackTrace(pw);
        pw.close();
    }

    /**
     * 获取异常文件存储目录
     *
     * @return
     */
    private File getStoreFile() {
        String path;
        String state = Environment.getExternalStorageState();
        boolean isRemovable = Environment.isExternalStorageRemovable();
        if (TextUtils.equals(state, Environment.MEDIA_MOUNTED) || !isRemovable) {
            path = ctx.getExternalCacheDir().toString();
        } else {
            path = ctx.getCacheDir().toString();
        }

        return new File(PhoneUtils.getSDCardPath() + ConstantsManager.APP_CRASH_DIR);
    }

    /**
     * 手机型号等信息
     *
     * @param pw
     */
    private void writePhoneInfo(PrintWriter pw) {
        String packageName = ctx.getPackageName();
        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        pw.println("Application Version: " + packageInfo.versionName + "_" + packageInfo.versionCode);
        pw.println("OS Version: " + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT);
        pw.println("Vendor: " + Build.MANUFACTURER);
        pw.println("Model: " + Build.MODEL);
        pw.println("CPU ABI: " + Build.CPU_ABI);
    }

    /**
     * 把错误信息上传到服务器，此处上传到友盟服务器
     *
     * @param ex
     */
    private void upload2Server(Throwable ex) {
        MobclickAgent.reportError(ctx,ex);
    }

    /**
     * 重启app
     */
    private void restartApplication(){
        String packageName = ctx.getPackageName();
        PackageManager packageManager = ctx.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(intent);
    }
}
