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
    private static List<AbstractSensor> mListAbstractSensors = null;

    public static List<AbstractSensor> discoverSensorList(Context context) {
        if (mListAbstractSensors == null) {
            mListAbstractSensors = new ArrayList<AbstractSensor>();

            addAndroidSensorsToList(context);
        }
        return mListAbstractSensors;
    }
    
    private static void addAndroidSensorsToList(Context context) {
        SensorManager sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        Iterator<Sensor> iter = sensorList.iterator();
        while (iter.hasNext()) {
            Sensor sensor = (Sensor) iter.next();
            Log.i("SENSOR", "sensor = [" + sensor.getName() + "]");
            AndroidSensor androidSensor = new AndroidSensor(sensor);
            mListAbstractSensors.add(androidSensor);
        }
    }
}
