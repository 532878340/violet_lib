package com.violet.library.app.windows.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.violet.library.BaseApplication;
import com.violet.library.R;

/**
 * description:Toast工具类()含图标
 *
 */
public class ToastWidget implements ToastInterface{

    private static Toast toast;

    private ToastWidget(){}

    private final static class ToastHolder{
        private static final ToastWidget INSTANCE = new ToastWidget();
    }

    public static ToastWidget getInstance(){
        return ToastHolder.INSTANCE;
    }

    /**
     *
     * @param context 上下文
     * @param iconRes 显示图片
     * @param toastStr 弹出文本
     */
    public void makeText(Context context, int iconRes, CharSequence toastStr) {
        View layout = LayoutInflater.from(context).inflate(R.layout.ly_toast,null);
        TextView tvToast = (TextView) layout.findViewById(R.id.toastTv);
        ImageView img = (ImageView) layout.findViewById(R.id.img);
        tvToast.setText(toastStr);
        img.setBackgroundResource(iconRes);
        img.setVisibility(iconRes > 0 ? View.VISIBLE : View.GONE);
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void success(CharSequence msg) {
//        makeText(BaseApplication.getInstance(), R.drawable.ic_toast_operation_success, msg);
        makeText(BaseApplication.getInstance(), 0, msg);
    }

    @Override
    public void failed(CharSequence msg) {
//        makeText(BaseApplication.getInstance(), R.drawable.ic_toast_operation_fail, msg);
        makeText(BaseApplication.getInstance(), 0, msg);
    }

    @Override
    public void warning(CharSequence msg) {
//        makeText(BaseApplication.getInstance(), R.drawable.ic_toast_operation_waring, msg);
        makeText(BaseApplication.getInstance(), 0, msg);
    }

    private void showToast(CharSequence msg){
        makeText(BaseApplication.getInstance(), 0, msg);
    }
}
