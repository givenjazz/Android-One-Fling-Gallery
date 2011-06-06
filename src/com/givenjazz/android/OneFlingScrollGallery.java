
package com.givenjazz.android;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Gallery;

public class OneFlingScrollGallery extends Gallery {
    private static final int NOTHING = 0;
    private static final int HORIZONTAL = 1;
    private static final int VERTICAL = 2;

    private float mSensitivity;
    
    private float mDownX;
    private float mDownY;
    private boolean mNeedToPosition;
    private boolean mNeedToJudge;
    private int mDirection;

    private float mDistanceX;
    private float mDeceleration;

    public OneFlingScrollGallery(Context context) {
        this(context, null);
    }

    public OneFlingScrollGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        mSensitivity = ppi/15;
        mDeceleration = SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f // inch/meter
                * ppi // pixels per inch
                * ViewConfiguration.getScrollFriction();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        float toMoveDistance = getWidth() - Math.abs(mDistanceX);
        float maxVelocity = (float)Math.sqrt(toMoveDistance * mDeceleration * 2);
        float revisedVelocityX = 0;

        if (velocityX > 0) {
            revisedVelocityX = Math.min(velocityX, maxVelocity);
        } else {
            revisedVelocityX = Math.max(velocityX, -maxVelocity);
        }

        return super.onFling(e1, e2, revisedVelocityX, velocityY);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        if (mNeedToPosition) {
            mDistanceX = 0;
            mNeedToPosition = false;
            distanceX = 0;
        }
        mDistanceX += distanceX;
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (mDirection) {
            case HORIZONTAL:
                return super.onTouchEvent(e);
            case VERTICAL:
                if (mNeedToJudge == true) {
                    mNeedToJudge = false;
                    e.setAction(MotionEvent.ACTION_DOWN);
                }

                getSelectedView().onTouchEvent(e);
                return true;
            case NOTHING:
                float deltaX = Math.abs(e.getX() - mDownX);
                float deltaY = Math.abs(e.getY() - mDownY);
                if (deltaX > deltaY + mSensitivity)
                    mDirection = HORIZONTAL;
                else if (deltaX + mSensitivity < deltaY)
                    mDirection = VERTICAL;

        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        mDirection = NOTHING;
        mNeedToPosition = true;
        mNeedToJudge = true;
        mDownX = e.getX();
        mDownY = e.getY();
        return true;
    }

}
