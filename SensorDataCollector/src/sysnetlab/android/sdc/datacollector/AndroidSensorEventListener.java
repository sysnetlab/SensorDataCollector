
package sysnetlab.android.sdc.datacollector;

import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.SystemClock;

public class AndroidSensorEventListener implements SensorEventListener {
    private Channel mChannel;

    public AndroidSensorEventListener(Channel channel) {
        mChannel = channel;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @SuppressLint("NewApi")
    public void onSensorChanged(SensorEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mChannel.write(
                    SystemClock.currentThreadTimeMillis() + ", "
                            + SystemClock.elapsedRealtime() + ", "
                            + SystemClock.elapsedRealtimeNanos());
        else
            mChannel.write(SystemClock.currentThreadTimeMillis()
                    + ", " + SystemClock.elapsedRealtime());
        for (int i = 0; i < event.values.length; i++)
            mChannel.write(", " + event.values[i]);
        mChannel.write("\n");
    }
    
    public Channel getChannel() {
        return mChannel;
    }
}
