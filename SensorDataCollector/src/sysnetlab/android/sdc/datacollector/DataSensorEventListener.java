/* $Id$ */
package sysnetlab.android.sdc.datacollector;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.SystemClock;

public class DataSensorEventListener implements SensorEventListener {
	PrintStream mOut;

	DataSensorEventListener(String filename) throws IOException {
		mOut = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
	}
	
	public DataSensorEventListener(PrintStream out) throws IOException {
		mOut = out;
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
		mOut.print(SystemClock.currentThreadTimeMillis() + ", " 
				+ SystemClock.elaps