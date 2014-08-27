
package sysnetlab.android.sdc.ui;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

public class SwipableRelativeLayout extends RelativeLayout {
    private int mSlop;
    /*
     * private int mMinFlingVelocity; private int mMaxFlingVelocity;
     */
    private float mDownX;
    private float mDownY;
    private boolean mIsVerticalScrolling;

    public SwipableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SwipableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipableRelativeLayout(Context context) {
        super(context);
        init();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        /*
         * determine if it is a scrolling or a swiping. If it is scrolling, pass
         * it to child views
         */
        final int action = motionEvent.getActionMasked();

        Log.d("SensorDataCollector", "SwipableRelativeLayout: motionEvent.action = " + action);

        // Always handle the case of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the swipe.
            mIsVerticalScrolling = true;
            /*
             * Do not intercept touch event, let the child (a TextView & a
             * ListView) handle it
             */
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = motionEvent.getRawX();
                mDownY = motionEvent.getRawY();
                mIsVerticalScrolling = true;
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaX = motionEvent.getRawX() - mDownX;
                float deltaY = motionEvent.getRawY() - mDownY;
                Log.d("SensorDataCollector", "SwipableRelativeLayout finds: "
                        + "deltaX = " + deltaX + " deltaY = " + deltaY
                        + "mSlop = " + mSlop);
                if (Math.abs(deltaX) > mSlop && Math.abs(deltaY) < Math.abs(deltaX)) {
                    // looks like a horizontal-swiping instead of a
                    // vertical-scrolling
                    mIsVerticalScrolling = false;
                }
                break;
        }

        if (mIsVerticalScrolling) {
            Log.d("SensorDataCollector", "SwipableRelativeLayout detects a vertical scroll.");
            super.onInterceptTouchEvent(motionEvent);
            return false;
        } else {
            Log.d("SensorDataCollector", "SwipableRelativeLayout detects horizontal swipe.");
            super.onInterceptTouchEvent(motionEvent);
            return true;
        }
    }

    private void init() {
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        mSlop = vc.getScaledTouchSlop();
        /*
         * mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
         * mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
         */
        mDownX = 0;
        mDownY = 0;
        mIsVerticalScrolling = true;
    }
}
