package com.violet.library.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.violet.library.base.BaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * description：自动垂直滚动的TextView
 * author：JimG on 17/3/13 09:34
 * e-mail：info@zijinqianbao@qq.com
 */


public class AutoVerticalScrollTextView<T> extends TextSwitcher implements TextSwitcher.ViewFactory{
    private Context mCtx;
    /**
     * mInUp,mOutUp分别构成翻页的进出动画
     */
    private SwitchAnimation mInAnim,mOutAnim;

    /**
     * 动画持续时间
     */
    private static final long ANIM_DURATION = 1500;
    /**
     * 文本字体大小
     */
    private static final int TEXT_SIZE = 14;

    /**
     * 轮播延迟
     */
    private final static long DELAY = 5000;

    private static final int EMPTY_MSG = -1;

    /**
     * 当前显示索引
     */
    private int index;

    private List<T> contentList = new ArrayList<>();

    public AutoVerticalScrollTextView(Context context) {
        this(context,null);
    }

    public AutoVerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCtx = context;
        mHandler = new ScrollHandler((Activity) mCtx);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        setFactory(this);
        mInAnim = createAnim(true,true);
        mOutAnim = createAnim(false,true);

        setInAnimation(mInAnim);//当View显示时动画资源
        setOutAnimation(mOutAnim);//当View隐藏是动画资源

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_lis != null){
                    _lis.onAutoTextViewClick(contentList.get(index % contentList.size()),index % contentList.size());
                }
            }
        });
    }

    /**
     * 创建切换动画
     * @param turnIn 是否为进入动画
     * @param turnUp 是否向上切换
     * @return
     */
    private SwitchAnimation createAnim(boolean turnIn,boolean turnUp){
        SwitchAnimation animation = new SwitchAnimation(turnIn,turnUp);
        animation.setDuration(ANIM_DURATION);
        animation.setFillAfter(true);
        animation.setInterpolator(new AccelerateInterpolator());
        return animation;
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(mCtx);
        textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,TEXT_SIZE);
        textView.setTextColor(Color.rgb(252,126,0));
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        return textView;
    }

    public void next(){
        if(getInAnimation() != mInAnim){
            setInAnimation(mInAnim);
        }
        if(getOutAnimation() != mOutAnim){
            setOutAnimation(mOutAnim);
        }
    }

    class SwitchAnimation extends Animation{
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;

        SwitchAnimation(boolean turnIn, boolean turnUp){
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterX = getWidth();
            mCenterY = getHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float centerX = mCenterX ;
            final float centerY = mCenterY ;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1: -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
            }
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }

    /**
     * 设置滚动数据
     * @param list
     */
    public AutoVerticalScrollTextView setFillContentList(@NonNull List<T> list){
        contentList.addAll(list);
        return this;
    }

    /**
     * 启动轮播效果
     */
    public void start(){
        if(_lis == null) return;
        setText(_lis.getItemValue(contentList.get(0)));
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(EMPTY_MSG,DELAY);
    }

    /**
     * 关闭轮播效果
     */
    public void end(){
        mHandler.removeCallbacksAndMessages(null);
    }

    private AutoScrollTextListener _lis;

    /**
     * 设置轮播监听
     */
    public AutoVerticalScrollTextView setAutoScrollTextListener(@NonNull AutoScrollTextListener lis){
        this._lis = lis;
        return this;
    }

    private ScrollHandler mHandler;

    class ScrollHandler extends BaseHandler {

        public ScrollHandler(Activity activity) {
            super(activity);
        }

        @Override
        public void handMessage(Message msg) {
            if(_lis != null){
                next();
                index ++ ;
                setText(_lis.getItemValue(contentList.get(index % contentList.size())));
                sendEmptyMessageDelayed(EMPTY_MSG,DELAY);
            }
        }
    }

    public interface AutoScrollTextListener<T>{
        /**
         * 点击事件
         */
        void onAutoTextViewClick(T entity,int pos);

        /**
         * 显示的文本值
         */
        CharSequence getItemValue(T entity);
    }
}
