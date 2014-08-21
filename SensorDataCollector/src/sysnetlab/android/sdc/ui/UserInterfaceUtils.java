/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package sysnetlab.android.sdc.ui;

import java.util.ArrayList;
import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorProperty;
import sysnetlab.android.sdc.sensor.audio.AudioRecordParameter;
import sysnetlab.android.sdc.sensor.audio.AudioSensor;
import sysnetlab.android.sdc.ui.adaptors.SensorPropertyListAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class UserInterfaceUtils {

    private static int mTagsGridNumColumns;

    public static void fillSensorProperties(Activity activity, ListView listView,
            AbstractSensor sensor) {
        UserInterfaceUtils.fillSensorProperties(activity, listView, sensor, false);
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

        switch (sensor.getMajorType()) {
            case AbstractSensor.ANDROID_SENSOR:
                property = new SensorProperty(activity.getResources().getString(
                        R.string.text_vendor),
                        sensor.getVendor());
                lstSensorProperties.add(property);

                property = new SensorProperty(activity.getResources().getString(
                        R.string.text_version),
                        Integer.toString(sensor.getVersion()));
                lstSensorProperties.add(property);

                Sensor aSensor = (Sensor) (((AndroidSensor) sensor).getSensor());

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
                AudioSensor audioSensor = (AudioSensor) sensor;

                AudioRecordParameter param = audioSensor.getAudioRecordParameter();

                property = new SensorProperty(activity.getResources().getString(
                        R.string.text_audio_source), activity.getResources().getString(
                        param.getSource().getSourceNameResId()));
                Log.d("SensorDataCollector", "AUDIO_SENSOR Property: (name, id, resid, string) = " +
                        property.getName() + "," +
                        param.getSource().getSourceId() + "," +
                        param.getSource().getSourceNameResId() + "," +
                        property.getValue());
                lstSensorProperties.add(property);

                property = new SensorProperty(activity.getResources().getString(
                        R.string.text_audio_channel_in), activity.getResources().getString(
                        param.getChannel().getChannelNameResId()));
                lstSensorProperties.add(property);

                property = new SensorProperty(activity.getResources().getString(
                        R.string.text_audio_encoding), activity.getResources().getString(
                        param.getEncoding().getEncodingNameResId()));
                lstSensorProperties.add(property);

                property = new SensorProperty(activity.getResources().getString(
                        R.string.text_audio_sampling_rate), Integer.toString(param
                        .getSamplingRate()));
                lstSensorProperties.add(property);

                property = new SensorProperty(activity.getResources().getString(
                        R.string.text_audio_min_buffer_size), Integer.toString(param
                        .getBufferSize()));
                lstSensorProperties.add(property);

                break;
            case AbstractSensor.CAMERA_SENSOR:
                // TODO: todo ...
                Log.w("SensorDataCollector", "Camera Sensor is a todo.");
                break;
            case AbstractSensor.WIFI_SENSOR:
                // TODO: todo ...
                Log.w("SensorDataCollector", "WiFi Sensor is a todo.");
                break;
            case AbstractSensor.BLUETOOTH_SENSOR:
                // TODO: todo ...
                Log.w("SensorDataCollector", "Bluetooth Sensor is a todo.");
                break;
            default:
                // TODO: todo ...
                Log.w("SensorDataCollector", "unknown sensor. unexpected.");
                break;
        }
        SensorPropertyListAdapter sensorPropertyListAdaptor = new SensorPropertyListAdapter(
                activity, lstSensorProperties);

        listView.setAdapter(sensorPropertyListAdaptor);
    }

    public static void updateAudioSensorPropertyListView(Activity activity, ListView listView,
            AudioRecordParameter param) {
        ArrayList<SensorProperty> listProperties = new ArrayList<SensorProperty>();

        SensorProperty property;

        property = new SensorProperty(activity.getResources().getString(
                R.string.text_audio_source), activity.getResources().getString(
                param.getSource().getSourceNameResId()));
        listProperties.add(property);

        property = new SensorProperty(activity.getResources().getString(
                R.string.text_audio_channel_in), activity.getResources().getString(
                param.getChannel().getChannelNameResId()));
        listProperties.add(property);

        property = new SensorProperty(activity.getResources().getString(
                R.string.text_audio_encoding), activity.getResources().getString(
                param.getEncoding().getEncodingNameResId()));
        listProperties.add(property);

        property = new SensorProperty(activity.getResources().getString(
                R.string.text_audio_sampling_rate), Integer.toString(param
                .getSamplingRate()));
        listProperties.add(property);

        property = new SensorProperty(activity.getResources().getString(
                R.string.text_audio_min_buffer_size), Integer.toString(param.getBufferSize()));
        listProperties.add(property);

        SensorPropertyListAdapter sensorPropertyListAdaptor = new SensorPropertyListAdapter(
                activity, listProperties);

        listView.setAdapter(sensorPropertyListAdaptor);

    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void setViewBackgroundCompatible(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public static void setEllipsizeforTextView(TextView textView, TextUtils.TruncateAt truncateAt) {
        // truncateAt can be any of the 4 values
        /*
         * TextUtils.TruncateAt.END TextUtils.TruncateAt.MARQUEE
         * TextUtils.TruncateAt.MIDDLE TextUtils.TruncateAt.START
         */
        textView.setEllipsize(truncateAt);
        textView.setSingleLine();
        textView.setHorizontallyScrolling(false);
    }

    public static String filterOutNewLines(String text) {
        String[] lines = text.split(System.getProperty("line.separator"));
        String strLongLine = "";
        for (String s : lines) {
            strLongLine += s + " ";
        }
        return strLongLine;
    }

    @SuppressLint("InlinedApi")
    public static boolean bindRunExperimentServiceCompatible(Context context, Intent service,
            ServiceConnection conn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return context.bindService(service, conn, Context.BIND_ABOVE_CLIENT);
        } else {
            return context.bindService(service, conn, Context.BIND_AUTO_CREATE);
        }
    }

    @SuppressLint("NewApi")
    public static int getNumColumnsCompatible(GridView gridView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return gridView.getNumColumns();
        } else {
            if (mTagsGridNumColumns > 0) {
                return mTagsGridNumColumns;
            } else {
                if (gridView.getChildCount() > 0) {
                    int width = gridView.getChildAt(0).getMeasuredWidth();
                    if (width > 0) {
                        mTagsGridNumColumns = gridView.getWidth() / width;
                    }
                }
            }
            return mTagsGridNumColumns > 0 ? mTagsGridNumColumns : GridView.AUTO_FIT;

        }
    }

    @SuppressLint("NewApi")
    public static boolean isInLayoutCompatible(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return view.isInLayout();
        } else {
            return false;
        }
    }

    @SuppressLint("NewApi")
    public static <T> void addAllCompatible(ArrayAdapter<T> adapter, List<T> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            adapter.addAll(list);
        } else {
            adapter.setNotifyOnChange(false);
            for (T e : list) {
                adapter.add(e);
            }
            adapter.setNotifyOnChange(true);
            adapter.notifyDataSetChanged();
        }
    }
}
