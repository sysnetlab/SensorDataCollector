
package sysnetlab.android.sdc.sensor;

import java.util.List;

import sysnetlab.android.sdc.datacollector.AndroidSensorEventListener;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class SensorUtils {

    private Context mContext;

    public AbstractSensor getSensor(String sensorName, int majorType, int minorType, Channel channel) {
        return getSensor(sensorName, majorType, minorType, -1, channel);
    }

    public AbstractSensor getSensor(String sensorName, int majorType, int minorType, int id,
            Channel channel) {
        switch (majorType) {
            case AbstractSensor.ANDROID_SENSOR:
                AndroidSensor androidSensor = new AndroidSensor();

                if (mContext == null)
                    throw new RuntimeException("SensorUtils: make sure context is set!");

                List<Sensor> lstSensors = ((SensorManager) mContext
                        .getSystemService(Context.SENSOR_SERVICE)).getSensorList(minorType);
                for (Sensor sensor : lstSensors) {
                    if (sensor.getName().equals(sensorName)) {
                        androidSensor.setSensor(sensor);
                        break;
                    }
                }
                if (androidSensor.getSensor() == null) {
                    throw new RuntimeException("SensorUtils: unexpected sensor name = "
                            + sensorName);
                }

                androidSensor.setMajorType(majorType);
                androidSensor.setMinorType(minorType);

                AndroidSensorEventListener listener =
                        new AndroidSensorEventListener(channel);
                androidSensor.setListener(listener);

                androidSensor.setId(id);

                return androidSensor;
            default:
                throw new RuntimeException("SensorUtils: unexpected major sensor type = "
                        + majorType);
        }
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }
}