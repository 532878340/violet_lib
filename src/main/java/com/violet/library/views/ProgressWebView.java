package com.violet.library.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * description：带进度条的webview
 * author：JimG on 16/10/12 10:14
 * e-mail：info@zijinqianbao.com
 */

public class ProgressWebView extends WebAssitantView{
    private NumberProgressBar progressBar;
    private IProgressChangeLis listener;

    @SuppressWarnings("deprecation")
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressBar = new NumberProgressBar(context,null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 3, 0, 0));
        progressBar.setReachedBarHeight(5);
        progressBar.setUnreachedBarHeight(5);
        progressBar.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
        addView(progressBar);

        init();
        setWebChromeClient(new WebChromeClientSub());
    }

    void init(){
        //对系统API在19以上的版本兼容,19以上onpagefinished时回恢复图片加载,如果存在多张图片引用相同的src,会只加载一个Image标签,这种需要单独出来

        if(Build.VERSION.SDK_INT >= 19){
            getSettings().setLoadsImagesAutomatically(true);
        }else{
            getSettings().setLoadsImagesAutomatically(false);
        }
    }

    class WebChromeClientSub extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress == 100){
                progressBar.setVisibility(View.GONE);
            }else{
                if(progressBar.getVisibility() == View.GONE)
                    progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
            if(newProgress == 10 || newProgress == 100){
                listener.getCurProgress(newProgress);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams param = (LayoutParams) progressBar.getLayoutParams();
        param.x = l;
        param.y = t;
        progressBar.setLayoutParams(param);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public interface IProgressChangeLis{
        int getCurProgress(int progress);
    }

    void setProgressChangeListener(IProgressChangeLis _lis){
        this.listener = _lis;
    }

    void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }
}
