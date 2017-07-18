package com.violet.library.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.violet.library.BaseApplication;
import com.violet.library.manager.ConfigsManager;
import com.violet.library.manager.L;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

/**
 * Created by perley on 15/9/20.
 */
public class PhoneUtils {

    /**
     * 获取metaData
     *
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return apiKey;
    }

    /**
     * 获取屏幕高度
     *
     * @param context 上下文对象
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文对象
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获取状态栏的高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取系统版本号
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        String versionCode = "";
        String packageName = context.getApplicationContext().getPackageName();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_CONFIGURATIONS);
            versionCode = String.valueOf(info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取系统版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        String packageName = context.getApplicationContext().getPackageName();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_CONFIGURATIONS);
            versionName = String.valueOf(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    /**
     * 显示软键盘
     */
    public static void showSoftKeyboard(Context context, EditText edit) {
        InputMethodManager keyboard = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(edit, 0);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(Context context, EditText edit) {
        InputMethodManager keyboard = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    /**
     * sp或者 dp 装换为 px
     */
    public static int dpToPx(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dpValue * scale);
    }

    /**
     * sp或者 dp 装换为 px
     */
    public static int dp2Px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dpValue * scale);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A windows value to represent dp equivalent to px value
     */
    public static float px2Dp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    /**
     * sp转换为Px
     *
     * @param sp
     * @return
     */
    public static float sp2px(float sp) {
        final float scale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().equals("usbnet0")) {
                    continue;
                }
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && (inetAddress instanceof Inet4Address)) {
                        return StringUtils.byte2hex(inetAddress.getAddress());
                    }
                }
            }
        } catch (SocketException ex) {
            L.e(ex.getMessage());
        }
        return null;
    }

    /**
     * 获取设备mac地址
     *
     * @return
     */
    public static byte[] getMacAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        while (interfaces.hasMoreElements()) {
            final NetworkInterface ni = interfaces.nextElement();
            try {
                if (ni.isLoopback() || ni.isPointToPoint() || ni.isVirtual())
                    continue;
            } catch (SocketException e) {
                e.printStackTrace();
            }
            byte[] macAddress = null;
            try {
                macAddress = ni.getHardwareAddress();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            if (macAddress != null && macAddress.length > 0)
                return macAddress;
        }
        return null;
    }

    //获取无线mac地址
    public static String getWifiMacAddr(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String macAddr = null;
        if (null != info) {
            String addr = info.getMacAddress();
            if (null != addr) {
                L.d("getWifiMacAddr:" + addr);
                macAddr = addr;
            }
        }
        return macAddr;
    }

    /**
     * 根据资源名称和类型获取资源
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 判断指定包名的应用程序是否可用(QQ:com.tencent.mobileqq 微信:com.tencent.mm Skype:com.skype.raider)
     *
     * @param context
     * @return
     */
    public static boolean isApplicationAvailable(Context context, String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            final PackageManager myPackageMgr = context.getPackageManager();
            try {
                myPackageMgr.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 判断指定的service是否存活
     * @param context
     * @param serviceName
     * @return
     */
    public static boolean isServiceRunning(Context context,String serviceName) {
        if(TextUtils.isEmpty(serviceName)) return false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定的app启动类
     * @param context
     * @return
     */
    public static String getLauncherClassName(Context context) {
        if(context == null){
            return null;
        }
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        try {
            for (ResolveInfo resolveInfo : pm.queryIntentActivities(intent, 0)) {
                if (resolveInfo.activityInfo.applicationInfo.packageName.equalsIgnoreCase(context.getPackageName())) {
                    return resolveInfo.activityInfo.name;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static String uniqueId;

    public static String getUniqueDeviceId(){
        if(TextUtils.isEmpty(uniqueId)){
            uniqueId = getUniqueDeviceId(BaseApplication.getInstance());
        }
        return uniqueId;
    }

    /**
     * 获取设备唯一id
     * @return
     */
    public static String getUniqueDeviceId(Context ctx){
        if(ConfigsManager.DEBUG_ENABLE){
            return "123456789123456";
        }

        //获取设备IMEI
        TelephonyManager TelephonyMgr = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String imeiInfo = TelephonyMgr.getDeviceId();
        //Pseudo-Unique ID
        String devIdShort = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10;
        //android id
        String androidId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        //wlan mac
        WifiManager wifiManager = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
        String wlanMac = wifiManager.getConnectionInfo().getMacAddress();
        //BlueTooth mac
        BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // Local Bluetooth adapter
        String btMac = m_BluetoothAdapter.getAddress();

        //根据上述五种方式拼接 获取唯一标识加密
        String uniqueId = imeiInfo + devIdShort + androidId + wlanMac + btMac;
        // compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(uniqueId.getBytes(),0,uniqueId.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String contactsId = new String();
        for (int i=0;i<p_md5Data.length;i++) {
            int b =  (0xFF & p_md5Data[i]);
        // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF)
                contactsId += "0";
        // add number to string
            contactsId += Integer.toHexString(b);
        }   // hex string to uppercase
        uniqueId = contactsId.toUpperCase();
        return uniqueId;
    }

    /**
     * uri 转path
     * @param activity
     * @param uri
     * @return
     */
    public static String uri2Path(Activity activity, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * 获取sd卡存储目录
     * @return
     */
    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
                if (lineStr.contains("sdcard")
                        && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure",
                                "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {

            return Environment.getExternalStorageDirectory().getPath();
        }

        return Environment.getExternalStorageDirectory().getPath();
    }
}
