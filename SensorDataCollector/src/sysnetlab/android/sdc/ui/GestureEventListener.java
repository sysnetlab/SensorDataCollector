
package sysnetlab.android.sdc.ui;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;

public abstract class GestureEventListener implements OnTouchListener {

    private final int mMinimumDistance;
    private final int mMinimumFlingVelocity;
    private final int mMaximumFlingVelocity;

    private final GestureDetector mGestureDetector;

    public void onSwipeLeft() {
        Log.i("SensorDataCollector", "called onSwipeLeft.");
    }

    public void onSwipeRight() {
        Log.i("SensorDataCollector", "called onSwipeRight.");
    }

    public void onSwipeTop() {
        Log.i("SensorDataCollector", "called onSwipeTop.");
    }

    public void onSwipeBottom() {
        Log.i("SensorDataCollector", "called onSwipeBottom.");
    }

    public boolean onTouch(final View v, final MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public GestureEventListener(Activity activity) {
        final ViewConfiguration vc = ViewConfiguration.get(activity);

        mMinimumDistance = vc.getScaledTouchSlop();
        mMinimumFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = vc.getScaledMaximumFlingVelocity();

        mGestureDetector = new GestureDetector(activity, new OnGestureListener());
    }

    private final class OnGestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > mMinimumDistance &&
                        Math.abs(velocityX) > mMinimumFlingVelocity &&
                        Math.abs(velocityX) < mMaximumFlingVelocity) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                }
            } else {
                if (Math.abs(diffY) > mMinimumDistance &&
                        Math.abs(velocityY) > mMinimumFlingVelocity &&
                        Math.abs(velocityY) < mMaximumFlingVelocity) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
            }
            return false;
        }
    }
}
