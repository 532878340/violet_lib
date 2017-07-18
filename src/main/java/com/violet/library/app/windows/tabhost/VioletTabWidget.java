package com.violet.library.app.windows.tabhost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TabWidget;

/**
 * 自定义的TabWidget，增加选中回调、气泡等功能
 * Created by Perley on 2015/2/10.
 */
public class VioletTabWidget extends TabWidget {

    public VioletTabWidget(Context context) {
        this(context, null);
    }

    public VioletTabWidget(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.tabWidgetStyle);
    }

    public VioletTabWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setStripEnabled(false);
        setDividerDrawable(null);
    }

    /**
     * 设置某个Tab上显示特定的文本（如未读消息数）
     * @param index Tab的索引值
     * @param text Tab上需要显示的文本
     */
    public void setMarkerText(int index, CharSequence text) {
        View childTab = getChildTabViewAt(index);
        if (childTab instanceof Markable) {
            ((Markable) childTab).setMarkerText(text);
        }
    }

    /**
     * 设置某个Tab上显示小圆点
     * @param index Tab的索引值
     */
    public void setMarkerPoint(int index) {
        View childTab = getChildTabViewAt(index);
        if (childTab instanceof Markable) {
            ((Markable) childTab).setMarkerPoint();
        }
    }




}
