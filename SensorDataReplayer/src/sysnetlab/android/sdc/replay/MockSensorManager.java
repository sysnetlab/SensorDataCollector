package sysnetlab.android.sdc.replay;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.util.Log;

public class MockSensorManager { //extends SensorManager {
	
	boolean registerListener(SensorEventListener listener, Sensor sensor, int rateUs, int maxBatchReportLatencyUs)
	{
		Log.i("SenseeReplaySensorManager", "in registerListener");
		return true;
	}
	
	boolean registerListener(SensorEventListener listener, Sensor sensor, int rateUs, Handler handler)
	{
		Log.i("SenseeReplaySensorManager", "in registerListener");
		return true;
	}
	
	boolean registerListener(SensorEventListener listener, Sensor sensor, int rateUs, int maxBatchReportLatencyUs, Handler handler)
	{
		Log.i("SenseeReplaySensorManager", "in registerListener");
		return true;
	}
	
	boolean	registerListener(SensorEventListener listener, Sensor sensor, int rateUs)
	{
		Log.i("SenseeReplaySensorManager", "in registerListener");
		return true;
	}
	
	void unregisterListener(SensorEventListener listener, Sensor sensor)
	{
		//Unregisters a listener for the sensors with which it is registered.
	}
	
	void unregisterListener(SensorEventListener listener)
	{
		//Unregisters a listener for all sensors.
	}
	
}
