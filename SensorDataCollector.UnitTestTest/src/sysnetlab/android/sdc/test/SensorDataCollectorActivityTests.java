package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.ui.SensorDataCollectorActivity;
import sysnetlab.android.sdc.ui.SensorListFragment;
import sysnetlab.android.sdc.ui.SensorSetupFragment;
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
		SensorListFragment sensorListFragment = sdcActivity.getSensorListFragment();
		assertNotNull(sensorListFragment);
		SensorSetupFragment sensorSetupFragment = sdcActivity.getSensorSetupFragment();
		assertNull(sensorSetupFragment);
	}
	
	public void testSensorDataCollectionState()
	{
		assert(sdcActivity.getCurrentCollectionState() == DataCollectionState.DATA_COLLECTION_STOPPED);
		sdcActivity.onBtnRunClicked_SensorListFragment(new Button(sdcActivity));
		assert(sdcActivity.getCurrentCollectionState() == DataCollectionState.DATA_COLLECTION_IN_PROGRESS);
		sdcActivity.onBtnRunClicked_SensorListFragment(new Button(sdcActivity));
		assert(sdcActivity.getCurrentCollectionState() == DataCollectionState.DATA_COLLECTION_STOPPED);
	}
}
