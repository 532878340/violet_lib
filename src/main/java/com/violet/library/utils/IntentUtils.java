package com.violet.library.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.violet.library.R;
import com.violet.library.app.windows.toast.ToastWidget;
import com.violet.library.manager.L;

import java.io.File;

/**
 * Created by perley on 15/9/20.
 */
public class IntentUtils {
    public static final String TAG = "IntentUtils";

    /**
     * 跳转系统拨号界面
     * @param phone
     */
    public static void jumpCallPhone(Context context, CharSequence phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    /**
     * 跳转发送邮件
     * @param mailAddress 邮箱地址
     * @param subject 主题
     * @param body 内容
     */
    public static void jumpEmail(Context context, CharSequence mailAddress, CharSequence subject, CharSequence body) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{mailAddress.toString()});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, body);
        try {
            context.startActivity(Intent.createChooser(i, "发送邮件..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "您尚未安装邮件客户端", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转发送邮件
     * @param mailAddress 邮箱地址
     */
    public static void jumpEmail(Context context, CharSequence mailAddress) {
        jumpEmail(context, mailAddress, null, null);
    }

    /**
     * 跳转浏览器
     * @param url 地址
     */
    public static void jumpWeb(Context context, CharSequence url) {
        if (url == null) {
            return;
        }
        String urlStr = url.toString();
        if (!urlStr.startsWith("http")) {
            urlStr = "http://" + urlStr;
        }
        Uri uri = Uri.parse(urlStr);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }

    /**
     * 选择浏览器
     * @param context
     * @param url
     */
    public static void jumpWebChoose(Context context,CharSequence url){
        L.d("url--------------->" + url);
        if (url == null) {
            return;
        }
        String urlStr = url.toString();
        if (!urlStr.startsWith("http")) {
            urlStr = "http://" + urlStr;
        }
        Intent intent= new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(urlStr);
        intent.setData(content_url);
        context.startActivity(Intent.createChooser(intent, "请选择一款浏览器"));
    }

    /**
     * 跳转系统短信界面
     * @param context
     * @param content
     */
    public static void jumpSms(Context context, CharSequence content) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setType("vnd.android-dir/mms-sms");
        it.putExtra("sms_body", content);
        context.startActivity(it);
    }

    /**
     * 安装APK文件
     * @param ctx
     * @param file
     *
     * 针对7.0可能出现的权限问题，解决方案
     * 1、application的oncreate()添加
     *  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0权限问题
     *      StrictMode.VmPolicy.Builder vmBuilder = new StrictMode.VmPolicy.Builder();
     *      StrictMode.setVmPolicy(vmBuilder.build());
     *  }
     *
     * 2、利用FileProvider，进行权限共享
     *  if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N){//判读版本是否在7.0以上
     *      apkUri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".fileprovider", file);
     *      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
     * }
     *
     */
    public static void installApk(Context ctx,File file){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri apkUri = Uri.fromFile(file);
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N){//判读版本是否在7.0以上
//            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
//            apkUri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".fileprovider", file);
//            //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        }
        Log.d(TAG, "installApk uri : " + apkUri.toString());
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }

    /**
     * 打开指定QQ聊天页
     * @param ctx
     * @param qqNum
     */
    public static void jumpQQ(Context ctx,String qqNum){
        if(!TextUtils.isEmpty(qqNum)){
            String url="mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum;
            try {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }catch (RuntimeException e){
                ToastWidget.getInstance().failed("请先安装QQ程序");
                e.printStackTrace();
            }
        }
    }

    /**
     * 跳转到skype指定联系人界面
     * @param ctx
     * @param skypeAct
     */
    public static void jumpSkype(Context ctx,String skypeAct){
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("skype:" + skypeAct + "?chat&topic=" + ctx.getString(R.string.app_name)));
        intent.setComponent(new ComponentName("com.skype.raider","com.skype.raider.Main"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    /**
     * 打开指定的app
     * @param ctx
     * @param appPackageName
     */
    public static void jumpApplication(Context ctx,String appPackageName){
        Intent intent = ctx.getPackageManager().getLaunchIntentForPackage(appPackageName);
        ctx.startActivity(intent);
    }

    /****************
     *
     * 发起添加群流程。
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public static boolean joinQQGroup(Context ctx,String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            ctx.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    /**
     * 跳转到singleTask类
     * @param ctx
     * @param cls
     * @param bundle
     */
    public static void jumpSingleTask(Context ctx, Class cls, Bundle bundle){
        Intent intent = new Intent(ctx,cls);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(intent);
    }

}
