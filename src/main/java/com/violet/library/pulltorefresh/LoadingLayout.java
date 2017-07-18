package com.violet.library.pulltorefresh;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * description：这个类定义了Header和Footer的共通行为
 * author：JimG on 16/9/30 14:51
 * e-mail：info@zijinqianbao.com
 */

public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout{
    //容器布局
    private View mContainer;
    //当前状态
    private State mCurState = State.NONE;
    //上一个状态
    private State mPreState = State.NONE;

    /**
     * 构造方法
     * @param context
     */
    public LoadingLayout(Context context) {
        this(context,null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    protected void init(Context context, AttributeSet attrs){
        mContainer = createLoadingView(context,attrs);
        if(null == mContainer){
            throw new NullPointerException("Loading view can not be null.");
        }
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mContainer,params);
    }

    /**
     * 显示或隐藏布局
     * @param show
     */
    public void show(boolean show){
        //状态不发生改变,直接return
        if(show == (View.VISIBLE == getVisibility())){
            return;
        }

        ViewGroup.LayoutParams params = mContainer.getLayoutParams();
        if(null != params){
            params.height = show ? LayoutParams.WRAP_CONTENT : 0;
            setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * 设置最后更新的时间文本
     *
     * @param label 文本
     */
    public void setLastUpdatedLabel(CharSequence label) {

    }

    /**
     * 设置加载中的图片
     *
     * @param drawable 图片
     */
    public void setLoadingDrawable(Drawable drawable) {

    }

    /**
     * 设置拉动的文本，典型的是“下拉可以刷新”
     *
     * @param pullLabel 拉动的文本
     */
    public void setPullLabel(CharSequence pullLabel) {

    }

    /**
     * 设置正在刷新的文本，典型的是“正在刷新”
     *
     * @param refreshingLabel 刷新文本
     */
    public void setRefreshingLabel(CharSequence refreshingLabel) {

    }

    /**
     * 设置释放的文本，典型的是“松开可以刷新”
     *
     * @param releaseLabel 释放文本
     */
    public void setReleaseLabel(CharSequence releaseLabel) {

    }


    @Override
    public void setState(State state) {
        if(mCurState != state){
            mPreState = mCurState;
            mCurState = state;
            onStateChanged();
        }
    }

    @Override
    public State getState() {
        return mCurState;
    }


    @Override
    public void onPull(float scale) {

    }

    /**
     * 得到前一个状态
     *
     * @return 状态
     */
    protected State getPreState() {
        return mPreState;
    }

    /**
     * 状态改变时调用
     */
    protected void onStateChanged(){
        switch (mCurState){

            case RESET:
                onReset();
                break;

            case RELEASE_TO_REFRESH://释放刷新
                onReleaseToRefresh();
                break;

            case PULL_TO_REFRESH://下拉刷新
                onPullToRefresh();
                break;

            case REFRESHING://正在刷新
                onRefreshing();
                break;

            case SUCCESSFUL://刷新成功
                onRefreshSuccess();
                break;

            case NO_MORE_DATA:
                onNoMoreData();
                break;

            default:
                break;
        }

    }

    /**
     * 当状态设置为{@link State#RESET}时调用
     */
    protected void onReset() {

    }

    /**
     * 当状态设置为{@link State#PULL_TO_REFRESH}时调用
     */
    protected void onPullToRefresh() {

    }

    /**
     * 当状态设置为{@link State#RELEASE_TO_REFRESH}时调用
     */
    protected void onReleaseToRefresh() {

    }

    /**
     * 当状态设置为{@link State#REFRESHING}时调用
     */
    protected void onRefreshing() {

    }

    /**
     * 当状态设置为{@link State#SUCCESSFUL}时调用
     */
    protected void onRefreshSuccess(){

    }

    /**
     * 当状态设置为{@link State#NO_MORE_DATA}时调用
     */
    protected void onNoMoreData() {

    }



    @Override
    public abstract int getContentSize();

    /**
     * 创建Loading的View
     *
     * @param context context
     * @param attrs attrs
     * @return Loading的View
     */
    protected abstract View createLoadingView(Context context, AttributeSet attrs);
}
