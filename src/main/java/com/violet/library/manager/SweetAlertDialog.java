package com.violet.library.manager;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.violet.library.R;
import com.violet.library.utils.PhoneUtils;

import okhttp3.Request;

/**
 * description：build构造ios对话框,此方式不能自定义布局
 * author：JimG on 16/11/24 14:48
 * e-mail：info@zijinqianbao@qq.com
 */

public class SweetAlertDialog {
    /**
     * 取消位置的flag
     */
    public static final int FLAG_NEGATIVE = 0;
    /**
     * 成功位置的flag
     */
    public static final int FLAG_POSITIVE = 1;

    private Context context;
    private int titleIds;//标题
    private String title;
    private String message;//内容
    private boolean withCancel;//是否含有cancel按钮
    private TextView mContentTitle;
    private TextView mContentMessage;
    private Dialog mDialog;
    private CharSequence mNegativeButtonText;
    private CharSequence mPositiveButtonText;
    private OnDialogClickListener mNegativeButtonListener;
    private OnDialogClickListener mPositiveButtonListener;
    private TextView mLeftTxt;
    private TextView mRightTxt;
    private View mCenterLine;
    private boolean mCancel;//标识点击屏幕是否可取消
    private int mContentTextSize;
    private int mContentColor;


    public SweetAlertDialog(Builder builder) {
        this.context = builder.mContext;
        this.titleIds = builder.mTitleResId;
        this.title = builder.mTitle;
        this.message = builder.mMessage;
        this.withCancel = builder.mWithCancel;
        this.mNegativeButtonText = builder.mNegativeButtonText;
        this.mPositiveButtonText = builder.mPositiveButtonText;
        this.mNegativeButtonListener = builder.mNegativeButtonListener;
        this.mPositiveButtonListener = builder.mPositiveButtonListener;
        this.mContentTextSize = builder.mContentTextSize;
        this.mContentColor = builder.mContentColor;
        this.mCancel = builder.mCancel;
        this.initView();
    }

    /**
     * 初始化布局文件
     */
    private void initView() {
        View rootView = LayoutInflater.from(context).inflate(R.layout.base_dialog, null);
        mContentTitle = (TextView) rootView.findViewById(R.id.dialogTitle);
        mContentMessage = (TextView) rootView.findViewById(R.id.tvContent);
        mLeftTxt = (TextView) rootView.findViewById(R.id.tvNagetive);
        mRightTxt = (TextView) rootView.findViewById(R.id.tvPositive);
        mCenterLine = rootView.findViewById(R.id.line);
        // 定义Dialog布局和参数
        mDialog = new Dialog(context, R.style.netLoadingDialog);
        mDialog.setContentView(rootView);
        mDialog.setCancelable(mCancel);
        mDialog.setCanceledOnTouchOutside(mCancel);

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = PhoneUtils.getScreenWidth(context) - PhoneUtils.dpToPx(context, 75);
        window.setGravity(Gravity.CENTER_VERTICAL); // 此处可以设置dialog显示的位置
        updateDialogUI();
        mDialog.show();
    }

    private void updateDialogUI() {
        // title resId
        if (titleIds != 0) {
            mContentTitle.setVisibility(View.VISIBLE);
            mContentTitle.setText(titleIds);
        }
        // title
        if (hasNull(title)) {
            mContentTitle.setVisibility(View.VISIBLE);
            mContentTitle.setText(title);
        }

        // message
        if (hasNull(message)) {
            mContentMessage.setText(message);

            if(mContentTextSize != 0){//设置字体大小
                int contentSize = context.getResources().getDimensionPixelSize(mContentTextSize);
                mContentMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX,contentSize);
            }
            if(mContentColor != 0){//设置字体颜色
                mContentMessage.setTextColor(context.getResources().getColor(mContentColor));
            }
        }

        // 默认显示取消按钮 自定义字体
        if (hasNull(mNegativeButtonText) || withCancel) {
            mLeftTxt.setVisibility(View.VISIBLE);
            mLeftTxt.setText(withCancel ? "取消" : mNegativeButtonText);
            mLeftTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDialog != null)
                        mDialog.dismiss();
                    if (!withCancel)
                        mNegativeButtonListener.onClick(mDialog, FLAG_NEGATIVE);
                }
            });
        }

        //左侧文字为空,
        if (!hasNull(mNegativeButtonText) && !withCancel && hasNull(mPositiveButtonText)) {
            mLeftTxt.setVisibility(View.GONE);
            mCenterLine.setVisibility(View.GONE);
            mRightTxt.setBackgroundResource(R.drawable.radius_single);
        }

        if (hasNull(mPositiveButtonText)) {
            mRightTxt.setVisibility(View.VISIBLE);
            mRightTxt.setText(mPositiveButtonText);
            mRightTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDialog != null)
                        mDialog.dismiss();
                    if(mPositiveButtonListener != null){
                        mPositiveButtonListener.onClick(mDialog, FLAG_POSITIVE);
                    }
                }
            });
        }
    }

    public boolean hasNull(CharSequence msg) {
        return !TextUtils.isEmpty(msg);
    }

    public static class Builder {
        private Context mContext;
        private int mTitleResId;
        private String mTitle;
        private String mMessage;
        private boolean mWithCancel = true;
        private CharSequence mNegativeButtonText;
        private CharSequence mPositiveButtonText;
        private OnDialogClickListener mNegativeButtonListener;
        private OnDialogClickListener mPositiveButtonListener;
        private boolean mCancel;
        private int mContentTextSize;
        private int mContentColor;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(@StringRes int titleId) {
            this.mTitleResId = titleId;
            return this;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setWithCancel(boolean withCancel) {
            this.mWithCancel = withCancel;
            return this;
        }

        public Builder setContentTextSize(int contentTextSize){
            this.mContentTextSize = contentTextSize;
            return this;
        }

        public Builder setContentColor(int color){
            this.mContentColor = color;
            return this;
        }

        public Builder setCancel(boolean cancel) {
            this.mCancel = cancel;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final OnDialogClickListener listener) {
            this.mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final OnDialogClickListener listener) {
            this.mPositiveButtonText = text;
            this.mPositiveButtonListener = listener;
            return this;
        }

        public SweetAlertDialog show() {
            return new SweetAlertDialog(this);
        }
    }

    public interface OnDialogClickListener {
        void onClick(Dialog dialog, int which);
    }
}
