package sysnetlab.android.sdc.test;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.adaptors.SensorListAdapter;
import sysnetlab.android.sdc.ui.fragments.ExperimentEditNotesFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentEditTagsFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorListFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorSelectionFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorSetupFragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CreateExperimentActivityFunctionTests extends
        ActivityInstrumentationTestCase2<CreateExperimentActivity> {
    
    private CreateExperimentActivity mCreateExperimentActivity;
    private ListView mListOperations;
    
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
        
        
        this.sendKeys(KeyEvent.KEYCODE_BACK);

        final CheckBox checkBox = (CheckBox) listSensors.getAdapter().getView(0, null, null)
                .findViewById(R.id.check);

        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {

                checkBox.performClick();
            }

        });

        int numSensorSelected = 0;
        for (AbstractSensor sensor : SensorDiscoverer.discoverSensorList(getActivity())) {
            if (sensor.isSelected()) {
                numSensorSelected++;
            }
        }

        assertTrue("1 sensor selected", numSensorSelected == 1);

        final CheckBox checkBox2 = (CheckBox) listSensors.getAdapter()
                .getView(listSensors.getCount() - 1, null, null).findViewById(R.id.check);

        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {

                checkBox2.performClick();
            }

        });

        numSensorSelected = 0;
        for (AbstractSensor sensor : SensorDiscoverer.discoverSensorList(getActivity())) {
            if (sensor.isSelected()) {
                numSensorSelected++;
            }
        }

        assertTrue("2 sensors selected", numSensorSelected == 2);

        this.sendKeys(KeyEvent.KEYCODE_BACK);

        getInstrumentation().waitForIdleSync();

        ExperimentSensorListFragment mExperimentSensorListFragment = mCreateExperimentActivity
                .getExperimentSetupFragment().getExperimentSensorListFragment();

        assertNotNull("Sensor list fragment should not be null", mExperimentSensorListFragment);

        // Thread.sleep(10000);

        assertTrue("2 sensors are selected, so the list should be 2 rows",
                mExperimentSensorListFragment.getListView().getCount() == numSensorSelected);

        final ListView sensorsList = mExperimentSensorListFragment.getListView();
        SensorListAdapter sensorListAdapter = (SensorListAdapter) sensorsList.getAdapter();
        String sensorClicked = sensorListAdapter.getSensors().get(1).getName();

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                sensorsList.performItemClick(
                        sensorsList.getAdapter().getView(1, null, null),
                        1,
                        sensorsList.getAdapter().getItemId(1));
            }
        });
        getInstrumentation().waitForIdleSync();

        String sensorDisplayed = ((TextView) getActivity().findViewById(
                R.id.textview_sensor_setup_sensor_name)).getText().toString();

        assertEquals("The sensor selected in the list differs from the sensor being displayed",
                sensorClicked,
                sensorDisplayed);
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

        this.sendKeys(KeyEvent.KEYCODE_BACK);
          
        getInstrumentation().waitForIdleSync();
 
        LinearLayout layout = (LinearLayout) listOperations.getAdapter().getView(0,  null, null).findViewById(R.id.layout_subtext);
        assertNotNull("layout should not be null", layout);
        assertTrue("tag list contains at least one member", tagList.size() > 0);
        assertTrue("layout has at least one child", layout.getChildCount() > 0);
        assertTrue("The number of tags displayed should be equal to the size of the tag list", layout.getChildCount() == tagList.size());
    }
    
    
       
    public void testExperimentNoteSetupFunction() throws Exception {

        final ListView listOperations = (ListView) mCreateExperimentActivity
                .findViewById(R.id.lv_operations);
        assertNotNull("Menu with operations has not been loaded", listOperations);
        assertTrue("listOperations.sgetCount() is not 3", listOperations.getCount() == 3);

        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
                listOperations.performItemClick(listOperations.getAdapter().getView(1, null, null),
                        1, listOperations.getAdapter()
                                .getItemId(1));
            }

        });

        getInstrumentation().waitForIdleSync();

        ExperimentEditNotesFragment notesFragment = mCreateExperimentActivity
                .getExperimentEditNotesFragment();
        assertNotNull("Note editing fragment failed to load", notesFragment);
        final EditText editTextNote = (EditText) notesFragment.getView().findViewById(R.id.edittext_experiment_note_editing_note);
        assertNotNull("Failed to get EditText from View", editTextNote); 
        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
                editTextNote.setText("Test note"); 
            }

        });

        getInstrumentation().waitForIdleSync();
        final Button addButton = (Button) notesFragment.getView().findViewById(R.id.button_experiment_note_editing_add_note);
        assertNotNull("Button selection failed to load", addButton);
        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
                 addButton.performClick();
            }

        });

        getInstrumentation().waitForIdleSync();
        Experiment noteExperiment = mCreateExperimentActivity.getExperiment();
        List<Note> noteList = noteExperiment.getNotes();
        assertNotNull("TagList failed to load", noteList);
        assertTrue("Tag is less than 1", noteList.size() > 0);

        this.sendKeys(KeyEvent.KEYCODE_BACK);
          
        getInstrumentation().waitForIdleSync();
 
        LinearLayout layout = (LinearLayout) listOperations.getAdapter().getView(1,  null, null).findViewById(R.id.layout_subtext);
        assertNotNull("layout should not be null", layout);
        assertTrue("tag list contains at least one member", noteList.size() > 0);
        assertTrue("layout has at least one child", layout.getChildCount() > 0);
        assertTrue("The number of tags displayed should be equal to the size of the tag list", layout.getChildCount() == noteList.size());
    }    

    public void testNewNoteEmptyTextField(){
    	final EditText mNoteEditText;
    	Button mButtonAddTag;
    	
    	mListOperations = (ListView) mCreateExperimentActivity
                .findViewById(R.id.lv_operations);
        assertNotNull("Menu with operations has not been loaded", mListOperations);
        assertTrue("listOperations.getCount() is not 3", mListOperations.getCount() == 3);

        clickOperation(1);

        getInstrumentation().waitForIdleSync();
        
        mNoteEditText=(EditText) mCreateExperimentActivity.
        		findViewById(R.id.edittext_experiment_note_editing_note);
        
        assertNotNull("The edit note text field has not been loaded.",mNoteEditText);
        
        getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				mNoteEditText.getText().append("testing");
			}
		});
        
        getInstrumentation().waitForIdleSync();
        
        mButtonAddTag=(Button) mCreateExperimentActivity.
        		findViewById(R.id.button_experiment_note_editing_add_note);
        
        assertNotNull("The button add new tag failed to load",mButtonAddTag);
        
        TouchUtils.clickView(this, mButtonAddTag);
        
        getInstrumentation().waitForIdleSync();
        
        clickOperation(1);
        
        getInstrumentation().waitForIdleSync();
        
        EditText mNoteEditText2=(EditText) mCreateExperimentActivity.
        		findViewById(R.id.edittext_experiment_note_editing_note);
        
        assertEquals("The edit text is not empty when a new note is being created",
        		"", 
        		mNoteEditText2.getText().toString());
    }

	private void clickOperation(final int op) {
		getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mListOperations.performItemClick(mListOperations.getAdapter().getView(op, null, null),
                        op, mListOperations.getAdapter()
                                .getItemId(op));
            }
        });
	}
}
