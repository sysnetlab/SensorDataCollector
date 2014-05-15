package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.ExperimentRunFragment;
import sysnetlab.android.sdc.ui.ExperimentSetupFragment;
import sysnetlab.android.sdc.ui.SensorListFragment;
import sysnetlab.android.sdc.ui.SensorSetupFragment;
import android.content.Intent;
import android.widget.Button;

public class CreateExperimentActivityTests 
		extends android.test.ActivityUnitTestCase<CreateExperimentActivity> {

	private CreateExperimentActivity createExperimentActivity;
	
	public CreateExperimentActivityTests() {
		super(CreateExperimentActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	    Intent intent = new Intent(getInstrumentation().getTargetContext(), CreateExperimentActivity.class);
        startActivity(intent, null, null);
        createExperimentActivity = getActivity();
        getInstrumentation().callActivityOnStart(createExperimentActivity);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testSensorDataCollectionState(){
		assert(createExperimentActivity.getCurrentCollectionState() == DataCollectionState.DATA_COLLECTION_STOPPED);
		createExperimentActivity.onBtnRunClicked_SensorListFragment(new Button(createExperimentActivity));
		assert(createExperimentActivity.getCurrentCollectionState() == DataCollectionState.DATA_COLLECTION_IN_PROGRESS);
		createExperimentActivity.onBtnRunClicked_SensorListFragment(new Button(createExperimentActivity));
		assert(createExperimentActivity.getCurrentCollectionState() == DataCollectionState.DATA_COLLECTION_STOPPED);
	}
	
	public void testExperimentSetupFragment() {
		ExperimentSetupFragment experimentSetupFragment = createExperimentActivity.getExperimentSetupFragment();
		assertNotNull(experimentSetupFragment);
	}
	
	public void testExperimentRunFragment() {
		ExperimentRunFragment experimentRunFragment=createExperimentActivity.getExperimentRunFragment();
		assertNotNull(experimentRunFragment);
	}
	
	public void testSensorDataCollectionActivityLoaded(){
		SensorSetupFragment sensorSetupFragment = createExperimentActivity.getSensorSetupFragment();
		assertNotNull(sensorSetupFragment);
	}
	
	public void testSensorListFragment() {
		SensorListFragment sensorListFragment=createExperimentActivity.getSensorListFragment();
		assertNotNull(sensorListFragment);
	}
}
