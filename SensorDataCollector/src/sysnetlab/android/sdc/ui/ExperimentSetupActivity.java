package sysnetlab.android.sdc.ui;


import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.appdata.AppDataSingleton;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ExperimentSetupActivity extends FragmentActivity 
	implements 
		ExperimentSetupFragment.OnFragmentClickListener,
		SensorListFragment.OnFragmentClickListener {
	private ExperimentSetupFragment mExperimentSetupFragment;
	private SensorListFragment mSensorListFragment;
	private Experiment mExperiment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO handle configuration change
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);
		
		mExperiment = new Experiment();
		
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			mExperimentSetupFragment = new ExperimentSetupFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.fragment_container, mExperimentSetupFragment);
			transaction.commit();
		} 		
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
		
		/*
		Intent intent = new Intent(this, CreateExperimentActivity.class);
        startActivity(intent);		
        */
	}

	@Override
	public void onBtnRunClicked_ExperimentSetupFragment(View v) {
		// TODO lazy work for now, more work;
		String name = ((EditText)findViewById(R.id.et_experiment_name)).getText().toString();
		mExperiment.setName(name);
		AppDataSingleton.getInstance().setExperiment(mExperiment);
		
		Intent intent = new Intent(this, CreateExperimentActivity.class);
        startActivity(intent);			
	}

	@Override
	public void onBtnBackClicked_ExperimentSetupFragment() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, SensorDataCollectorActivity.class);
        startActivity(intent);	
	}

	@Override
	public void onSensorClicked_SensorListFragment(AndroidSensor sensor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBtnRunClicked_SensorListFragment(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBtnClearClicked_SensorListFragment() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBtnBackClicked_SensorListFragment() {
		// TODO Auto-generated method stub
		
	}
}
