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

package sysnetlab.android.sdc.sensor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sysnetlab.android.sdc.datacollector.AndroidSensorEventListener;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.sensor.audio.AudioRecordParameter;
import sysnetlab.android.sdc.sensor.audio.AudioRecordSettingDataSource;
import sysnetlab.android.sdc.sensor.audio.AudioSensor;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorDiscoverer {
    private static Context mContext;
    private static List<AbstractSensor> mListAbstractSensors = null;

    public static void initialize(Context context) {
        mContext = context;
    }

    public static boolean isInitialized() {
        if (mContext == null)
            return false;
        return true;
    }

    public static AbstractSensor constructSensorObject(String sensorName, int majorType,
            int minorType,
            Channel channel, Object object) {
        return constructSensorObject(sensorName, majorType, minorType, -1, channel, object);
    }

    public static AbstractSensor constructSensorObject(String sensorName, int majorType,
            int minorType,
            int id, Channel channel, Object object) {
        if (mContext == null) {
            throw new RuntimeException(
                    "SensorDataCollector::SensorDiscoverer::getSensor(): "
                            + "context has not been initialized. call initialize() first.");
        }

        switch (majorType) {
            case AbstractSensor.ANDROID_SENSOR:
                return constructAndroidSensorObject(sensorName, majorType, minorType, id, channel);
            case AbstractSensor.AUDIO_SENSOR:
                return constructAudioSensorObject(sensorName, majorType, minorType, id, channel,
                        object);
            default:
                throw new RuntimeException(
                        "SensorDataCollector::SensorDiscoverer:constructSensorObject(): "
                                + "unexpected major sensor type = "
                                + majorType);
        }
    }

    public static List<AbstractSensor> discoverSensorList() {
        if (mListAbstractSensors == null) {
            mListAbstractSensors = new ArrayList<AbstractSensor>();

            addAndroidSensorsToList();
            addAudioSensorToList();
        }
        return mListAbstractSensors;
    }

    private static void addAndroidSensorsToList() {
        if (mContext == null) {
            throw new RuntimeException(
                    "SensorDataCollector::SensorDiscoverer::addAndroidSensorsToList(): "
                            + "context has not been initialized. call initialize() first.");
        }

        SensorManager sensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        Iterator<Sensor> iter = sensorList.iterator();
        while (iter.hasNext()) {
            Sensor sensor = (Sensor) iter.next();
            Log.d("SENSOR", "sensor = [" + sensor.getName() + "]");
            AndroidSensor androidSensor = new AndroidSensor(sensor);
            mListAbstractSensors.add(androidSensor);
        }
    }

    private static void addAudioSensorToList() {
        if (mContext == null) {
            throw new RuntimeException(
                    "SensorDataCollector::SensorDiscoverer::addAudioSensorToList(): "
                            + "context has not been initialized. call initialize() first.");
        }

        AudioSensor audioSensor = AudioSensor.getInstance();

        AudioRecordSettingDataSource.initializeInstance(mContext);
        AudioRecordSettingDataSource dbSource = AudioRecordSettingDataSource.getInstance();
        dbSource.open();
        if (!dbSource.isDataSourceReady()) {
            dbSource.prepareDataSource();
        }
        List<AudioRecordParameter> listParams = dbSource.getAllAudioRecordParameters();
        dbSource.close();

        if (listParams == null || listParams.isEmpty()) {
            return;
        }

        audioSensor.setAudioRecordParameter(listParams.get(0));

        mListAbstractSensors.add(audioSensor);
    }

    private static AndroidSensor constructAndroidSensorObject(String sensorName, int majorType,
            int minorType, int id, Channel channel) {

        AndroidSensor androidSensor = new AndroidSensor();

        List<Sensor> lstSensors = ((SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE)).getSensorList(minorType);
        for (Sensor sensor : lstSensors) {
            if (sensor.getName().equals(sensorName)) {
                androidSensor.setSensor(sensor);
                break;
            }
        }
        if (androidSensor.getSensor() == null) {
            throw new RuntimeException(
                    "SensorDataCollector::SensorDiscoverer::constructAndroidSensorObject(): "
                            + "unexpected sensor name = "
                            + sensorName);
        }

        androidSensor.setMajorType(majorType);
        androidSensor.setMinorType(minorType);

        AndroidSensorEventListener listener = new AndroidSensorEventListener(channel);
        androidSensor.setListener(listener);

        androidSensor.setId(id);

        return androidSensor;
    }

    private static AudioSensor constructAudioSensorObject(String sensorName, int majorType,
            int minorType,
            int id, Channel channel, Object object) {

        AudioRecordParameter param = (AudioRecordParameter) object;
        AudioSensor audioSensor = AudioSensor.getInstance();
        audioSensor.setAudioRecordParameter(param);

        if (audioSensor.getAudioRecordParameter() == null) {
            AudioRecordSettingDataSource.initializeInstance(mContext);
            AudioRecordSettingDataSource dbSource = AudioRecordSettingDataSource
                    .getInstance();
            dbSource.open();
            if (dbSource.isDataSourceReady()) {
                List<AudioRecordParameter> listParams = dbSource
                        .getAllAudioRecordParameters();

                if (listParams != null && !listParams.isEmpty())
                    audioSensor.setAudioRecordParameter(listParams.get(0));
            }
            dbSource.close();
        }

        audioSensor.setId(id);
        return audioSensor;
    }
}
