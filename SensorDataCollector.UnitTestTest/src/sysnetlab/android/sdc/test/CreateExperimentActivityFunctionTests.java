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
    private EditText mEditText;
    
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
        mListOperations = (ListView) mCreateExperimentActivity
                .findViewById(R.id.lv_operations);
        assertNotNull("Menu with operations has not been loaded", mListOperations);
        assertTrue("listOperations.getCount() is not 3", mListOperations.getCount() == 3);

        clickOperation(2);
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
        
        getInstrumentation().waitForIdleSync();
        
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
        mListOperations = (ListView) mCreateExperimentActivity
                .findViewById(R.id.lv_operations);
        assertNotNull("Menu with operations has not been loaded", mListOperations);
        assertTrue("listOperations.sgetCount() is not 3", mListOperations.getCount() == 3);

        clickOperation(0);
        getInstrumentation().waitForIdleSync();

        ExperimentEditTagsFragment tagFragment = mCreateExperimentActivity
                .getExperimentEditTagsFragment();
        assertNotNull("Tag selection fragment failed to load", tagFragment);
        final EditText editTextTag = (EditText) tagFragment.getView().findViewById(R.id.edittext_tag);
        final EditText editTextDesc = (EditText) tagFragment.getView().findViewById(R.id.edittext_description);
        assertNotNull("Failed to get EditText tag from View", editTextTag);
        assertNotNull("Failed to get EditText description from View", editTextDesc);
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
        int listSize = tagList.size();
        assertTrue("Tag is less than 1", listSize > 0);
        
        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
            	editTextDesc.setText("Test Description"); 
            }

        });
        getInstrumentation().waitForIdleSync();
        
        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
            	 addButton.performClick();
            }

        });
        
        getInstrumentation().waitForIdleSync();
        tagList = tagExperiment.getTags();
        assertTrue("The tag was created without a name",listSize==tagList.size());
        assertTrue("The tag description was deleted", !editTextDesc.getText().equals(""));
        
        this.sendKeys(KeyEvent.KEYCODE_BACK);          
        getInstrumentation().waitForIdleSync();
 
        LinearLayout layout = (LinearLayout) mListOperations.getAdapter().getView(0,  null, null).findViewById(R.id.layout_subtext);
        assertNotNull("layout should not be null", layout);
        assertTrue("tag list contains at least one member", tagList.size() > 0);
        assertTrue("layout has at least one child", layout.getChildCount() > 0);
        assertTrue("The number of tags displayed should be equal to the size of the tag list", layout.getChildCount() == tagList.size());
    }
    
    public void testExperimentTagSetupDuplicateTagFunction() throws Exception {

        // click on the Tag operation
        mListOperations = (ListView) mCreateExperimentActivity.findViewById(R.id.lv_operations);
        assertNotNull("Menu with operations has not been loaded", mListOperations);
        assertTrue("listOperations.sgetCount() is not 3", mListOperations.getCount() == 3);

        clickOperation(0);
        getInstrumentation().waitForIdleSync();

        // check and see if the ExperimentEditTagFragment is loaded. 
        ExperimentEditTagsFragment tagFragment = mCreateExperimentActivity.getExperimentEditTagsFragment();
        assertNotNull("Tag selection fragment failed to load", tagFragment);
        final EditText editTextTag = (EditText) tagFragment.getView().findViewById(R.id.edittext_tag);
        final EditText editTextDesc = (EditText) tagFragment.getView().findViewById(R.id.edittext_description);
        final Button addButton = (Button) tagFragment.getView().findViewById(R.id.btn_add_tag);
        assertNotNull("Failed to get EditText tag from View", editTextTag);
        assertNotNull("Failed to get EditText description from View", editTextDesc);
        assertNotNull("Button selection failed to load", addButton);
        
        // enter two tags with the same name, but different description
        int numberOfDuplicatedTags = 2;
        for (int i = 0; i < numberOfDuplicatedTags; i++) {
            final String strTagDesc = "This is tag " + i;
            getInstrumentation().runOnMainSync(new Runnable() {

                @Override
                public void run() {
                    editTextTag.setText("Test tag");
                    editTextDesc.setText(strTagDesc);
                }

            });

            getInstrumentation().waitForIdleSync();

            getInstrumentation().runOnMainSync(new Runnable() {

                @Override
                public void run() {
                    addButton.performClick();
                }

            });

            getInstrumentation().waitForIdleSync();
        }
        
        // tag inserted should be just 1 as a result of duplicate names
        Experiment tagExperiment = mCreateExperimentActivity.getExperiment();
        List<Tag> tagList = tagExperiment.getTags();
        assertNotNull("TagList failed to load", tagList);
        assertEquals("Tag should be 1", 1, tagList.size());
        
        // enter two tags with the different names
        numberOfDuplicatedTags = 2;
        for (int i = 0; i < numberOfDuplicatedTags; i++) {
            final String strTag = "Test Tag " + i;
            final String strTagDesc = "This is tag " + i;
            getInstrumentation().runOnMainSync(new Runnable() {

                @Override
                public void run() {
                    editTextTag.setText(strTag);
                    editTextDesc.setText(strTagDesc);
                }

            });

            getInstrumentation().waitForIdleSync();

            getInstrumentation().runOnMainSync(new Runnable() {

                @Override
                public void run() {
                    addButton.performClick();
                }

            });

            getInstrumentation().waitForIdleSync();
        }   
        
        tagList = tagExperiment.getTags();
        assertNotNull("TagList failed to load", tagList);
        assertEquals("Tag should be 1", 3, tagList.size());
    }
    
    
       
    public void testExperimentNoteSetupFunction() throws Exception {

        mListOperations = (ListView) mCreateExperimentActivity
                .findViewById(R.id.lv_operations);
        assertNotNull("Menu with operations has not been loaded", mListOperations);
        assertTrue("listOperations.sgetCount() is not 3", mListOperations.getCount() == 3);

        clickOperation(1);
        getInstrumentation().waitForIdleSync();

        ExperimentEditNotesFragment notesFragment = mCreateExperimentActivity
                .getExperimentEditNotesFragment();
        assertNotNull("Note editing fragment failed to load", notesFragment);
        mEditText = (EditText) mCreateExperimentActivity.findViewById(R.id.edittext_experiment_note_editing_note);
        assertNotNull("Failed to get EditText from View", mEditText);
        
        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
            	mEditText.setText("Test note"); 
            }

        });
        getInstrumentation().waitForIdleSync();
        
        Button addButton = (Button) mCreateExperimentActivity.findViewById(R.id.button_experiment_note_editing_add_note);
        assertNotNull("Button selection failed to load", addButton);
        
        TouchUtils.clickView(this, addButton);
        getInstrumentation().waitForIdleSync();
        
        Experiment noteExperiment = mCreateExperimentActivity.getExperiment();
        List<Note> noteList = noteExperiment.getNotes();
        assertNotNull("TagList failed to load", noteList);
        assertTrue("Tag is less than 1", noteList.size() > 0);
        
        clickOperation(1);
        getInstrumentation().waitForIdleSync();
        
        final EditText editTextNote = (EditText) mCreateExperimentActivity.findViewById(R.id.edittext_experiment_note_editing_note);
        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
                editTextNote.setText("Test note 2"); 
            }

        });
        getInstrumentation().waitForIdleSync();
        
        addButton = (Button) mCreateExperimentActivity.findViewById(R.id.button_experiment_note_editing_add_note);
        TouchUtils.clickView(this, addButton);
        getInstrumentation().waitForIdleSync();
        
        noteList = noteExperiment.getNotes();
        
        this.sendKeys(KeyEvent.KEYCODE_BACK);
          
        getInstrumentation().waitForIdleSync();
 
        LinearLayout notesLayout = (LinearLayout) mListOperations.getAdapter().getView(1,  null, null).findViewById(R.id.layout_subtext);
        LinearLayout tagsLayout = (LinearLayout) mListOperations.getAdapter().getView(0,  null, null).findViewById(R.id.layout_subtext);
        assertNotNull("notesLayout should not be null", notesLayout);
        assertNotNull("tagsLayout should not be null", tagsLayout);
        assertTrue("Notes list must contains at least one member", noteList.size() > 0);
        assertTrue("Layout must have at least one child", notesLayout.getChildCount() > 0);
        TextView tvNote = (TextView) notesLayout.getChildAt(0);
        assertTrue("The notes layout should display the last created note", tvNote.getText().toString().contains("Test note 2"));
        
        //Tests if the layout is being recycled by the ListView adapter
        assertTrue("Notes and Tags layout should not be equal", notesLayout.getChildCount()!=tagsLayout.getChildCount());
        
        
        
    }    

    public void testNewNoteEmptyTextField() throws Exception{    	
    	Button mButtonAddNote;
    	
    	mListOperations = (ListView) mCreateExperimentActivity
                .findViewById(R.id.lv_operations);
        assertNotNull("Menu with operations has not been loaded", mListOperations);
        assertTrue("listOperations.getCount() is not 3", mListOperations.getCount() == 3);

        clickOperation(1);

        getInstrumentation().waitForIdleSync();
        
        mEditText = (EditText) mCreateExperimentActivity.
        		findViewById(R.id.edittext_experiment_note_editing_note);
        
        assertNotNull("The edit note text field has not been loaded.", mEditText);
        
        getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				mEditText.setText("testing");
			}
		});
        
        getInstrumentation().waitForIdleSync();
        
        mButtonAddNote=(Button) mCreateExperimentActivity.
        		findViewById(R.id.button_experiment_note_editing_add_note);
        
        assertNotNull("The button add new tag failed to load",mButtonAddNote);
        
        TouchUtils.clickView(this, mButtonAddNote);
        getInstrumentation().waitForIdleSync();
        
        clickOperation(1);
        getInstrumentation().waitForIdleSync();               
        
        assertEquals("The edit text is not empty when a new note is being created",
        		"", 
        		mEditText.getText().toString());
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
