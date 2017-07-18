package com.violet.library.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.violet.library.R;
import com.violet.library.app.windows.toast.ToastWidget;
import com.violet.library.manager.ConfigsManager;
import com.violet.library.manager.ConstantsManager;
import com.violet.library.manager.DialogManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * description：更新工具类
 * author：JimG on 17/5/26 09:49
 * e-mail：info@zijinqianbao@qq.com
 */

public class UpdateUtils {
    public static final String TAG = "UpdateUtils";

    /**
     * 下载文件保存目录
     */
    public static final String DESTININATION_DIR = PhoneUtils.getSDCardPath() + ConstantsManager.APP_DOWNLOAD_DIR;

    /**
     * 检测是否有版本更新
     * @param ctx
     * @param netVersion 网络上的版本
     * @param updateMsg  网络版本提示信息
     * @return 是否调用了更新功能
     */
    public static boolean checkUpdateInfo(final Context ctx, int netVersion, final String versionName, String updateMsg,final String downloadUrl){
        int curVersion = Integer.valueOf(PhoneUtils.getVersionCode(ctx));

        //当前版本低于网络版本,则启动更新
        boolean requestUpdate = curVersion < netVersion;
        if(requestUpdate){
            if(!TextUtils.isEmpty(updateMsg)){
                updateMsg = updateMsg + "<br/>";
            }else{
                updateMsg = "";
            }

            updateMsg = updateMsg + ctx.getResources().getString(R.string.update_now);
            DialogManager.getConfirmCancelDialog(ctx, updateMsg, new View.OnClickListener() {
                @Override
                public void onClick(View v) {//更新操作
                    downloadApk(ctx,versionName,downloadUrl);
                }
            }).show();
        }

        return requestUpdate;
    }

    /**
     * 下载apk
     * @param ctx
     * @param versionName 版本名，用来标识apk名称
     * @param downloadUrl
     */
    public static void downloadApk(final Context ctx,String versionName, final String downloadUrl){
        try{
            if(TextUtils.isEmpty(versionName)){
                ToastWidget.getInstance().warning("版本名不能为空");
            }else if(TextUtils.isEmpty(downloadUrl) || !downloadUrl.endsWith(".apk")){
                ToastWidget.getInstance().warning("文件不是合法的安装包格式");
            }else{//下载
                File downloadDir = new File(DESTININATION_DIR);
                if(!downloadDir.exists()){//创建目录
                    downloadDir.mkdirs();
                }

                final String apkName = ctx.getPackageName().replace(".","_") + "_" + versionName + ".apk";//com_violet_library_1.0.0.apk
                final File file = new File(DESTININATION_DIR,apkName);//保存的文件
                if(file.exists()){//存在文件则直接安装
                    IntentUtils.installApk(ctx,file);
                }else{//不存在文件则下载
                    View contentView = View.inflate(ctx,R.layout.download_layout,null);
                    final Dialog dialog = DialogManager.getStandardDialog(ctx, "进度提示", contentView, null, R.string.msg_cancel, 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //取消下载
                            OkHttpUtils.getInstance().cancelTag(ConstantsManager.TAG_DOWNLOAD);
                        }
                    },null,false,true);

                    final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);//进度条
                    final TextView curProgress = (TextView) dialog.findViewById(R.id.curProgress);//进度显示
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    OkHttpUtils.get()
                            .url(ConfigsManager.URL_BASE + downloadUrl)
                            .tag(ConstantsManager.TAG_DOWNLOAD)
                            .build()
                            .execute(new FileCallBack(DESTININATION_DIR,apkName) {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    ToastWidget.getInstance().warning("下载异常，请稍后重试");
                                    if(dialog != null) dialog.dismiss();
                                }

                                @Override
                                public void onResponse(File response, int id) {
                                    IntentUtils.installApk(ctx,file);
                                    if(dialog != null) dialog.dismiss();
                                }

                                @Override
                                public void inProgress(float progress, long total, int id) {
                                    if(dialog != null){
                                        progressBar.setProgress((int) (progress * 100));
                                        curProgress.setText(StringUtils.getDecimalFormat(progress * 100)+ "%");
                                    }
                                }
                            });
                }
            }
        }catch (Exception e){
            ToastWidget.getInstance().warning("下载异常，请去电脑端扫描下载.");
        }
    }
}
