package sysnetlab.android.sdc.ui;


import sysnetlab.android.sdc.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

public class ExperimentSetupActivity extends FragmentActivity 
	implements ExperimentSetupFragment.OnFragmentClickListener {
	private ExperimentSetupFragment mExperimentSetupFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO handle configuration change
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);
		
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
		Intent intent = new Intent(this, CreateExperimentActivity.class);
        startActivity(intent);		
	}

	@Override
	public void onBtnRunClicked_ExperimentSetupFragment(View v) {
		// TODO lazy work for now, more work
		Intent intent = new Intent(this, CreateExperimentActivity.class);
        startActivity(intent);			
	}

	@Override
	public void onBtnBackClicked_ExperimentSetupFragment() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, SensorDataCollectorActivity.class);
        startActivity(intent);	
	}
}
