/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

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
        Log.d("SensorDataCollector", "called onSwipeLeft.");
    }

    public void onSwipeRight() {
        Log.d("SensorDataCollector", "called onSwipeRight.");
    }

    public void onSwipeTop() {
        Log.d("SensorDataCollector", "called onSwipeTop.");
    }

    public void onSwipeBottom() {
        Log.d("SensorDataCollector", "called onSwipeBottom.");
    }

    public boolean onTouch(final View v, final MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
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
            float diffX = 0;
            float diffY = 0;
            
            if (e1 != null && e2 != null) {
                diffX = e2.getX() - e1.getX();
                diffY = e2.getY() - e1.getY();
            } 
            
            Log.d("SensorDataCollector", "onFling: diff X = " + diffX + " diff Y = " + diffY);

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
