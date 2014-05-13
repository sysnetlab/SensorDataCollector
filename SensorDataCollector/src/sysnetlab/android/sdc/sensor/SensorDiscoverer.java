/* $Id$ */
package sysnetlab.android.sdc.sensor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorDiscoverer {
	private static List<AndroidSensor> mList = null;
	
	public static List<AndroidSensor> discoverSensorList(Context ctx) {
		if (mList == null) {
			SensorManager sensorManager = (SensorManager)ctx.getSystemService(Context.SENSOR_SERVICE);	
			List<Sensor> slist = sensorManager.getSensorList(Sensor.TYPE_ALL);	
			mList = new ArrayList<AndroidSensor>();
			
			Iterator<Sensor> iter = slist.iterator();
			while (iter.hasNext()) {
				Sensor sensor = (Sensor) iter.next();
				Log.i("SENSOR", "sensor = [" + sensor.getName() + "]");
				AndroidSensor dsensor = new AndroidSensor(sensor);
				mList.add(dsensor);
			}
		}
		return mList;
	}
}
