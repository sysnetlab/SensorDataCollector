package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.fragments.ExperimentEditNotesFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentEditTagsFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentRunFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorSelectionFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorSetupFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSetupFragment;
import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;

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
	ExperimentSetupFragment experimentSetupFragment=createExperimentActivity.getExperimentSetupFragment();
	assertNotNull("The setup fragment has not been loaded",experimentSetupFragment);
}

public void testExperimentSensorSetupFragment() {
		ListView lView = null;
		ExperimentSensorSelectionFragment sensorSelectionFragment = null;
		
		lView = (ListView) createExperimentActivity.findViewById(R.id.lv_operations);
	    assertNotNull("Menu with operations has not been loaded", lView);
	    
	    lView.performItemClick(lView.getAdapter().getView(2, null, null), 2, lView.getAdapter().getItemId(2));
	    sensorSelectionFragment=createExperimentActivity.getExperimentSensorSelectionFragment();
	    assertNotNull("Sensors selection fragment failed to load",sensorSelectionFragment);
		
		lView= (ListView) sensorSelectionFragment.getSensorListView();
		assertNotNull("The list of sensors was not loaded",lView);
		
		if(lView!=null && !(lView.getCount()==0)){
			lView.performItemClick(lView.getAdapter().getView(0, null, null), 0, lView.getAdapter().getItemId(0));
			ExperimentSensorSetupFragment experimentSensorSetupFragment=createExperimentActivity.getExperimentSensorSetupFragment();
			assertNotNull("Sensor setup fragment has not been loaded",experimentSensorSetupFragment);
		}
	}
	
	public void testExperimentRunFragment() {
		Button view = (Button) createExperimentActivity.findViewById(R.id.button_experiment_run);
	    assertNotNull("Button not allowed to be null", view);
	    view.performClick();
		ExperimentRunFragment experimentRunFragment=createExperimentActivity.getExperimentRunFragment();
		assertNotNull(experimentRunFragment);
	}
	
	public void testSensorSelectionFragment() {
		ListView lView = (ListView) createExperimentActivity.findViewById(R.id.lv_operations);
	    assertNotNull("Menu with operations has not been loaded", lView);
	    lView.performItemClick(lView.getAdapter().getView(2, null, null), 2, lView.getAdapter().getItemId(2));
	    ExperimentSensorSelectionFragment sensorSelectionFragment=createExperimentActivity.getExperimentSensorSelectionFragment();
		assertNotNull("Sensors selection fragment failed to load",sensorSelectionFragment);
	}
	
	public void testExperimentNotesFragment() {
		ListView lView = (ListView) createExperimentActivity.findViewById(R.id.lv_operations);
	    assertNotNull("Menu with operations has not been loaded", lView);
	    lView.performItemClick(lView.getAdapter().getView(1, null, null), 1, lView.getAdapter().getItemId(1));
	    ExperimentEditNotesFragment experimentNotesFragment=createExperimentActivity.getCreateExperimentNotesFragment();
		assertNotNull("Notes edition fragment failed to load",experimentNotesFragment);
	}
	
	public void testExperimentEditTagsFragmentt() {
		ListView lView = (ListView) createExperimentActivity.findViewById(R.id.lv_operations);
	    assertNotNull("Menu with operations has not been loaded", lView);
	    lView.performItemClick(lView.getAdapter().getView(0, null, null), 0, lView.getAdapter().getItemId(0));
	    ExperimentEditTagsFragment experimentEditTagsFragment=createExperimentActivity.getExperimentEditTagsFragment();
		assertNotNull("Tags edition fragment failed to load",experimentEditTagsFragment);
	}
}
