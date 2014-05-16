package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.ui.SensorDataCollectorActivity;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class SensorDataCollectorActivityTests 
		extends android.test.ActivityUnitTestCase<SensorDataCollectorActivity> {

	private SensorDataCollectorActivity sdcActivity;
	
	public SensorDataCollectorActivityTests() {
		super(SensorDataCollectorActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	    Intent intent = new Intent(getInstrumentation().getTargetContext(), SensorDataCollectorActivity.class);
        startActivity(intent, null, null);
        sdcActivity = getActivity();
        getInstrumentation().callActivityOnStart(sdcActivity);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSensorDataCollectionActivityLoaded()
	{
		assertNotNull(sdcActivity.findViewById(R.id.fragment_container));
		assertNotNull(sdcActivity.getExperimentListFragment());
	}
	
	public void testCreateExperimentButtonClicked()
	{
	    Button view = (Button) sdcActivity.findViewById(R.id.buttonCreateExperiment);
	    assertNotNull("Button not allowed to be null", view);
	    view.performClick();
	    Intent triggeredIntent = getStartedActivityIntent();
	    assertNotNull("Intent was null", triggeredIntent);
	}
	
	public void testExperimentListClicked() {
		// TODO test correct experiment is constructed and loaded
		ArrayAdapter<Experiment> mExperimentListFragment=sdcActivity.getExperimentListFragment().getExperimentArray();
		assertNotNull("Experiment list is null", mExperimentListFragment);
		assertNotSame("Experiment list is empty, create an experiment to run next tests", 0, mExperimentListFragment.getCount());
		if(!mExperimentListFragment.isEmpty()){
			sdcActivity.onExperimentClicked_ExperimentListFragment(mExperimentListFragment.getItem(0));
			Intent triggeredIntent = getStartedActivityIntent();
			assertNotNull("Intent was null", triggeredIntent);			
			Experiment mExperiment=triggeredIntent.getParcelableExtra("experiment");
			assertEquals("The loaded experiment is not correct",mExperiment,mExperimentListFragment.getItem(0));	
		}		
	}
	
	public void testExperimentListFragmentCreated() {
		// TODO test if mExperimentListFragment is instantiated
	}
}
