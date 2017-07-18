package com.violet.library.app.windows.tabhost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 独立的TabWidget控件，提供单独的选项监听（此控件不能配合TabHost使用）
 * Created by Perley on 2015/6/29.
 */
public class IndieTabWidget extends VioletTabWidget {

    private OnTabSelectedListener mTabSelectedListener;

    public IndieTabWidget(Context context) {
        this(context, null);
    }

    public IndieTabWidget(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.tabWidgetStyle);
    }

    public IndieTabWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 设置tab选中监听
     */
    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        mTabSelectedListener = listener;
        addTabClickListener();
    }

    private void addTabClickListener() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setOnClickListener(new TabClickListener(i));
        }
    }


    private class TabClickListener implements View.OnClickListener {

        private final int mTabIndex;

        private TabClickListener(int tabIndex) {
            mTabIndex = tabIndex;
        }

        public void onClick(View v) {
            if (mTabSelectedListener != null) {
                mTabSelectedListener.onTabSelectionChanged(mTabIndex);
            }
        }
    }


    public interface OnTabSelectedListener {
        /**
         * @param tabIndex index of the tab that was selected
         */
        void onTabSelectionChanged(int tabIndex);
    }
}
