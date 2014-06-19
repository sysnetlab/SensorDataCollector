package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.ui.SensorDataCollectorActivity;
import sysnetlab.android.sdc.ui.fragments.ExperimentListFragment;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;


public class SensorDataCollectorActivityTests 
		extends android.test.ActivityUnitTestCase<SensorDataCollectorActivity> {

	private SensorDataCollectorActivity sdcActivity;
	private ExperimentListFragment mExperimentListFragment;
	private ArrayAdapter<Experiment> mExperimentList;
	
	public SensorDataCollectorActivityTests() {
		super(SensorDataCollectorActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
        Context context = getInstrumentation().getTargetContext();
        context.setTheme(R.style.Theme_AppCompat);
        Intent intent = new Intent(context, SensorDataCollectorActivity.class);		

        startActivity(intent, null, null);
        sdcActivity = getActivity();
        getInstrumentation().callActivityOnStart(sdcActivity);
        mExperimentListFragment = sdcActivity.getExperimentListFragment();
        mExperimentList = mExperimentListFragment.getExperimentArray();
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
	    Button view = (Button) sdcActivity.findViewById(R.id.button_create_experiment);
	    assertNotNull("Button not allowed to be null", view);
	    view.performClick();
	    Intent triggeredIntent = getStartedActivityIntent();
	    assertNotNull("Intent was null", triggeredIntent);
	}
	
	public void testExperimentListFragmentCreated() {
		assertNotNull("ExperimentListFragment is null",mExperimentListFragment);
	}
	
	public void testExperimentListClicked() {
	    try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		assertNotNull("Experiment list is null", mExperimentList);
		assertNotSame("Experiment list is empty, create an experiment to run next tests", 0, mExperimentList.getCount());
			
		if(!mExperimentList.isEmpty()){								
			
			// TODO Change the way the experiment is loaded performing a item click
			
			//mlistView.performItemClick(mlistView.getAdapter().getView(0, null, null),
			//				0, mlistView.getAdapter().getItemId(0));
			
		    sdcActivity.onExperimentClicked_ExperimentListFragment(mExperimentList.getItem(0));
			Intent triggeredIntent = getStartedActivityIntent();
			assertNotNull("Intent was null", triggeredIntent);
			Experiment mExperiment=ExperimentManagerSingleton.getInstance().getActiveExperiment();
			assertNotNull("The loaded experiment is null", mExperiment);			
			assertEquals("The loaded experiment is not correct",mExperiment,mExperimentList.getItem(0));	
		}		
	}		
}
