package sysnetlab.android.sdc.ui;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.datacollector.DataSensorEventListener;
import sysnetlab.android.sdc.datasink.DataSinkSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CreateExperimentActivity extends FragmentActivity 
	implements SensorListFragment.OnFragmentClickListener, SensorSetupFragment.OnFragmentClickListener,
	ExperimentTagsFragment.OnFragmentEventListener {
	
	private SensorManager mSensorManager;
	
	private SensorListFragment mSensorListFragment;
	private SensorSetupFragment mSensorSetupFragment;
	private ExperimentTagsFragment mExperimentLabelFragment;
	
	private DataCollectionState mCollectionState;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);

		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			mSensorListFragment = new SensorListFragment();
			transaction.add(R.id.sensor_list, mSensorListFragment);
			mExperimentLabelFragment = new ExperimentTagsFragment();
			transaction.add(R.id.experiment_labels, mExperimentLabelFragment);
			transaction.commit();	
		} 
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mCollectionState = DataCollectionState.DATA_COLLECTION_STOPPED;
	}

	public void onStart()
	{
		super.onStart();
		
		final Button btnRunStop = (Button) findViewById(R.id.btnDataSensorsRun);
		btnRunStop.setText(getResources().getString(R.string.button_run_text_run));
		btnRunStop.setTextColor(Color.GREEN);
	}
	
	public void onSensorClicked_SensorListFragment(AndroidSensor sensor) {
		if (mSensorSetupFragment == null) {
			mSensorSetupFragment = new SensorSetupFragment();
		}
		mSensorSetupFragment.setSensor(sensor);
		FragmentUtil.switchToFragment(this, mSensorSetupFragment, "sensorsetup");
	}	
	
	@Override
	public void onBtnRunClicked_SensorListFragment(View v) {
		if (mCollectionState == DataCollectionState.DATA_COLLECTION_STOPPED) {
			try {
				runDataSensor();
				
				((Button)v).setText(getResources().getString(R.string.button_run_text_stop));
				((Button)v).setTextColor(Color.RED);
				
				mCollectionState = DataCollectionState.DATA_COLLECTION_IN_PROGRESS;
			} catch (IOException e) {
				Log.e("SensorDataCollector", e.toString());
			}
			// Toast.makeText(this, "Run Button Pressed", Toast.LENGTH_SHORT).show();	
		} else if (mCollectionState == DataCollectionState.DATA_COLLECTION_IN_PROGRESS) {
			try {
				stopDataSensor();
			} catch (IOException e) {
				Log.e("SensorDataCollector", e.toString());
			}
			((Button)v).setText(getResources().getString(R.string.button_run_text_run));
			((Button)v).setTextColor(Color.GREEN);
			
			mCollectionState = DataCollectionState.DATA_COLLECTION_STOPPED;
			// Toast.makeText(this, "Stop Button Pressed", Toast.LENGTH_SHORT).show();	
		} else {
			Toast.makeText(this, "Unsupported Button Action", Toast.LENGTH_SHORT).show();					
		}
	}
	
	@Override
	public void onBtnClearClicked_SensorListFragment() {
		Toast.makeText(this, "Clear Button Pressed", Toast.LENGTH_SHORT).show();		
	}

	@Override
	public void onBtnConfirmClicked_SensorSetupFragment(View v, AbstractSensor sensor) {
		Log.i("SensorDataCollector", "SensorSetupFragment: Button Confirm clicked.");
		
		EditText et = (EditText)findViewById(R.id.edittext_sampling_rate);
		
		switch(sensor.getMajorType()) {
		case AbstractSensor.ANDROID_SENSOR:
			AndroidSensor androidSensor = (AndroidSensor)sensor;
			if (androidSensor.isStreamingSensor()) {
				androidSensor.setSamplingInterval((int)(1000000./Double.parseDouble(et.getText().toString())));
			}
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
			Log.i("SensorDataCollector", "unknow sensor. unexpected.");			
			break;
		}
		
		FragmentUtil.switchToFragment(this, mSensorListFragment, "sensorlist");
	}

	@Override
	public void onBtnCancelClicked_SensorSetupFragment() {
		Log.i("SensorDataCollector", "Button Cancel clicked.");
		FragmentUtil.switchToFragment(this, mSensorListFragment, "sensorlist");
	} 	
	
	@Override
	public void onBtnBackClicked_SensorListFragment() {
		Intent intent = new Intent(this, ExperimentSetupActivity.class);
        startActivity(intent);
	}
	
	public SensorListFragment getSensorListFragment()
	{
		return mSensorListFragment;
	}
	
	public SensorSetupFragment getSensorSetupFragment()
	{
		return mSensorSetupFragment;
	}
	
	public DataCollectionState getCurrentCollectionState()
	{
		return mCollectionState;
	}
	
	private void runDataSensor() throws IOException {	
		DataSinkSingleton.getInstance().createExperiment();
		
		Iterator<AndroidSensor> iter = SensorDiscoverer.discoverSensorList(this).iterator();
		int nChecked = 0;
		while (iter.hasNext()) {
			AndroidSensor sensor = (AndroidSensor) iter.next();
			if (sensor.isSelected()) {
				nChecked ++;

				PrintStream out = DataSinkSingleton.getInstance().open(sensor.getName().replace(' ', '_') + ".txt");
				DataSensorEventListener listener = new DataSensorEventListener(out);
				sensor.setListener(listener);
				mSensorManager.registerListener(listener, (Sensor)sensor.getSensor(), sensor.getSamplingInterval());
			}
		}	
		
		CharSequence text = "Started data collection for " + nChecked + " Sensors";
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();			
	}		
	
	private void stopDataSensor() throws IOException {		
		Iterator<AndroidSensor> iter = SensorDiscoverer.discoverSensorList(this).iterator();
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
		
		DataSinkSingleton.getInstance().close();
		
		CharSequence text = "Stopped data collection for " + nChecked + " Sensors";
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();		
	}

	@Override
	public void onTxtFldEnterPressed_ExperimentTagsFragment() {
		Log.i("CreateExperiment", "New label being added");
		EditText et = (EditText)findViewById(R.id.edittext_new_label);
		LinearLayout ll = (LinearLayout) findViewById(R.id.layout_label_list);
		Button btnLabel = new Button(this);
		btnLabel.setText(et.getText());
		ll.addView(btnLabel);
	}

	@Override
	public void onBtnLabelClicked_ExperimentTagsFragment() {
		// TODO Auto-generated method stub
		
	}	
}
