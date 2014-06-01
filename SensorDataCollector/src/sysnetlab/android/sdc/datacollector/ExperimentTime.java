
package sysnetlab.android.sdc.datacollector;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.SystemClock;

public class ExperimentTime {
    private long mThreadTimeMillis; /*
                                     * Returns milliseconds running in the
                                     * current thread. Added in API level 1.
                                     */
    private long mElapsedRealtime; /*
                                    * Returns milliseconds since boot, including
                                    * time spent in sleep. Added in API level 1.
                                    */
    private long mElapsedRealtimeNanos; /*
                                         * Returns nanoseconds since boot,
                                         * including time spent in sleep. Added
                                         * in API level 17.
                                         */

    @SuppressLint("NewApi")
    public ExperimentTime() {
        mThreadTimeMillis = SystemClock.currentThreadTimeMillis();
        mElapsedRealtime = SystemClock.elapsedRealtime();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        } else {
            mElapsedRealtimeNanos = -1l;
        }
    }
    
    public ExperimentTime(long threadTimeMillis, long elapsedRealtime, long elapsedRealtimeNanos) {
        mThreadTimeMillis = threadTimeMillis;
        mElapsedRealtime = elapsedRealtime;
        mElapsedRealtimeNanos = elapsedRealtimeNanos;
    }

    public ExperimentTime(long threadTimeMillis, long elapsedRealtime) {
        this(threadTimeMillis, elapsedRealtime, -1);
    }

    public long getThreadTimeMillis() {
        return mThreadTimeMillis;
    }

    public void setThreadTimeMillis(long threadTimeMillis) {
        mThreadTimeMillis = threadTimeMillis;
    }

    public long getElapsedRealtime() {
        return mElapsedRealtime;
    }

    public void setElapsedRealtime(long elapsedRealtime) {
        mElapsedRealtime = elapsedRealtime;
    }

    public long getElapsedRealtimeNanos() {
        return mElapsedRealtimeNanos;
    }

    public void setmElapsedRealtimeNanos(long mElapsedRealtimeNanos) {
        this.mElapsedRealtimeNanos = mElapsedRealtimeNanos;
    }

    public String toString() {
        return Long.toString(mThreadTimeMillis) + "\n" + Long.toString(mElapsedRealtime) + "\n"
                + mElapsedRealtimeNanos;
    }
    
    public boolean equals(Object rhs) {
        if (rhs == this) {
            return true;
        }
        
        if (!(rhs instanceof ExperimentTime)) {
            return false;
        }
        
        ExperimentTime t = (ExperimentTime) rhs;
        
        if (mThreadTimeMillis != t.mThreadTimeMillis) {
            return false;
        }
        
        if (mElapsedRealtime != t.mElapsedRealtime) {
            return false;
        }
        
        if (mElapsedRealtimeNanos != t.mElapsedRealtimeNanos) {
            return false;
        }
        
        return true;
    }
}
