package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.ExperimentRunFragment;
import sysnetlab.android.sdc.ui.ExperimentSensorSelectionFragment;
import sysnetlab.android.sdc.ui.ExperimentSetupFragment;
import sysnetlab.android.sdc.ui.ExperimentSensorSetupFragment;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;

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
	    //TODO retiring SensorListFragment, need to rewrite the test case
//		assert(createExperimentActivity.getCurrentCollectionState() == DataCollectionState.DATA_COLLECTION_STOPPED);
//		createExperimentActivity.onBtnRunClicked_SensorListFragment(new Button(createExperimentActivity));
//		assert(createExperimentActivity.getCurrentCollectionState() == DataCollectionState.DATA_COLLECTION_IN_PROGRESS);
//		createExperimentActivity.onBtnRunClicked_SensorListFragment(new Button(createExperimentActivity));
//		assert(createExperimentActivity.getCurrentCollectionState() == DataCollectionState.DATA_COLLECTION_STOPPED);
	}
	
	public void testExperimentSetupFragment() {
		ExperimentSetupFragment experimentSetupFragment = createExperimentActivity.getExperimentSetupFragment();
		assertNotNull(experimentSetupFragment);
	}
	
	public void testExperimentRunFragment() {
		Button view = (Button) createExperimentActivity.findViewById(R.id.button_experiment_run);
	    assertNotNull("Button not allowed to be null", view);
	    view.performClick();
		ExperimentRunFragment experimentRunFragment=createExperimentActivity.getExperimentRunFragment();
		assertNotNull(experimentRunFragment);
	}
	
	public void testSensorDataCollectionActivityLoaded(){
		// TODO Get the sensor list view from the sensorSelectionFragment and perform a item click
		
		//ImageView view = (ImageView) createExperimentActivity.findViewById(R.id.imv_sensors_plusminus);
	    //assertNotNull("Button not allowed to be null", view);
	    //view.performClick();
		//ExperimentSensorSelectionFragment sensorSelectionFragment=createExperimentActivity.getExperimentSensorSensorSelectionFragment();				
		//ListView listView = (ListView) sensorSelectionFragment.getSensorListView();
		//listView.performItemClick(listView.getAdapter().getView(0, null, null),
		//				0, listView.getAdapter().getItemId(0));
		
		ExperimentSensorSetupFragment sensorSetupFragment = createExperimentActivity.getSensorSetupFragment();
		assertNotNull(sensorSetupFragment);
	}
	
	public void testSensorSelectionFragment() {
		ImageView view = (ImageView) createExperimentActivity.findViewById(R.id.imv_sensors_plusminus);
	    assertNotNull("Button not allowed to be null", view);
	    view.performClick();
	    ExperimentSensorSelectionFragment sensorSelectionFragment=createExperimentActivity.getExperimentSensorSensorSelectionFragment();
		assertNotNull(sensorSelectionFragment);
	}
}
