
package sysnetlab.android.sdc.sensor;

import java.util.List;

import sysnetlab.android.sdc.datacollector.AndroidSensorEventListener;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class SensorUtil {

    private Context mContext;

    public AbstractSensor getSensor(String sensorName,
            int majorType, int minorType, Channel channel) {
        switch (majorType) {
            case AbstractSensor.ANDROID_SENSOR:
                AndroidSensor androidSensor = new AndroidSensor();

                if (mContext == null)
                    throw new RuntimeException("SensorClassUtil: make sure context is set!");

                List<Sensor> lstSensors = ((SensorManager) mContext
                        .getSystemService(Context.SENSOR_SERVICE)).getSensorList(minorType);
                for (Sensor sensor : lstSensors) {
                    if (sensor.getName().equals(sensorName)) {
                        androidSensor.setSensor(sensor);
                        break;
                    }
                }
                if (androidSensor.getSensor() == null) {
                    throw new RuntimeException("SensorClassUtil: unexpected sensor name = "
                            + sensorName);
                }

                androidSensor.setMajorType(majorType);
                androidSensor.setMinorType(minorType);

                AndroidSensorEventListener listener =
                        new AndroidSensorEventListener(channel);
                androidSensor.setListener(listener);

                return androidSensor;
            default:
                throw new RuntimeException("SensorUtil: unexpected major sensor type = "
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
