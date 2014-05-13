package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class ViewExperimentActivity extends FragmentActivity implements
		ExperimentViewFragment.ExperimentViewFragmentHandler,
		ExperimentTagsFragment.OnFragmentEventListener {
	private ExperimentViewFragment mExperimentViewFragment;
	private Experiment mExperiment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO handle configuration change
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);
		
		Intent intent = getIntent();
		mExperiment = intent.getParcelableExtra("experiment");
		if (mExperiment == null)
			Log.i("SensorDataColelctor", "ViewExperimentActivity failed to get experiment from intent");
		Log.i("SensorDataCollector", "ViewExperimentActivity: experiment is " + mExperiment.toString());

		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			mExperimentViewFragment = new ExperimentViewFragment();
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.add(R.id.fragment_container, mExperimentViewFragment);
			transaction.commit();
		}

		Log.i("SensorDataCollector", "ViewExperimentActivity.onCreate called.");
	}
	
	public Experiment getExperiment() {
		return mExperiment;
	}

	@Override
	public void onTxtFldEnterPressed_ExperimentTagsFragment(String newTag) {
		// TODO Auto-generated method stub
		
	}
}
