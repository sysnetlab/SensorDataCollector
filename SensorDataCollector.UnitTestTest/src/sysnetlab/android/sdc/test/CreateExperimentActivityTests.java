
package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import android.content.Context;
import android.content.Intent;

public class CreateExperimentActivityTests
        extends android.test.ActivityUnitTestCase<CreateExperimentActivity> {

    private CreateExperimentActivity createExperimentActivity;

    public CreateExperimentActivityTests() {
        super(CreateExperimentActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        
        Context context = getInstrumentation().getTargetContext();
        context.setTheme(R.style.Theme_AppCompat);
        Intent intent = new Intent(context, CreateExperimentActivity.class);
        startActivity(intent, null, null);                
        createExperimentActivity = getActivity();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testLayout() {
        assertNotNull("The activity cannot be null.", createExperimentActivity);
        assertNotNull("Fragment container must exist.", createExperimentActivity.findViewById(R.id.fragment_container));
        assertNotNull("ProgressBar Layout must exist.", createExperimentActivity.findViewById(R.id.progressbar_task_in_progress));
    }

    public void testSensorDataCollectionState() {
        assertNotNull("The activity cannot be null.", createExperimentActivity);
        assert(createExperimentActivity.getCurrentCollectionState() == DataCollectionState.DATA_COLLECTION_STOPPED);
        // createExperimentActivity.onBtnRunClicked_SensorListFragment(new  Button(createExperimentActivity));
        // assert(createExperimentActivity.getCurrentCollectionState() ==
        // DataCollectionState.DATA_COLLECTION_IN_PROGRESS);
        // createExperimentActivity.onBtnRunClicked_SensorListFragment(new
        // Button(createExperimentActivity));
        // assert(createExperimentActivity.getCurrentCollectionState() ==
        // DataCollectionState.DATA_COLLECTION_STOPPED);
    }
/* move to functional test */
//    public void testExperimentSetupFragment() {
//        ExperimentSetupFragment experimentSetupFragment = createExperimentActivity
//                .getExperimentSetupFragment();
//        assertNotNull("The setup fragment has not been loaded", experimentSetupFragment);
//    }
//
//    public void testExperimentSensorSetupFragment() {
//        ListView lView = null;
//        // ExperimentSensorSelectionFragment sensorSelectionFragment = null;
//
//        lView = (ListView) createExperimentActivity.findViewById(R.id.lv_operations);
//        assertNotNull("Menu with operations has not been loaded", lView);
//        assertTrue("lView.getCount() is not 3", lView.getCount() == 3);
//        
//        /* The tests below will never be performed correctly. The issue is that it takes time
//         * for a fragment to load. You can observe from the onCreateView method's log messages
//         * never appear in the log. 
//         * 
//         * The correct way to test clicks and actions is to use ActivityInstrumentationTestCase2.
//         * 
//         * The following tests has been moved to CreateExperimentActivityFunctionTests
//         */
//
///*      
//         lView.performItemClick(lView.getAdapter().getView(2, null, null), 2, lView.getAdapter()
//                .getItemId(2));
//        sensorSelectionFragment = createExperimentActivity.getExperimentSensorSelectionFragment();
//        assertNotNull("Sensors selection fragment failed to load", sensorSelectionFragment);
//
//        lView = (ListView) sensorSelectionFragment.getSensorListView();
//        assertNotNull("The list of sensors was not loaded", lView);
//
//        if (lView != null && !(lView.getCount() == 0)) {
//            lView.performItemClick(lView.getAdapter().getView(0, null, null), 0, lView.getAdapter()
//                    .getItemId(0));
//            ExperimentSensorSetupFragment experimentSensorSetupFragment = createExperimentActivity
//                    .getExperimentSensorSetupFragment();
//            assertNotNull("Sensor setup fragment has not been loaded",
//                    experimentSensorSetupFragment);
//        }
//*/
//    }
//
//    public void testExperimentRunFragment() {
//        Button view = (Button) createExperimentActivity.findViewById(R.id.button_experiment_run);
//        assertNotNull("Button not allowed to be null", view);
//        view.performClick();
//        ExperimentRunFragment experimentRunFragment = createExperimentActivity
//                .getExperimentRunFragment();
//        assertNotNull(experimentRunFragment);
//    }
//
//    public void testSensorSelectionFragment() {
//        ListView lView = (ListView) createExperimentActivity.findViewById(R.id.lv_operations);
//        assertNotNull("Menu with operations has not been loaded", lView);
//        lView.performItemClick(lView.getAdapter().getView(2, null, null), 2, lView.getAdapter()
//                .getItemId(2));
//        ExperimentSensorSelectionFragment sensorSelectionFragment = createExperimentActivity
//                .getExperimentSensorSelectionFragment();
//        assertNotNull("Sensors selection fragment failed to load", sensorSelectionFragment);
//    }
//
//    public void testExperimentNotesFragment() {
//        ListView lView = (ListView) createExperimentActivity.findViewById(R.id.lv_operations);
//        assertNotNull("Menu with operations has not been loaded", lView);
//        lView.performItemClick(lView.getAdapter().getView(1, null, null), 1, lView.getAdapter()
//                .getItemId(1));
//        ExperimentEditNotesFragment experimentNotesFragment = createExperimentActivity
//                .getExperimentEditNotesFragment();
//        assertNotNull("Notes edition fragment failed to load", experimentNotesFragment);
//    }
//
//    public void testExperimentEditTagsFragment() {
//        ListView lView = (ListView) createExperimentActivity.findViewById(R.id.lv_operations);
//        assertNotNull("Menu with operations has not been loaded", lView);
//        lView.performItemClick(lView.getAdapter().getView(0, null, null), 0, lView.getAdapter()
//                .getItemId(0));
//        ExperimentEditTagsFragment experimentEditTagsFragment = createExperimentActivity
//                .getExperimentEditTagsFragment();
//        assertNotNull("Tags edition fragment failed to load", experimentEditTagsFragment);
//    }
}
