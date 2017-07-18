package com.violet.library.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * description：webview滚动监听
 * author：JimG on 16/10/12 10:13
 * e-mail：info@zijinqianbao.com
 */

public class WebAssitantView extends WebView{
    public OnScrollChangeListener mScrollInterface;

    public WebAssitantView(Context context) {
        super(context);
    }

    public WebAssitantView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebAssitantView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mScrollInterface != null){
            mScrollInterface.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener scrollInterface) {
        this.mScrollInterface = scrollInterface;
    }

    public interface OnScrollChangeListener {

        public void onScrollChanged(int l, int t, int oldl, int oldt);

    }
}
