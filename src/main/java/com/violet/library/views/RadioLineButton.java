package com.violet.library.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.violet.library.R;
import com.violet.library.utils.PhoneUtils;

/**
 * description：带下划线的radiobutton
 * author：JimG on 16/9/30 16:20
 * e-mail：info@zijinqianbao.com
 */
@SuppressLint("DrawAllocation")
public class RadioLineButton extends RadioButton {
	 private static final int LINE_HEIGHT = 2;

	public RadioLineButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RadioLineButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RadioLineButton(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isChecked()){
            Paint p = new Paint();
            p.setColor(Color.rgb(255,186,0));
            float lineHeight = PhoneUtils.dpToPx(getContext(),LINE_HEIGHT);
            float width = getWidth();
            float lineWidth = getPaint().measureText(getText().toString());
            float leftPadding = (width - lineWidth) / 2;
            
            canvas.drawRect(leftPadding, getHeight() - lineHeight, width - leftPadding, getHeight(), p);
		}
	}
}
