package com.violet.library.pulltorefresh;

import android.view.View;

/**
 * description：定义下拉刷新的接口
 * author：JimG on 16/9/30 14:19
 * e-mail：info@zijinqianbao.com
 */

public interface IPullToRefresh<T extends View>{
    //下拉刷新是否可用
    public void setPullRefreshEnabled(boolean pullRefreshEnabled);

    //上拉加载是否可用
    public void setPullLoadEnabled(boolean pullLoadEnabled);

    //滚动到底部自动加载,设置为true时,上拉加载设置无效
    public void setScrollLoadEnabled(boolean scrollLoadEnabled);

    //下拉刷新是否可用
    public boolean isPullRefreshEnabled();

    //上拉加载是否可用
    public boolean isPullLoadEnabled();

    //滚动到底部自动加载
    public boolean isScrollLoadEnabled();

    //设置刷新的监听
    public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<T> refreshListener);

    //下拉刷新完成
    public void onPullDownRefreshComplete();

    //上拉加载完成
    public void onPullUpRefreshComplete();

    //获取刷新内容view
    public T getRefreshableView();

    //获取头部下拉view
    public LoadingLayout getHeaderLoadingLayout();
    
    //获取底部上拉view
    public LoadingLayout getFooterLoadingLayout();

    //设置刷新文本提示信息
    public void setLastUpdatedLabel(CharSequence label);
}
