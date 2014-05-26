
package sysnetlab.android.sdc.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.AndroidSensorEventListener;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class RunExperimentService extends Service {
    private final int NOTIFICATION_ID = R.string.text_local_service_started;

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
    public void onCreate() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting. We put an icon in the
        // status bar.
        showNotification();
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
        // Cancel the persistent notification.
        mNotificationManager.cancel(NOTIFICATION_ID);

        // Tell the user we stopped.
        // Toast.makeText(this, R.string.text_local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // The PendingIntent to launch our activity if the user selects this
        // notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, CreateExperimentActivity.class), 0);

        // In this sample, we'll use the same text for the ticker and the
        // expanded notification
        CharSequence text = getText(R.string.text_local_service_started);        
        
        Notification notification = (new NotificationCompat.Builder(this))
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.icon_sensors)
                .setContentTitle(getText(R.string.app_name))
                .setContentText(text)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .build();
        
        // Send the notification.
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private NotificationManager mNotificationManager;
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

        experiment.setDateTimeDone(SimpleDateFormat.getDateTimeInstance().format(
                Calendar.getInstance().getTime()));

        StoreSingleton.getInstance().writeExperimentMetaData(experiment);

        StoreSingleton.getInstance().closeAllChannels();

        mIsExperimentRunning = false;
    }

    public boolean isExperimentRunning() {
        return mIsExperimentRunning;
    }
}
