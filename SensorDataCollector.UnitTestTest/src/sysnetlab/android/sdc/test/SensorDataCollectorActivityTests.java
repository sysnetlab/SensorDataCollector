package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.SensorDataCollectorActivity;
import sysnetlab.android.sdc.ui.SensorListAdaptor;
import sysnetlab.android.sdc.ui.SensorListFragment;
import android.content.Intent;

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
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSensorDataCollectionActivityLoaded()
	{
		SensorListFragment sensorListFragment = sdcActivity.getSensorListFragment();
		assertNotNull(sensorListFragment);
		getInstrumentation().callActivityOnStart(sdcActivity);
	}
	
}
