package sysnetlab.android.sdc.ui;


import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.appdata.AppDataSingleton;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ExperimentSetupActivity extends FragmentActivity 
	implements 
		ExperimentSetupFragment.OnFragmentClickListener,
		ExperimentTagsFragment.OnFragmentEventListener,
		SensorListFragment.OnFragmentClickListener,
		ExperimentRunFragment.OnFragmentClickListener,
		ExperimentRunTaggingFragment.OnFragmentClickListener {
	private ExperimentSetupFragment mExperimentSetupFragment;
	private SensorListFragment mSensorListFragment;
	private ExperimentRunFragment mExperimentRunningFragment;
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
		
		/*
		Intent intent = new Intent(this, CreateExperimentActivity.class);
        startActivity(intent);	
        */	
		// TODO lazy work for now, more work ...
		if (mExperimentRunningFragment == null)
			mExperimentRunningFragment = new ExperimentRunFragment();
		FragmentUtil.switchToFragment(this, mExperimentRunningFragment, "sensorlist");			
	}

	@Override
	public void onBtnBackClicked_ExperimentSetupFragment() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, SensorDataCollectorActivity.class);
        startActivity(intent);	
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

	@Override
	public void onBtnDoneClicked_ExperimentRunFragment() {
		Intent intent = new Intent(this, SensorDataCollectorActivity.class);
        startActivity(intent);	
	}

	@Override
	public void onTagClicked_ExperimentRunTaggingFragment(int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void runExperiment(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopExperiment(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
}
