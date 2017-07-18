package com.violet.library.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.violet.library.R;


/**
 * description：圆形、圆角 ImageView
 * author：JimG on 16/10/25 10:59
 * e-mail：info@zijinqianbao.com
 */

public class CircleImageView extends ImageView {
    /**
     * 绘制画笔
     */
    private Paint mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Bitmap mRawBitmap;
    /**
     * 调用这个方法来产生一个画有一个位图的渲染器（Shader）
     */
    private BitmapShader mShader;
    private Matrix mMatrix = new Matrix();

    /**
     * 边框的颜色
     */
    private int borderColor;
    /**
     * 边框宽度
     */
    private int borderWidth;
    /**
     * 圆角大小
     */
    private int roundRadius;

    /**
     * 类型
     */
    private int mType;

    /**
     * 普通模式
     */
    private static final int TYPE_NONE = 0;
    /**
     * 圆形
     */
    private static final int TYPE_CIRCLE = 1;
    /**
     * 圆角
     */
    private static final int TYPE_ROUND_RECT = 2;

    private RectF mRectBorder = new RectF();
    private RectF mRectBitmap = new RectF();

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        borderColor = typedArray.getColor(R.styleable.CircleImageView_borderColor, Color.WHITE);
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_borderWidth, dip2px(5));

        mType = typedArray.getInt(R.styleable.CircleImageView_type,TYPE_CIRCLE);
        roundRadius = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_rectRoundRadius,dip2px(5));
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap rawBitmap = getBitmap(getDrawable());
        if (rawBitmap != null && mType != TYPE_NONE) {
            int viewMinSize = Math.min(getWidth(), getHeight());
            float dstWidth = mType == TYPE_CIRCLE ? viewMinSize : getWidth();
            float dstHeight = mType == TYPE_CIRCLE ? viewMinSize : getHeight();
            float halfBorderWidth = borderWidth / 2.0f;
            float doubleBorderWidth = borderWidth * 2;

            if (mShader == null || !rawBitmap.equals(mRawBitmap)) {
                mRawBitmap = rawBitmap;//#Shader.TileMode.CLAMP Shader.TileMode.REPEAT Shader.TileMode.MIRROR
                mShader = new BitmapShader(mRawBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            }

            if (mShader != null) {
                mMatrix.setScale((dstWidth - doubleBorderWidth) / rawBitmap.getWidth(), (dstHeight - doubleBorderWidth) / rawBitmap.getHeight());
                mShader.setLocalMatrix(mMatrix);
            }

            mBitmapPaint.setShader(mShader);

            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStrokeWidth(borderWidth);

            if(mType == TYPE_CIRCLE){
                float radius = viewMinSize / 2.0f;
                canvas.drawCircle(radius, radius, radius - borderWidth / 2, mBorderPaint);
                canvas.translate(borderWidth, borderWidth);
                canvas.drawCircle(radius - borderWidth, radius - borderWidth, radius - borderWidth, mBitmapPaint);
            }else if(mType == TYPE_ROUND_RECT){
                mRectBorder.set(halfBorderWidth,halfBorderWidth,dstWidth - halfBorderWidth,dstHeight - halfBorderWidth);
                mRectBitmap.set(0,0,dstWidth - doubleBorderWidth,dstHeight - doubleBorderWidth);
//                float borderRadius = Math.max(roundRadius - halfBorderWidth,0);
//                float bitmapRadius = Math.max(roundRadius - borderWidth,0);
                canvas.drawRoundRect(mRectBorder,roundRadius,roundRadius,mBorderPaint);
                canvas.translate(borderWidth,borderWidth);
                canvas.drawRoundRect(mRectBitmap,roundRadius,roundRadius,mBitmapPaint);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 获取绘制的Bitmap
     *
     * @param drawable 资源drawable
     * @return bitmap
     */
    private Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof ColorDrawable) {
            Rect rect = drawable.getBounds();
            int color = ((ColorDrawable) drawable).getColor();
            Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
            return bitmap;
        } else {
            return null;
        }
    }

    private int dip2px(int dipVal) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dipVal * scale + 0.5f);
    }
}
