package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.SensorDataCollectorActivity;
import android.content.Intent;
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
}
