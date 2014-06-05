package sysnetlab.android.sdc.test;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.fragments.ExperimentEditTagsFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorSelectionFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorSetupFragment;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class CreateExperimentActivityFunctionTests extends
        ActivityInstrumentationTestCase2<CreateExperimentActivity> {
    
    private CreateExperimentActivity mCreateExperimentActivity;
    
    public CreateExperimentActivityFunctionTests() {
        super(CreateExperimentActivity.class);
      }    

    public CreateExperimentActivityFunctionTests(Class<CreateExperimentActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        mCreateExperimentActivity = (CreateExperimentActivity) getActivity();
    }
    
    public void testExperimentSensorSetupFunction() throws Exception {

        // Set up an ActivityMonitor if necessary 
        /*
         * ActivityMonitor createExperimentActivityMonitor =
         * getInstrumentation()
         * .addMonitor(CreateExperimentActivity.class.getName(), null, false);
         */
        final ListView listOperations = (ListView) mCreateExperimentActivity
                .findViewById(R.id.lv_operations);
        assertNotNull("Menu with operations has not been loaded", listOperations);
        assertTrue("listOperations.getCount() is not 3", listOperations.getCount() == 3);

        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {

                listOperations.performItemClick(listOperations.getAdapter().getView(2, null, null),
                        2, listOperations.getAdapter()
                                .getItemId(2));
            }

        });

        getInstrumentation().waitForIdleSync();

        ExperimentSensorSelectionFragment sensorSelectionFragment = mCreateExperimentActivity
                .getExperimentSensorSelectionFragment();
        assertNotNull("Sensors selection fragment failed to load", sensorSelectionFragment);

        final ListView listSensors = (ListView) sensorSelectionFragment.getSensorListView();
        assertNotNull("The list of sensors was not loaded", listSensors);
        assertTrue("The list of sensors cannot be 0", listSensors.getCount() > 0);

        getInstrumentation().waitForIdleSync();

        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {

                listSensors.performItemClick(listSensors.getAdapter().getView(0, null, null), 0,
                        listSensors.getAdapter()
                                .getItemId(0));
            }

        });

        getInstrumentation().waitForIdleSync();

        ExperimentSensorSetupFragment experimentSensorSetupFragment = mCreateExperimentActivity
                .getExperimentSensorSetupFragment();
        assertNotNull("Sensor setup fragment has not been loaded",
                experimentSensorSetupFragment);

        getInstrumentation().waitForIdleSync();
    }
    
    public void testExperimentTagSetupFunction() throws Exception {

        // Set up an ActivityMonitor if necessary 
        /*
         * ActivityMonitor createExperimentActivityMonitor =
         * getInstrumentation()
         * .addMonitor(CreateExperimentActivity.class.getName(), null, false);
         */
        final ListView listOperations = (ListView) mCreateExperimentActivity
                .findViewById(R.id.lv_operations);
        assertNotNull("Menu with operations has not been loaded", listOperations);
        assertTrue("listOperations.sgetCount() is not 3", listOperations.getCount() == 3);

        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
            	listOperations.performItemClick(listOperations.getAdapter().getView(0, null, null),
                        0, listOperations.getAdapter()
                                .getItemId(0));
            }

        });

        getInstrumentation().waitForIdleSync();

        ExperimentEditTagsFragment tagFragment = mCreateExperimentActivity
                .getExperimentEditTagsFragment();
        assertNotNull("Tag selection fragment failed to load", tagFragment);
        final EditText editTextTag = (EditText) tagFragment.getView().findViewById(R.id.edittext_tag);
        assertNotNull("Failed to get EditText from View", editTextTag); 
        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
            	editTextTag.setText("Test tag"); 
            }

        });

        getInstrumentation().waitForIdleSync();
        final Button addButton = (Button) tagFragment.getView().findViewById(R.id.btn_add_tag);
        assertNotNull("Button selection failed to load", addButton);
        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
            	 addButton.performClick();
            }

        });

        getInstrumentation().waitForIdleSync();
        Experiment tagExperiment = mCreateExperimentActivity.getExperiment();
        List<Tag> tagList = tagExperiment.getTags();
        assertNotNull("TagList failed to load", tagList);
        assertTrue("Tag is less than 1",tagList.size() > 0);
    }
}
