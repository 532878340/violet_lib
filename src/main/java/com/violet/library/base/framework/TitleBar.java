package com.violet.library.base.framework;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.violet.library.R;
import com.violet.library.app.windows.toast.ToastWidget;
import com.violet.library.utils.IntentUtils;

/**
 * description：标题栏
 * author：JimG on 16/10/9 17:21
 * e-mail：info@zijinqianbao.com
 */

public class TitleBar {
    private View titleLayout;

    private Context context;

    public TitleBar(View container) {
        titleLayout = container.findViewById(R.id.title_root);
        context = container.getContext();
    }

    public View getTitleLayout() {
        return titleLayout;
    }

    public void setTitleBar(int titleText) {
        setTitleBar(false, titleText);
    }

    public void setTitleBar(CharSequence titleText) {
        setTitleBar(false, titleText);
    }

    public void setTitleBar(boolean showBack, int titleText) {
        setTitleBar(showBack, titleText, 0, 0, null);
    }

    public void setTitleBar(boolean showBack, CharSequence titleText) {
        setTitleBar(showBack, false, titleText, 0, null, null);
    }

    public void setTitleBar(boolean showBack, boolean showClose, CharSequence titleText) {
        setTitleBar(showBack, showClose, titleText, 0, null, null);
    }

    /**
     * 设置标题栏
     *
     * @param showBack     是否显示返回按钮
     * @param titleTextRes 标题文本
     * @param rightIcon    右边图标
     * @param rightTextRes 右边文本
     * @param rightClick   右边点击事件
     */
    public void setTitleBar(boolean showBack, int titleTextRes, int rightIcon, int rightTextRes, View.OnClickListener rightClick) {
        String titleText = null;
        if (titleTextRes != 0) {
            titleText = context.getString(titleTextRes);
        }
        String rightText = null;
        if (rightTextRes != 0) {
            rightText = context.getString(rightTextRes);
        }
        setTitleBar(showBack, false, titleText, rightIcon, rightText, rightClick);
    }

    /**
     * 设置标题栏
     *
     * @param showBack   是否显示返回按钮
     * @param titleText  标题文本
     * @param rightIcon  右边图标
     * @param rightText  右边文本
     * @param rightClick 右边点击事件
     */
    public void setTitleBar(boolean showBack, boolean showClose, CharSequence titleText, int rightIcon, String rightText, View.OnClickListener rightClick) {
        if (titleLayout == null) {
            return;
        }
        TextView title = (TextView) titleLayout.findViewById(R.id.top_title_tv);
        title.setText(titleText);

        TextView right = (TextView) titleLayout.findViewById(R.id.top_right_tv);
        if (rightClick != null) {
            if (rightIcon != 0) {
                right.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightIcon, 0);
            }
            right.setText(rightText);
            right.setOnClickListener(rightClick);
            right.setVisibility(View.VISIBLE);
        } else {
            right.setVisibility(View.GONE);
        }

        if (showBack) {
            TextView btnBack = (TextView) titleLayout.findViewById(R.id.top_left_tv);
            btnBack.setCompoundDrawablesWithIntrinsicBounds(R.drawable.top_left_arrow, 0, 0, 0);
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof Activity) {
                        ((Activity) context).onBackPressed();
                    }
                }
            });
        }

        if (showClose) {
            View top_left_close = titleLayout.findViewById(R.id.top_left_close);
            top_left_close.setVisibility(View.VISIBLE);
            top_left_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Class cls = Class.forName("com.deelon.zijinqianbao.userinterface.activity.HomeMainActivity");
                        IntentUtils.jumpSingleTask(context, cls, null);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 设置左边栏按钮事件
     *
     * @param leftIcon
     * @param _lis
     */
    public void setTitleLeftLis(int leftIcon, View.OnClickListener _lis) {
        if (titleLayout == null) {
            return;
        }

        TextView leftLis = (TextView) titleLayout.findViewById(R.id.top_left_tv);
        leftLis.setCompoundDrawablesWithIntrinsicBounds(leftIcon > 0 ? leftIcon : R.drawable.top_left_arrow, 0, 0, 0);

        leftLis.setOnClickListener(_lis);
    }

    /**
     * 设置左边按钮
     *
     * @param icon 图标
     * @param text 文案
     * @param l
     */
    public void setLeft(int icon, String text, View.OnClickListener l) {
        if (titleLayout == null) {
            return;
        }
        TextView left = (TextView) titleLayout.findViewById(R.id.top_left_tv);
        left.setCompoundDrawablesWithIntrinsicBounds(icon > 0 ? icon : 0, 0, 0, 0);
        left.setText(text);
        if (!TextUtils.isEmpty(text) || icon > 0) {
            ((View) left.getParent()).setOnClickListener(l);
            left.setVisibility(View.VISIBLE);
        } else {
            left.setVisibility(View.GONE);
        }
    }
}
