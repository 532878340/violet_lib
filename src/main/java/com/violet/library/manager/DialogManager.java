package com.violet.library.manager;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.violet.library.R;
import com.violet.library.utils.PhoneUtils;

/**
 * description：通用对话框
 * author：JimG on 16/10/12 16:46
 * e-mail：info@zijinqianbao.com
 */

public class DialogManager{
    /**
     * 通用提示的对话框,单个按钮(CharSequence)
     * @return
     */
    public static Dialog getAlertDialog(Context ctx, CharSequence contentStr,final View.OnClickListener _positiveLis){
        return getStandardDialog(ctx,null,null,contentStr,null,null,_positiveLis,null,false,true);
    }
    public static Dialog getAlertDialog(Context ctx, int contentRes,final View.OnClickListener _positiveLis){
        String content = contentRes == 0 ? null : ctx.getResources().getString(contentRes);
        return getStandardDialog(ctx,null,null,content,null,null,_positiveLis,null,false,true);
    }

    /**
     * 通用提示对话框,两个按钮
     * @param ctx
     * @param contentStr
     * @param positiveStr
     * @param nagetiveStr
     * @param _positiveLis
     * @param _nagetiveLis
     * @return
     */
    public static Dialog getStandardDialog(Context ctx,CharSequence contentStr, CharSequence positiveStr, CharSequence nagetiveStr,
                                           final View.OnClickListener _positiveLis, final View.OnClickListener _nagetiveLis){
        return getStandardDialog(ctx,null,null,contentStr,positiveStr,nagetiveStr,_positiveLis,_nagetiveLis,true,false);
    }

    public static Dialog getStandardDialog(Context ctx,CharSequence contentStr,int positiveRes,int nagetiveRes,final View.OnClickListener _positiveLis, final View.OnClickListener _nagetiveLis){
        String positiveStr = positiveRes == 0 ? null : ctx.getResources().getString(positiveRes);
        String nagetiveStr = nagetiveRes == 0 ? null : ctx.getResources().getString(nagetiveRes);
        return getStandardDialog(ctx,null,null,contentStr,positiveStr,nagetiveStr,_positiveLis,_nagetiveLis,true,false);
    }


    /**
     * 信息提示对话框,默认"确定、取消"按钮,无取消监听
     * @param ctx
     * @param contentStr
     * @param _positiveLis
     * @return
     */
    public static Dialog getConfirmCancelDialog(Context ctx,CharSequence contentStr,final View.OnClickListener _positiveLis){
        return getStandardDialog(ctx,null,null,contentStr,null,null,_positiveLis,null,true,false);
    }


    /**
     *
     * @param ctx
     * @param title 标题
     * @param layoutRes 布局内容,null则直接现在TextView
     * @param contentStr 内容文本
     * @param positiveStr 确定按钮文本
     * @param nagetiveStr 取消按钮文本
     * @param _positiveLis 确定按钮监听
     * @param _nagetiveLis 取消按钮监听
     * @param withNagetive 是否含有取消按钮
     * @param canCancel 是否可以dismiss
     * @return
     */
    public static Dialog getStandardDialog(Context ctx, CharSequence title, View layoutRes, CharSequence contentStr, CharSequence positiveStr, CharSequence nagetiveStr,
                                           final View.OnClickListener _positiveLis, final View.OnClickListener _nagetiveLis,boolean withNagetive,boolean canCancel){
        final Dialog dialog = new Dialog(ctx,R.style.netLoadingDialog);

        dialog.setContentView(R.layout.base_dialog);

        //标题
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        if(title != null){
            dialogTitle.setText(title);
        }

        //内容
        TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);

        if(layoutRes != null){
            RelativeLayout contentLy = (RelativeLayout) dialog.findViewById(R.id.content);
            contentLy.addView(layoutRes);
            tvContent.setVisibility(View.GONE);
        }else{
            tvContent.setText(Html.fromHtml(TextUtils.isEmpty(contentStr) ? "" : contentStr.toString()));
        }

        //确定按钮
        TextView positiveTv = (TextView) dialog.findViewById(R.id.tvPositive);
        if(positiveStr != null){
            positiveTv.setText(positiveStr);
        }

        positiveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_positiveLis != null) _positiveLis.onClick(v);
                dialog.dismiss();
            }
        });

        //是否含有取消按钮
        TextView nagetiveTv = (TextView) dialog.findViewById(R.id.tvNagetive);
        if(withNagetive){
            //取消按钮
            if(nagetiveStr != null){
                nagetiveTv.setText(nagetiveStr);
            }

            nagetiveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(_nagetiveLis != null) _nagetiveLis.onClick(v);
                    dialog.dismiss();
                }
            });
        }else{
            //仅显示单个确定按钮,此时需要修改background
            nagetiveTv.setVisibility(View.GONE);
            dialog.findViewById(R.id.line).setVisibility(View.GONE);
            positiveTv.setBackgroundResource(R.drawable.radius_single);
            positiveTv.setTextColor(ctx.getResources().getColor(R.color.txt_light));
        }

        //是否可dismiss
        dialog.setCancelable(canCancel);
        dialog.setCanceledOnTouchOutside(canCancel);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = PhoneUtils.getScreenWidth(ctx) - PhoneUtils.dpToPx(ctx, 80);
        window.setGravity(Gravity.CENTER_VERTICAL); // 此处可以设置dialog显示的位置

        return dialog;
    }

    public static Dialog getStandardDialog(Context ctx, CharSequence title, View layoutRes, CharSequence contentStr, int positiveRes, int nagetiveRes,
                                           final View.OnClickListener _positiveLis, final View.OnClickListener _nagetiveLis,boolean withNagetive,boolean canCancel){

        String positiveStr = positiveRes == 0 ? null : ctx.getResources().getString(positiveRes);
        String nagetiveStr = nagetiveRes == 0 ? null : ctx.getResources().getString(nagetiveRes);

        return getStandardDialog(ctx,title,layoutRes,contentStr,positiveStr,nagetiveStr,_positiveLis,_nagetiveLis,withNagetive,canCancel);
    }
}
