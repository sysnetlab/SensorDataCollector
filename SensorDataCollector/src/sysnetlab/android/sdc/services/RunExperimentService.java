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

package sysnetlab.android.sdc.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import sysnetlab.android.sdc.datacollector.AndroidSensorEventListener;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.sensor.audio.AudioSensor;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class RunExperimentService extends Service {

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public RunExperimentService getService() {
            return RunExperimentService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SensorDataCollector", "LocalService::RunExperimentService::Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        // Tell the user we stopped.
        // Toast.makeText(this, R.string.text_local_service_stopped,
        // Toast.LENGTH_SHORT).show();
    }

    private boolean mIsExperimentRunning = false;

    public void runExperimentInService(SensorManager sensorManager, Experiment experiment) {
        if (mIsExperimentRunning) {
            return;
        }

        StoreSingleton.getInstance().setupNewExperimentStorage(null);

        if (!SensorDiscoverer.isInitialized()) SensorDiscoverer.initialize(getApplicationContext());
        Iterator<AbstractSensor> iter = SensorDiscoverer.discoverSensorList().iterator();
        ArrayList<AbstractSensor> lstSensors = new ArrayList<AbstractSensor>();

        while (iter.hasNext()) {
            AbstractSensor abstractSensor = (AbstractSensor) iter.next();
            if (!abstractSensor.isSelected()) {
                continue;
            }

            Channel channel;
            switch (abstractSensor.getMajorType()) {
                case AbstractSensor.ANDROID_SENSOR:
                    AndroidSensor sensor = (AndroidSensor) abstractSensor;
                    
                    Log.d("SensorDataCollector",
                            "RunExperimentService::runExperimentInService(): process sensor "
                                    + sensor.getName());

                    channel = StoreSingleton.getInstance().createChannel(sensor.getName(), Channel.WRITE_ONLY, Channel.CHANNEL_TYPE_CSV);
                    AndroidSensorEventListener listener =
                            new AndroidSensorEventListener(channel);
                    sensor.setListener(listener);
                    listener.getChannel().open();
                    sensorManager.registerListener(listener, (Sensor) sensor.getSensor(),
                            sensor.getSamplingInterval());
                    lstSensors.add(sensor);
                    break;
                case AbstractSensor.AUDIO_SENSOR:
                    AudioSensor audioSensor = (AudioSensor) abstractSensor;

                    Log.d("SensorDataCollector",
                            "RunExperimentService::runExperimentInService(): process sensor "
                                    + audioSensor.getName());

                    channel = StoreSingleton.getInstance().createChannel(audioSensor.getName(),
                            Channel.WRITE_ONLY, Channel.CHANNEL_TYPE_WAV);
                    channel.open();
                    audioSensor.setChannel(channel);

                    audioSensor.start();

                    lstSensors.add(audioSensor);

                    break;
                default:
                    Log.e("SensorDataCollector",
                            "RunExperimentService::runExperimentInService(): process sensor with major type "
                                    + abstractSensor.getMajorType() + ", but not implemented");
                    break;
            }
        }
        experiment.setSensors(lstSensors);

        mIsExperimentRunning = true;
    }

    public void stopExperimentInService(SensorManager sensorManager, Experiment experiment) {
        if (!mIsExperimentRunning) {
            return;
        }

        if (!SensorDiscoverer.isInitialized()) SensorDiscoverer.initialize(getApplicationContext());
        Iterator<AbstractSensor> iter = SensorDiscoverer.discoverSensorList().iterator();

        while (iter.hasNext()) {
            AbstractSensor abstractSensor = (AbstractSensor) iter.next();
            if (!abstractSensor.isSelected()) {
                continue;
            }

            switch (abstractSensor.getMajorType()) {
                case AbstractSensor.ANDROID_SENSOR:
                    AndroidSensor sensor = (AndroidSensor) abstractSensor;

                    AndroidSensorEventListener listener = sensor.getListener();
                    sensorManager.unregisterListener(listener);
                    break;
                case AbstractSensor.AUDIO_SENSOR:
                    AudioSensor audioSensor = (AudioSensor) abstractSensor;
                    audioSensor.stop();
                    break;
                default:
                    Log.e("SensorDataCollector",
                            "RunExperimentService::stopExperimentInService(): process sensor with major type "
                                    + abstractSensor.getMajorType() + ", but not implemented");
                    break;
            }
        }

        experiment.setDateTimeDone(Calendar.getInstance().getTime());
        experiment.setPath(StoreSingleton.getInstance().getNewExperimentPath());

        StoreSingleton.getInstance().writeExperimentMetaData(experiment);

        StoreSingleton.getInstance().closeAllChannels();

        mIsExperimentRunning = false;
    }

    public boolean isExperimentRunning() {
        return mIsExperimentRunning;
    }
}
