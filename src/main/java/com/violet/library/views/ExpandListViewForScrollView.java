package com.violet.library.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * description：
 * author：JimG on 17/3/31 11:41
 * e-mail：info@zijinqianbao@qq.com
 */


public class ExpandListViewForScrollView extends ExpandableListView{
    public ExpandListViewForScrollView(Context context) {
        super(context);
    }

    public ExpandListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
