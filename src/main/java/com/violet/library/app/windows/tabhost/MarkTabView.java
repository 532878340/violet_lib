package com.violet.library.app.windows.tabhost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.violet.library.utils.PhoneUtils;

/**
 * Created by Perley on 2015/2/10.
 */
public class MarkTabView extends VioletTabView implements Markable {

    private static final int POINT_SIZE = 5; // 圆点的size，单位dp

    private TextView mBadge;

    public MarkTabView(Context context, int layoutId) {
        this(context, null, layoutId);
    }

    public MarkTabView(Context context, AttributeSet attrs, int layoutId) {
        super(context, attrs, layoutId);
        View badge = findViewById(android.R.id.text2);
        if (!(badge instanceof TextView)) {
            throw new IllegalStateException("layoutId中必须包含id为android.R.id.text2的TextView");
        }
        mBadge = (TextView) badge;
    }

    @Override
    public void setMarkerText(CharSequence text) {
        mBadge.setText(text);
    }

    @Override
    public void setMarkerPoint() {
        mBadge.setText("");

        final int size = PhoneUtils.dp2Px(POINT_SIZE);
        ViewGroup.LayoutParams lp = mBadge.getLayoutParams();
        lp.height = size;
        lp.width = size;
        mBadge.requestLayout();
    }

}
