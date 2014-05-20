
package sysnetlab.android.sdc.ui;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorProperty;
import sysnetlab.android.sdc.ui.adaptors.SensorPropertyListAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class UserInterfaceUtil {

    public static void fillSensorProperties(Activity activity, ListView listView,
            AbstractSensor sensor) {
        UserInterfaceUtil.fillSensorProperties(activity, listView, sensor, false);
    }

    public static void fillSensorProperties(Activity activity, ListView listView,
            AbstractSensor sensor, boolean displayName) {
        ArrayList<SensorProperty> lstSensorProperties = new ArrayList<SensorProperty>();

        SensorProperty property;

        if (displayName) {
            property = new SensorProperty(activity.getResources().getString(R.string.text_name),
                    sensor.getName());
            lstSensorProperties.add(property);
        }

        property = new SensorProperty(activity.getResources().getString(R.string.text_vendor),
                sensor.getVendor());
        lstSensorProperties.add(property);

        property = new SensorProperty(activity.getResources().getString(R.string.text_version),
                Integer.toString(sensor.getVersion()));
        lstSensorProperties.add(property);

        switch (sensor.getMajorType()) {
            case AbstractSensor.ANDROID_SENSOR:
                Sensor aSensor = (Sensor) (sensor.getSensor());

                property = new SensorProperty(activity.getResources().getString(
                        R.string.text_android_sensor_type),
                        Integer.toString(aSensor.getType()));
                lstSensorProperties.add(property);

                property = new SensorProperty(activity.getResources().getString(
                        R.string.text_resolution),
                        Float.toString(aSensor.getResolution()));
                lstSensorProperties.add(property);

                property = new SensorProperty(activity.getResources().getString(
                        R.string.text_power_consumption),
                        Float.toString(aSensor.getPower()) + " mA");
                lstSensorProperties.add(property);

                property = new SensorProperty(activity.getResources().getString(
                        R.string.text_maximum_range),
                        Float.toString(aSensor.getMaximumRange()));
                lstSensorProperties.add(property);

                if (((AndroidSensor) sensor).isStreamingSensor()) {
                    property = new SensorProperty(activity.getResources().getString(
                            R.string.text_minimum_sample_delay),
                            Float.toString(((AndroidSensor) sensor).getSamplingInterval()));
                } else {
                    property = new SensorProperty(activity.getResources().getString(
                            R.string.text_minimum_sample_delay),
                            activity.getResources().getString(R.string.text_not_applicable));
                }
                lstSensorProperties.add(property);

                break;
            case AbstractSensor.AUDIO_SENSOR:
                Log.i("SensorDataCollector", "Audio Sensor is a todo.");
                // TODO: todo ...
                break;
            case AbstractSensor.CAMERA_SENSOR:
                // TODO: todo ...
                Log.i("SensorDataCollector", "Camera Sensor is a todo.");
                break;
            case AbstractSensor.WIFI_SENSOR:
                // TODO: todo ...
                Log.i("SensorDataCollector", "WiFi Sensor is a todo.");
                break;
            case AbstractSensor.BLUETOOTH_SENSOR:
                // TODO: todo ...
                Log.i("SensorDataCollector", "Bluetooth Sensor is a todo.");
                break;
            default:
                // TODO: todo ...
                Log.i("SensorDataCollector", "unknown sensor. unexpected.");
                break;
        }
        SensorPropertyListAdapter sensorPropertyListAdaptor = new SensorPropertyListAdapter(
                activity, lstSensorProperties);

        listView.setAdapter(sensorPropertyListAdaptor);
    }
    
    
    @SuppressLint("NewApi")
    public static void setViewBackgroundCompatible(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }        
    }
}
