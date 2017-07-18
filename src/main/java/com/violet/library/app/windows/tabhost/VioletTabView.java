package com.violet.library.app.windows.tabhost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * description：tabview
 * author：JimG on 16/10/14 14:28
 * e-mail：info@zijinqianbao.com
 */

public class VioletTabView extends RelativeLayout{
    private TextView mLabel;

    private ImageView mIcon;

    public VioletTabView(Context context) {
        this(context, null);
    }

    public VioletTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VioletTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View label = findViewById(android.R.id.text1);
        if (!(label instanceof android.widget.TextView)) {
            throw new IllegalStateException("layoutId中必须包含id为android.R.id.text1的TextView");
        }
        mLabel = (TextView) label;

        View icon = findViewById(android.R.id.icon);
        if (!(icon instanceof ImageView)) {
            throw new IllegalStateException("layoutId中必须包含id为android.R.id.icon的ImageView");
        }
        mIcon = (ImageView) icon;
    }

    /**
     * 设置显示文本
     * @param text
     * @return
     */
    public VioletTabView setLabel(CharSequence text) {
        mLabel.setText(text);
        return this;
    }

    /**
     * 设置显示文本
     * @param textRes
     * @return
     */
    public VioletTabView setLabel(int textRes) {
        mLabel.setText(textRes);
        return this;
    }

    /**
     * 设置显示图标
     * @param iconRes
     * @return
     */
    public VioletTabView setIcon(int iconRes) {
        mIcon.setImageResource(iconRes);
        return this;
    }
}
