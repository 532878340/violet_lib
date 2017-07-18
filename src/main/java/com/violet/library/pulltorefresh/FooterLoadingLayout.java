package com.violet.library.pulltorefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.violet.library.R;


/**
 * description：上拉加载布局
 * author：JimG on 16/9/30 16:20
 * e-mail：info@zijinqianbao.com
 */

public class FooterLoadingLayout extends LoadingLayout{
    //footer容器
    private RelativeLayout mFooterContainer;
    /* 状态提示文本 */
    private TextView mFooterLabel;
    /**进度条*/
    private ImageView imgIcon;
    /**进度条旋转动画*/
    private AnimationDrawable animDrawable;
    /**指示箭头*/
    private ImageView loadArrow;
    /**向上的动画*/
    private Animation mRotateUpAnim;
    /**向下的动画*/
    private Animation mRotateDownAnim;
    /** 旋转动画时间 */
    private static final int ROTATE_ANIM_DURATION = 150;

    public FooterLoadingLayout(Context context) {
        super(context);
        init();
    }

    public FooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        mFooterContainer = (RelativeLayout) findViewById(R.id.footerContainer);
        mFooterLabel = (TextView) findViewById(R.id.tv_footer_label);
        imgIcon = (ImageView) findViewById(R.id.progress);
        animDrawable = (AnimationDrawable) imgIcon.getDrawable();
        loadArrow = (ImageView) findViewById(R.id.loadArrow);

        setState(State.NONE);

        float pivotValue = 0.5f;    // SUPPRESS CHECKSTYLE
        float toDegree = -180f;     // SUPPRESS CHECKSTYLE
        // 初始化旋转动画
        mRotateUpAnim = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(toDegree, 0.0f, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    public int getContentSize() {
        if(null != mFooterContainer){
            return mFooterContainer.getHeight();
        }
        return (int) (getResources().getDisplayMetrics().density * 40);
    }

    @Override
    protected void onStateChanged() {
        imgIcon.setVisibility(View.INVISIBLE);
        loadArrow.setVisibility(View.VISIBLE);
        super.onStateChanged();
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return View.inflate(context, R.layout.pull_refresh_footer,null);
    }

    @Override
    protected void onReset() {
        mFooterLabel.setText(R.string.pull_loading);
        getLoadingAnimation().stop();
    }

    @Override
    protected void onPullToRefresh() {
        if (State.RELEASE_TO_REFRESH == getPreState()) {
            loadArrow.clearAnimation();
            loadArrow.startAnimation(mRotateDownAnim);
        }
        mFooterLabel.setText(R.string.pull_up_text);
    }

    @Override
    protected void onReleaseToRefresh() {
        loadArrow.clearAnimation();
        loadArrow.startAnimation(mRotateUpAnim);
        mFooterLabel.setText(R.string.pull_release_load);
    }

    @Override
    protected void onRefreshing() {
        mFooterLabel.setText(R.string.pull_loading);
        imgIcon.setVisibility(View.VISIBLE);
        loadArrow.clearAnimation();
        loadArrow.setVisibility(View.INVISIBLE);
        getLoadingAnimation().start();
    }

    @Override
    protected void onNoMoreData() {
        mFooterLabel.setText(R.string.pull_no_more_msg);
    }

    /**
     * 获取进度条图片动画
     */
    private AnimationDrawable getLoadingAnimation(){
        if(animDrawable == null){
            animDrawable = (AnimationDrawable) imgIcon.getDrawable();
        }
        return animDrawable;
    }
}
