package com.violet.library.manager;

import android.app.Activity;
import android.util.Log;

/**
 * Log统一管理类
 * 
 * @author way
 * 
 */
public class L {

	private static boolean isDebug = ConfigsManager.DEBUG_ENABLE;

	private static final String TAG = "way";

    /**
     * 是否输出日志
     * @param debug
     */
    public static void setIsDebug(boolean debug) {
        isDebug = debug;
    }

	// 下面四个是默认tag的函数
	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, msg);
	}
	//下面是传入类名打印log
	public static void i(Class<?> _class, String msg){
		if (isDebug)
			Log.i(_class.getName(), msg);
	}
	public static void d(Class<?> _class, String msg){
		if (isDebug)
			Log.d(_class.getName(), msg);
	}
	public static void d(Activity _class, String msg){
		if (isDebug)
			Log.d(_class.getClass().getName(), msg);
	}
	public static void e(Class<?> _class, String msg){
		if (isDebug)
			Log.e(_class.getName(), msg);
	}
	public static void v(Class<?> _class, String msg){
		if (isDebug)
			Log.v(_class.getName(), msg);
	}
	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			Log.d(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			Log.e(tag, msg);
	}
	
	public static void w(String tag, String msg) {
		if (isDebug)
			Log.w(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (isDebug)
			Log.v(tag, msg);
	}
}