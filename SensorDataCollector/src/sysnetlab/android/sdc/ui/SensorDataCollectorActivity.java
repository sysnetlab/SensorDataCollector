/* $Id$ */
package sysnetlab.android.sdc.ui;


import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;


public class SensorDataCollectorActivity extends FragmentActivity 
	implements ExperimentListFragment.OnFragmentClickListener {
	
	private ExperimentListFragment mExperimentListFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);

		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			mExperimentListFragment = new ExperimentListFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.fragment_container, mExperimentListFragment);
			transaction.commit();
		} 
	}
	
	public void onStart()
	{
		super.onStart();
	}
	
	@Override
	public void onExperimentClicked_ExperimentListFragment(Experiment experiment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreateExperimentButtonClicked_ExperimentListFragment(Button b) {
		Intent intent = new Intent(this, CreateExperimentActivity.class);
        startActivity(intent);
	}
	
	public ExperimentListFragment getExperimentListFragment()
	{
		return mExperimentListFragment;
	}
	
}
