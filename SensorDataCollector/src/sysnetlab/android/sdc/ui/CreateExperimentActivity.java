package sysnetlab.android.sdc.ui;

import java.io.IOException;
import java.util.Iterator;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.datacollector.AndroidSensorEventListener;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.datastore.SimpleFileStoreSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateExperimentActivity extends FragmentActivity 
	implements 
		SensorListFragment.OnFragmentClickListener, 
		SensorSetupFragment.OnFragmentClickListener,
		ExperimentSetupFragment.OnFragmentClickListener,
		ExperimentTagsFragment.OnFragmentEventListener,
		ExperimentRunFragment.OnFragmentClickListener,
		ExperimentRunFragment.ExperimentHandler,
		ExperimentRunTaggingFragment.OnFragmentClickListener		
{
	
	private SensorManager mSensorManager;
	
	private ExperimentSetupFragment mExperimentSetupFragment;
	private SensorListFragment mSensorListFragment;
	private SensorSetupFragment mSensorSetupFragment;
	private ExperimentRunFragment mExperimentRunFragment;
	
	private DataCollectionState mCollectionState;
	
	private Experiment mExperiment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO handle configuration change
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);
		
		
		/** 
		 * create an experiment using SimpleFileStore. It can be set
		 * using UI in the future when different types of Store are
		 * corrected. 
		 */
		mExperiment = new Experiment(SimpleFileStoreSingleton.getInstance());
		
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			mExperimentSetupFragment = new ExperimentSetupFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.fragment_container, mExperimentSetupFragment);
			transaction.commit();
		} 			

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mCollectionState = DataCollectionState.DATA_COLLECTION_STOPPED;
		Log.i("SensorDataCollector", "Leaving CreateExperimentActivit::onCreate.");
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
			Log.i("SensorDataCollector", "unknown sensor. unexpected.");			
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
		// TODO lazy work for now, more work ...
		if (mExperimentSetupFragment == null)
			mExperimentSetupFragment = new ExperimentSetupFragment();
		FragmentUtil.switchToFragment(this, mExperimentSetupFragment, "experimentsetup");
	}
	
	public SensorListFragment getSensorListFragment()
	{
		return mSensorListFragment;
	}
	
	public SensorSetupFragment getSensorSetupFragment()
	{
		return mSensorSetupFragment;
	}
	
	public ExperimentSetupFragment getExperimentSetupFragment() {
		return mExperimentSetupFragment;
	}
	
	public DataCollectionState getCurrentCollectionState()
	{
		return mCollectionState;
	}
	
	public Experiment getExperiment() {
		return mExperiment;
	}

	private void runExperiment() throws IOException {
		mExperiment.getStore().addExperiment();
		
		Iterator<AndroidSensor> iter = SensorDiscoverer.discoverSensorList(this).iterator();
		int nChecked = 0;
		while (iter.hasNext()) {
			AndroidSensor sensor = (AndroidSensor) iter.next();
			if (sensor.isSelected()) {
				nChecked ++;

				Channel channel = mExperiment.getStore().getChannel(sensor.getName());
				AndroidSensorEventListener listener = 
						new AndroidSensorEventListener(channel);
				sensor.setListener(listener);
				mSensorManager.registerListener(listener, (Sensor)sensor.getSensor(), sensor.getSamplingInterval());
			}
		}	
		
		CharSequence text = "Started data collection for " + nChecked + " Sensors";
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();			
	}		
	
	private void stopExperiment() throws IOException {		
		Iterator<AndroidSensor> iter = SensorDiscoverer.discoverSensorList(this).iterator();
		int nChecked = 0;
		while (iter.hasNext()) {
			AndroidSensor sensor = (AndroidSensor) iter.next();
			if (sensor.isSelected()) {
				nChecked ++;
				AndroidSensorEventListener listener = sensor.getListener();
				mSensorManager.unregisterListener(listener);
			}
		}
		
		mExperiment.getStore().writeExperimentMetaData(mExperiment);
		
		mExperiment.getStore().closeAllChannels();
		
		CharSequence text = "Stopped data collection for " + nChecked + " Sensors";
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();		
	}

	@Override
	public void onTagClicked_ExperimentRunTaggingFragment(int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBtnDoneClicked_ExperimentRunFragment() {
		Intent intent = new Intent(this, SensorDataCollectorActivity.class);
        startActivity(intent);
	}

	@Override
	public void onTxtFldEnterPressed_ExperimentTagsFragment(String newTag) {
		Log.i("CreateExperiment", "New label being added");
		mExperiment.addTag(newTag);
	}

	@Override
	public void onImvTagsClicked_ExperimentSetupFragment(ImageView v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onImvNotesClicked_ExperimentSetupFragment(ImageView v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onImvSensorsClicked_ExperimentSetupFragment(ImageView v) {
		// TODO lazy work for now, more work ...
		if (mSensorListFragment == null)
			mSensorListFragment = new SensorListFragment();
		FragmentUtil.switchToFragment(this, mSensorListFragment, "sensorlist");
	}

	@Override
	public void onBtnRunClicked_ExperimentSetupFragment(View view) {
		if (mExperimentRunFragment == null)
			mExperimentRunFragment = new ExperimentRunFragment();

		Log.i("SensorDataCollector",
				"CreateExperimentActivity::onBtnRunClicked_ExperimentSetupFragment(): "
						+ view.findViewById(R.id.et_experiment_setup_name));
		mExperiment.setName(((EditText) view
				.findViewById(R.id.et_experiment_setup_name)).getText()
				.toString());

		FragmentUtil.switchToFragment(this, mExperimentRunFragment,
				"experimentrun");
	}

	@Override
	public void onBtnBackClicked_ExperimentSetupFragment() {
		Intent intent = new Intent(this, SensorDataCollectorActivity.class);
        startActivity(intent);	
	}

	@Override
	public void runExperiment_ExperimentRunFragment(View v) {
		if (mCollectionState == DataCollectionState.DATA_COLLECTION_STOPPED) {
			try {
				runExperiment();
				mCollectionState = DataCollectionState.DATA_COLLECTION_IN_PROGRESS;
			} catch (IOException e) {
				Log.e("SensorDataCollector", e.toString());
			}	
		} else {
			Toast.makeText(this, "Unsupported Button Action", Toast.LENGTH_SHORT).show();					
		}
	}

	@Override
	public void stopExperiment_ExperimentRunFragment(View v) {
		if (mCollectionState == DataCollectionState.DATA_COLLECTION_IN_PROGRESS) {
			try {
				stopExperiment();
			} catch (IOException e) {
				Log.e("SensorDataCollector", e.toString());
			}
			mCollectionState = DataCollectionState.DATA_COLLECTION_STOPPED;
		} else {
			Toast.makeText(this, "Unsupported Button Action", Toast.LENGTH_SHORT).show();					
		}		
	}
}
