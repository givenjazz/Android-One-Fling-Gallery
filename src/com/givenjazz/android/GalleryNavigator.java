
package com.givenjazz.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GalleryNavigator extends View {
    private static final int SPACING = 6;
    private static final int RADIUS = 4;
    private int mSize = 5;
    private int mPosition = 0;
    private static final Paint mOnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);;
    private static final Paint mOffPaint = new Paint(Paint.ANTI_ALIAS_FLAG);;

    public GalleryNavigator(Context context) {
        super(context);
        mOnPaint.setColor(0xCCFFFFFF);
        mOffPaint.setColor(0x66FFFFFF);
    }

    public GalleryNavigator(Context c, int size) {
        this(c);
        mSize = size;
    }

    public GalleryNavigator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mOnPaint.setColor(0xCCFFFFFF);
        mOffPaint.setColor(0x66FFFFFF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mSize; ++i) {
            if (i == mPosition) {
                canvas.drawCircle(i * (2 * RADIUS + SPACING) + RADIUS, RADIUS, RADIUS, mOnPaint);
            } else {
                canvas.drawCircle(i * (2 * RADIUS + SPACING) + RADIUS, RADIUS, RADIUS, mOffPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSize * (2 * RADIUS + SPACING) - SPACING, 2 * RADIUS);
    }
    
    

//    @Override
//	public boolean isInEditMode() {
//		return false;
//	}

	public void setPosition(int id) {
        mPosition = id;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public void setPaints(int onColor, int offColor) {
        mOnPaint.setColor(onColor);
        mOffPaint.setColor(offColor);
    }

    public void setBlack() {
        setPaints(0xE6000000, 0x66000000);
    }

}
