package com.violet.library.manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.violet.library.R;
import com.violet.library.utils.IntentUtils;
import com.violet.library.utils.PhoneUtils;
import com.violet.library.utils.download.ProgressDownloader;
import com.violet.library.utils.download.ProgressResponseBody;

import java.io.File;

/**
 * description：下载更新管理
 * author：JimG on 16/10/13 15:58
 * e-mail：info@zijinqianbao.com
 */

public class UpdateManager implements ProgressResponseBody.ProgressListener{
    public static final String TAG = "UpdateManager";

    private Context ctx;

    /**
     * 进度条显示对话框
     */
    private Dialog proDialog;

    /**
     * 下载进度条
     */
    private ProgressBar progressBar;
    /**
     * 当前进度显示
     */
    private TextView tvCurProgress;

    /**
     * 标识是否为暂停状态,默认为暂停状态
     */
    private boolean isPause;

    /**
     * 断点位置
     */
    private long breakPoints;
    /**
     * 下载器
     */
    private ProgressDownloader downloader;
    /**
     * 下载的字节数
     */
    private long totalBytes;
    /**
     * 总文件字节数
     */
    private long contentLength;

    /**
     * 保存的文件位置
     */
    private File destinaFile;

    private UpdateManager(){}

    private UpdateManager(Context ctx){
        this.ctx = ctx;
    }

    private static UpdateManager mInstance;

    public static UpdateManager getInstance(Context ctx){
        if(mInstance == null){
            synchronized (UpdateManager.class){
                mInstance = new UpdateManager(ctx);
            }
        }
        return mInstance;
    }


    /**
     * 检测是否有版本更新
     * @param cxt
     * @param netVersion 网络上的版本
     * @param updateMsg  网络版本提示信息
     * @return 是否调用了更新功能
     */
    public static boolean checkUpdateInfo(final Context cxt,int netVersion,String updateMsg){
        int curVersion = Integer.valueOf(PhoneUtils.getVersionCode(cxt));

        //当前版本低于网络版本,则启动更新
        boolean requestUpdate = curVersion < netVersion;
        if(requestUpdate){
            if(!TextUtils.isEmpty(updateMsg)){
                updateMsg = updateMsg + "<br/>";
            }else{
                updateMsg = "";
            }

            updateMsg = updateMsg + cxt.getResources().getString(R.string.update_now);
            DialogManager.getConfirmCancelDialog(cxt, updateMsg, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateManager.getInstance(cxt).downLoadApk();
                }
            }).show();
        }

        return requestUpdate;
    }

    /**
     * 下载APK
     */
    private void downLoadApk(){
        destinaFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "violet.apk");
        downloader = new ProgressDownloader(ConfigsManager.APK_DOWNLOAD,destinaFile,UpdateManager.this);

        View contentView = View.inflate(ctx,R.layout.download_layout,null);
        proDialog = DialogManager.getStandardDialog(ctx, "进度提示", contentView, null, R.string.pause, 0, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消下载
                downloader.pause();
                breakPoints = 0;
                isPause = false;
            }
        },true,false);

        progressBar = (ProgressBar) proDialog.findViewById(R.id.progressBar);
        tvCurProgress = (TextView) proDialog.findViewById(R.id.curProgress);
        //继续、暂停按钮
        final TextView positiveTv = (TextView) proDialog.findViewById(R.id.tvPositive);
        positiveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPause){//暂停状态
                    downloader.download(breakPoints);
                    positiveTv.setText(R.string.pause);
                }else{//下载中状态
                    downloader.pause();
                    positiveTv.setText(R.string._continue);

                    breakPoints = totalBytes;
                }
                isPause = !isPause;
            }
        });

        proDialog.show();

        downloader.download(breakPoints);
    }

    @Override
    public void onPreExecute(long contentLength) {
        // 文件总长只需记录一次，要注意断点续传后的contentLength只是剩余部分的长度

        if (this.contentLength == 0) {
            if(contentLength == 0){//未获取到当前文件的大小
                progressBar.setIndeterminate(false);
            }
            this.contentLength = contentLength;
        }
    }

    @Override
    public void update(long totalBytes, boolean done) {
        // 注意加上断点的长度
        this.totalBytes = totalBytes + breakPoints;

        if(this.contentLength != 0){
            //获取到文件大小
            final int progress = (int) (this.totalBytes * 100 / contentLength);

            Activity act = (Activity) ctx;
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(progress);
                    tvCurProgress.setText(progress + "%");
                }
            });
        }
        if (done) {
            //下载完成,进行安装
            IntentUtils.installApk(ctx,destinaFile);

            proDialog.dismiss();
        }
    }
}
