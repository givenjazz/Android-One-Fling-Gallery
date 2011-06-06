package com.givenjazz.android;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Gallery;

public class OneFlingGallery extends Gallery {
    private float mDeceleration;
    private float mDistanceX;

    public OneFlingGallery(Context context) {
	this(context, null);
    }

    public OneFlingGallery(Context context, AttributeSet attrs) {
	super(context, attrs);
	float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
	mDeceleration = SensorManager.GRAVITY_EARTH // g (m/s^2)
		* 39.37f // inch/meter
		* ppi // pixels per inch
		* ViewConfiguration.getScrollFriction();
    }
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	    float velocityY) {

	float toMoveDistance = getWidth() - Math.abs(mDistanceX);
	float maxVelocity = (float) Math.sqrt(toMoveDistance * mDeceleration
		* 2);
	float revisedVelocityX = 0;

	if (velocityX > 0) {
	    revisedVelocityX = Math.min(velocityX, maxVelocity);
	} else {
	    revisedVelocityX = Math.max(velocityX, -maxVelocity);
	}

	return super.onFling(e1, e2, revisedVelocityX, velocityY);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
	    float distanceY) {
	mDistanceX += distanceX;
	return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    mDistanceX = 0;
	}
	return super.onTouchEvent(event);
    }
}