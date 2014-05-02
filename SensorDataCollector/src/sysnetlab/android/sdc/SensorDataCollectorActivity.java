/* $Id$ */
package sysnetlab.android.sdc;


import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.datacollector.DataSensorEventListener;
import sysnetlab.android.sdc.datasink.DataSink;
import sysnetlab.android.sdc.datasink.SimpleFileSink;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.DataSensorFactory;
import sysnetlab.android.sdc.sensor.VirtualSensor;
import sysnetlab.android.sdc.ui.SensorListFragment;
import sysnetlab.android.sdc.ui.SensorSetupFragment;
import sysnetlab.android.sdc.R;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SensorDataCollectorActivity extends FragmentActivity 
implements SensorListFragment.OnClickListener, SensorSetupFragment.OnClickListener {
	
	private SensorManager mSensorManager;
	private SensorListFragment mSensorListFragment;
	private SensorSetupFragment mSensorSetupFragment;
	private DataSink mDataSink;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);

		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			mSensorListFragment = new SensorListFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.fragment_container, mSensorListFragment);
			transaction.commit();
		} 

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mDataSink = new SimpleFileSink();
	}
	
	public void onStart() {
		super.onStart();

		final Button btnRunStop = (Button) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container)
				.getView()
				.findViewById(R.id.btnDataSensorsRun);
		btnRunStop.setText(getResources().getString(R.string.button_run_text_run));
		DataCollectionState.setState(DataCollectionState.DATA_COLLECTION_STOPPED);
		btnRunStop.setTextColor(Color.GREEN);
	}
	
	public void onSensorClicked(AndroidSensor sensor) {
		if (mSensorSetupFragment != null) {
			mSensorSetupFragment.setSensor(sensor);
			switchToFragment(mSensorSetupFragment, "sensorsetup");
		} else {
			mSensorSetupFragment = new SensorSetupFragment();
			mSensorSetupFragment.setSensor(sensor);
			switchToFragment(mSensorSetupFragment, "sensorsetup");
		}
	}	
	
	@Override
	public void onBtnRunClicked(View v) {
		if (DataCollectionState.getState() == DataCollectionState.DATA_COLLECTION_STOPPED) {
			try {
				runDataSensor();
				
				((Button)v).setText(getResources().getString(R.string.button_run_text_stop));
				((Button)v).setTextColor(Color.RED);
				DataCollectionState.setState(DataCollectionState.DATA_COLLECTION_IN_PROGRESS);
			} catch (IOException e) {
				Log.e("SensorDataCollector", e.toString());
			}
			// Toast.makeText(this, "Run Button Pressed", Toast.LENGTH_SHORT).show();	
		} else if (DataCollectionState.getState() == DataCollectionState.DATA_COLLECTION_IN_PROGRESS) {
			try {
				stopDataSensor();
			} catch (IOException e) {
				Log.e("SensorDataCollector", e.toString());
			}
			((Button)v).setText(getResources().getString(R.string.button_run_text_run));
			((Button)v).setTextColor(Color.GREEN);
			DataCollectionState.setState(DataCollectionState.DATA_COLLECTION_STOPPED);
			// Toast.makeText(this, "Stop Button Pressed", Toast.LENGTH_SHORT).show();	
		} else {
			Toast.makeText(this, "Unsupported Button Action", Toast.LENGTH_SHORT).show();					
		}
	}
	
	@Override
	public void onBtnClearClicked() {
		Toast.makeText(this, "Clear Button Pressed", Toast.LENGTH_SHORT).show();		
	}

	@Override
	public void onBtnConfirmClicked(View v, VirtualSensor sensor) {
		Log.i("SensorDataCollector", "SensorSetupFragment: Button Confirm clicked.");
		
		EditText et = (EditText)findViewById(R.id.edittext_sampling_rate);
		
		switch(sensor.getMajorType()) {
		case VirtualSensor.ANDROID_SENSOR:
			AndroidSensor androidSensor = (AndroidSensor)sensor;
			if (androidSensor.isStreamingSensor()) {
				androidSensor.setSamplingInterval((int)(1000000./Double.parseDouble(et.getText().toString())));
			}
			break;
		case VirtualSensor.AUDIO_SENSOR:
			Log.i("SensorDataCollector", "Audio Sensor is a todo.");
			// TODO: todo ...
			break;
		case VirtualSensor.CAMERA_SENSOR:
			// TODO: todo ...	
			Log.i("SensorDataCollector", "Camera Sensor is a todo.");			
			break;
		case VirtualSensor.WIFI_SENSOR:
			// TODO: todo ...		
			Log.i("SensorDataCollector", "WiFi Sensor is a todo.");			
			break;
		case VirtualSensor.BLUETOOTH_SENSOR:
			// TODO: todo ...	
			Log.i("SensorDataCollector", "Bluetooth Sensor is a todo.");			
			break;
		default:
			// TODO: todo ...	
			Log.i("SensorDataCollector", "unknow sensor. unexpected.");			
			break;
		}
		
		switchToFragment(mSensorListFragment, "sensorlist");
	}

	@Override
	public void onBtnCancelClicked() {
		Log.i("SensorDataCollector", "Button Cancel clicked.");
		switchToFragment(mSensorListFragment, "sensorlist");
	} 	
	
	private void runDataSensor() throws IOException {	
		mDataSink.createExperiment();
		
		Iterator<AndroidSensor> iter = DataSensorFactory.getSensorList(this).iterator();
		int nChecked = 0;
		while (iter.hasNext()) {
			AndroidSensor sensor = (AndroidSensor) iter.next();
			if (sensor.isSelected()) {
				nChecked ++;
				/*
				String path = Environment.getExternalStorageDirectory().getPath();
				path = path + "/SensorData";
				File sensorDataDir = new File(path);
				if (!sensorDataDir.exists()) {
					sensorDataDir.mkdir();
				}
				String filename = path + "/" + sensor.getName().replace(' ', '_') + ".txt";
				Log.i("SensorDataCollector", "Saved to " + filename);
				*/
				PrintStream out = mDataSink.open(sensor.getName().replace(' ', '_') + ".txt");
				DataSensorEventListener listener = new DataSensorEventListener(out);
				sensor.setListener(listener);
				mSensorManager.registerListener(listener, (Sensor)sensor.getSensor(), sensor.getSamplingInterval());
			}
		}	
		
		CharSequence text = "Started data collection for " + nChecked + " Sensors";
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();			
	}		
	
	private void stopDataSensor() throws IOException {		
		Iterator<AndroidSensor> iter = DataSensorFactory.getSensorList(this).iterator();
		int nChecked = 0;
		while (iter.hasNext()) {
			AndroidSensor sensor = (AndroidSensor) iter.next();
			if (sensor.isSelected()) {
				nChecked ++;
				DataSensorEventListener listener = sensor.getListener();
				listener.finish();
				mSensorManager.unregisterListener(listener);
			}
		}
		
		mDataSink.close();
		
		CharSequence text = "Stopped data collection for " + nChecked + " Sensors";
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();		
	}	
	
	private void switchToFragment(Fragment fragment, String name) {
		if (fragment != null) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, fragment);
			transaction.addToBackStack(name);
			transaction.commit();			
		} else {
			Log.e("SensorDataCollector", "Oops, this not supposed to happen!");
		}		
	}
}
