/* $Id$ */
package sysnetlab.android.sdc.datacollector;

import sysnetlab.android.sdc.datasink.DataSink;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.SystemClock;

public class DataSensorEventListener implements SensorEventListener {
	private DataSink mDataSink;
	private int mSinkPort;

	public DataSensorEventListener(DataSink sink, int port) {
		mDataSink = sink;
		mSinkPort = port;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		/*
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
    		out.print(SystemClock.currentThreadTimeMillis() + ", " 
    				+ SystemClock.elapsedRealtimeNanos());
    	else
		 */
		mDataSink.write(mSinkPort,
				SystemClock.currentThreadTimeMillis() + ", " + SystemClock.elapsedRealtime());	    		
		for (int i = 0; i < event.values.length; i ++)
			mDataSink.write(mSinkPort, ", " + event.values[i]);
		mDataSink.write(mSinkPort, "\n");
	}
}
