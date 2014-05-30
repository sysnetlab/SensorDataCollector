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
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        // Tell the user we stopped.
        // Toast.makeText(this, R.string.text_local_service_stopped, Toast.LENGTH_SHORT).show();
    }
    
    private boolean mIsExperimentRunning = false;

    public void runExperimentInService(SensorManager sensorManager, Experiment experiment) {
        if (mIsExperimentRunning) {
            return;
        }

        StoreSingleton.getInstance().setupNewExperimentStorage(null);

        Iterator<AbstractSensor> iter = SensorDiscoverer.discoverSensorList(this).iterator();
        ArrayList<AbstractSensor> lstSensors = new ArrayList<AbstractSensor>();

        while (iter.hasNext()) {
            AndroidSensor sensor = (AndroidSensor) iter.next();
            if (sensor.isSelected()) {
                Log.i("SensorDataCollector",
                        "RunExperimentService::onHandleIntent(): process sensor "
                                + sensor.getName());

                Channel channel = StoreSingleton.getInstance().createChannel(sensor.getName());
                AndroidSensorEventListener listener =
                        new AndroidSensorEventListener(channel);
                sensor.setListener(listener);
                sensorManager.registerListener(listener, (Sensor) sensor.getSensor(),
                        sensor.getSamplingInterval());
                lstSensors.add(sensor);
            }
        }
        experiment.setSensors(lstSensors);

        mIsExperimentRunning = true;
    }

    public void stopExperimentInService(SensorManager sensorManager, Experiment experiment) {
        if (!mIsExperimentRunning) {
            return;
        }

        Iterator<AbstractSensor> iter = SensorDiscoverer.discoverSensorList(this).iterator();

        while (iter.hasNext()) {
            AndroidSensor sensor = (AndroidSensor) iter.next();
            if (sensor.isSelected()) {
                AndroidSensorEventListener listener = sensor.getListener();
                sensorManager.unregisterListener(listener);
            }
        }

        experiment.setDateTimeDone(Calendar.getInstance().getTime());

        StoreSingleton.getInstance().writeExperimentMetaData(experiment);

        StoreSingleton.getInstance().closeAllChannels();

        mIsExperimentRunning = false;
    }

    public boolean isExperimentRunning() {
        return mIsExperimentRunning;
    }
}
