package com.violet.library.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * description：高度增加不会自动滚动
 * author：JimG on 17/4/6 10:11
 * e-mail：info@zijinqianbao@qq.com
 */


public class AutoScrollNotScrollView extends ScrollView {
    public AutoScrollNotScrollView(Context context) {
        super(context);
    }

    public AutoScrollNotScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollNotScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
