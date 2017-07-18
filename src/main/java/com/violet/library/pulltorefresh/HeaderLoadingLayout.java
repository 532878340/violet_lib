package com.violet.library.pulltorefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.violet.library.R;

/**
 * description：下拉刷新布局
 * author：JimG on 16/9/30 15:18
 * e-mail：info@zijinqianbao.com
 */

public class HeaderLoadingLayout extends LoadingLayout {
    /** 旋转动画时间 */
    private static final int ROTATE_ANIM_DURATION = 150;
    /**Header的容器*/
    private RelativeLayout mHeaderContainer;
    /**状态提示TextView*/
    private TextView mHeaderLabel;
    /**时间的TextView*/
    private TextView mHeaderTimeTv;
    /**最后更新时间TextView*/
    private TextView mHeaderTimeLabel;
    /**进度条*/
    private ImageView imgIcon;
    /**指示箭头*/
    private ImageView loadArrow;
    /**进度条旋转动画*/
    private AnimationDrawable animDrawable;
    /**向上的动画*/
    private Animation mRotateUpAnim;
    /**向下的动画*/
    private Animation mRotateDownAnim;

    public HeaderLoadingLayout(Context context) {
        super(context);
        init();
    }

    public HeaderLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    /**
     * 初始化操作
     */
    private void init(){
        mHeaderContainer = (RelativeLayout) findViewById(R.id.headerContainer);
        mHeaderLabel = (TextView) findViewById(R.id.tv_header_label);
        mHeaderTimeLabel = (TextView) findViewById(R.id.tv_update_label);
        mHeaderTimeTv = (TextView) findViewById(R.id.tv_time_label);
        imgIcon = (ImageView) findViewById(R.id.progress);
        loadArrow = (ImageView) findViewById(R.id.loadArrow);
        animDrawable = (AnimationDrawable) imgIcon.getDrawable();

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
        if(null != mHeaderContainer){
            return mHeaderContainer.getHeight();
        }
        return (int) (getResources().getDisplayMetrics().density * 60);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return LayoutInflater.from(context).inflate(R.layout.pull_refresh_header,null);
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        //如果最近更新时间文本为空,隐藏前面的标题
        mHeaderTimeLabel.setVisibility(TextUtils.isEmpty(label) ? View.INVISIBLE : View.VISIBLE);
        mHeaderTimeTv.setText(label);
    }

    @Override
    protected void onStateChanged() {
        loadArrow.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.INVISIBLE);
        super.onStateChanged();
    }

    @Override
    protected void onReset() {
        mHeaderLabel.setText(R.string.pull_down_text);
        getLoadingAnimation().stop();
    }

    @Override
    protected void onPullToRefresh() {
        if (State.RELEASE_TO_REFRESH == getPreState()) {
            loadArrow.clearAnimation();
            loadArrow.startAnimation(mRotateDownAnim);
        }
        mHeaderLabel.setText(R.string.pull_down_text);
    }

    @Override
    protected void onReleaseToRefresh() {
        loadArrow.clearAnimation();
        loadArrow.startAnimation(mRotateUpAnim);
        mHeaderLabel.setText(R.string.pull_release_text);
    }

    @Override
    protected void onRefreshing() {
        loadArrow.clearAnimation();
        loadArrow.setVisibility(View.INVISIBLE);
        imgIcon.setVisibility(View.VISIBLE);

        mHeaderLabel.setText(R.string.pull_refreshing);
        getLoadingAnimation().start();
    }

    @Override
    protected void onRefreshSuccess() {
        mHeaderLabel.setText(R.string.pull_succ);
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
