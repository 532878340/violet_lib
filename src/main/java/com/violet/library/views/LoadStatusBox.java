package com.violet.library.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.violet.library.R;


/**
 * @author shi
 */
public class LoadStatusBox extends RelativeLayout {

    private View progress, errorView, empty;
    private ProgressWheel proWheel;

    public LoadStatusBox(Context context) {
        super(context);
    }

    public LoadStatusBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadStatusBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progress = findViewById(R.id.loadProgress);
        proWheel = (ProgressWheel) findViewById(R.id.proWheel);
        errorView = findViewById(R.id.loadErrorBox);
        empty = findViewById(R.id.emptyView);
    }

    /**
     * 开始加载用的方法
     */
    public void loading() {
        progress.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);

        if (!proWheel.isSpinning()) {
            proWheel.spin();
        }

        setVisibility(View.VISIBLE);
    }

    public void failed() {
        progress.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);

        if (proWheel.isSpinning()) {
            proWheel.stopSpinning();
        }
        setVisibility(View.VISIBLE);
    }

    /**
     * 数据为空的调用方法
     */
    public void empty() {
        empty("暂时没有数据");
    }

    /**
     * 数据为空的调用方法
     *
     * @param emptyMsg
     */
    public void empty(String emptyMsg) {
        empty(emptyMsg, R.drawable.icon_nodata);
    }

    /**
     * 数据为空的调用方法
     *
     * @param emptyMsg    文本提示
     * @param drawableRes 显示的图片
     */
    public void empty(String emptyMsg, int drawableRes) {
        progress.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        proWheel.stopSpinning();
        setVisibility(View.VISIBLE);
        ((TextView) empty.findViewById(R.id.emptyText)).setText(emptyMsg);
        if (drawableRes > 0) {
            ((ImageView) empty.findViewById(R.id.emptyImage)).setImageResource(drawableRes);
            empty.findViewById(R.id.emptyImage).setVisibility(View.VISIBLE);
        } else {
            ((ImageView) empty.findViewById(R.id.emptyImage)).setImageResource(0);
            empty.findViewById(R.id.emptyImage).setVisibility(View.GONE);
        }

        setOnClickListener(null);
    }

    public void success() {
        proWheel.stopSpinning();
        if (getVisibility() != View.GONE) {
            setVisibility(View.GONE);
        }
    }

    @Override
    public boolean performClick() {
        if (progress.getVisibility() == View.VISIBLE) {
            return true;
        }
        return super.performClick();
    }
}
