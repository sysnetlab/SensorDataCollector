package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import android.content.Intent;



public class ViewExperimentActivityTests extends android.test.ActivityUnitTestCase<ViewExperimentActivity> {

	private ViewExperimentActivity veActivity;
	
	public ViewExperimentActivityTests() {
		super(ViewExperimentActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();		
	    Intent intent = new Intent(getInstrumentation().getTargetContext(), ViewExperimentActivity.class);
	    Experiment mExperiment= new Experiment();	
	    intent.putExtra("experiment", mExperiment);
        startActivity(intent, null, null);
        veActivity = getActivity();
        getInstrumentation().callActivityOnStart(veActivity);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testViewExperimentActivityLoaded()
	{
		assertNotNull(veActivity.findViewById(R.id.fragment_container));		
	}

}
