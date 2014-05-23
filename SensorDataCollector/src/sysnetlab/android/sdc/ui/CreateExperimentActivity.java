
package sysnetlab.android.sdc.ui;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datacollector.ExperimentTime;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.StateTag;
import sysnetlab.android.sdc.datacollector.TaggingAction;
import sysnetlab.android.sdc.datacollector.TaggingState;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.services.RunExperimentService;
import sysnetlab.android.sdc.ui.fragments.ExperimentEditNotesFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentEditTagsFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentRunFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentRunTaggingFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorSelectionFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorSetupFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSetupFragment;
import sysnetlab.android.sdc.ui.fragments.FragmentUtil;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

public class CreateExperimentActivity extends FragmentActivity
        implements
        ExperimentSensorSetupFragment.OnFragmentClickListener,
        ExperimentSetupFragment.OnFragmentClickListener,
        ExperimentSensorSelectionFragment.OnFragmentClickListener,
        ExperimentEditTagsFragment.OnFragmentClickListener,
        ExperimentRunFragment.OnFragmentClickListener,
        ExperimentRunFragment.ExperimentHandler,
        ExperimentRunTaggingFragment.OnFragmentClickListener,
        ExperimentEditNotesFragment.OnFragmentClickListener
{
    //private SensorManager mSensorManager;

    private ExperimentSetupFragment mExperimentSetupFragment;
    private ExperimentSensorSelectionFragment mExperimentSensorSelectionFragment;
    private ExperimentSensorSetupFragment mSensorSetupFragment;
    private ExperimentEditNotesFragment mExperimentEditNotesFragment;
    private ExperimentEditTagsFragment mExperimentEditTagsFragment;
    private ExperimentRunFragment mExperimentRunFragment;

    private DataCollectionState mCollectionState;

    private Experiment mExperiment;

    private int mPreviousTagPosition;
    private StateTag mStateTagPrevious;
    private Drawable mDrawableBackground;
    
    private RunExperimentService mRunExperimentService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO handle configuration change
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);


        mPreviousTagPosition = -1;
        
        ViewPager vp=new ViewPager(this);
		vp.setOffscreenPageLimit(5);

        int operation = getIntent().getIntExtra(SensorDataCollectorActivity.APP_OPERATION_KEY,
                SensorDataCollectorActivity.APP_OPERATION_CREATE_NEW_EXPERIMENT);
        if (operation == SensorDataCollectorActivity.APP_OPERATION_CLONE_EXPERIMENT) {
            mExperiment = ExperimentManagerSingleton.getInstance().getActiveExperiment().clone();
        } else {
            /**
             * create an experiment using SimpleFileStore. It can be set using
             * UI in the future when different types of Store are corrected.
             */
            mExperiment = new Experiment();
        }

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            mExperimentSetupFragment = new ExperimentSetupFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, mExperimentSetupFragment);
            transaction.commit();
        }
        //mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mCollectionState = DataCollectionState.DATA_COLLECTION_STOPPED;
        Log.i("SensorDataCollector", "Leaving CreateExperimentActivit::onCreate.");
    }           
    
    @Override
    public void onBtnConfirmClicked_SensorSetupFragment(View v, AbstractSensor sensor) {
        Log.i("SensorDataCollector", "SensorSetupFragment: Button Confirm clicked.");

        EditText et = (EditText) findViewById(R.id.edittext_sensor_steup_sampling_rate);

        switch (sensor.getMajorType()) {
            case AbstractSensor.ANDROID_SENSOR:
                AndroidSensor androidSensor = (AndroidSensor) sensor;
                if (androidSensor.isStreamingSensor()) {
                    androidSensor.setSamplingInterval((int) (1000000. / Double.parseDouble(et
                            .getText().toString())));
                }
                break;
            case AbstractSensor.AUDIO_SENSOR:
                Log.i("SensorDataCollector", "Audio Sensor is a todo.");
                // TODO: todo ...
                break;
            case AbstractSensor.CAMERA_SENSOR:
                // TODO: todo ...
                Log.i("SensorDataCollector", "Camera Sensor is a todo.");
                break;
            case AbstractSensor.WIFI_SENSOR:
                // TODO: todo ...
                Log.i("SensorDataCollector", "WiFi Sensor is a todo.");
                break;
            case AbstractSensor.BLUETOOTH_SENSOR:
                // TODO: todo ...
                Log.i("SensorDataCollector", "Bluetooth Sensor is a todo.");
                break;
            default:
                // TODO: todo ...
                Log.i("SensorDataCollector", "unknown sensor. unexpected.");
                break;
        }

        //getSupportFragmentManager().popBackStack();
        // FragmentUtil.switchToFragment(this,
        // mExperimentSensorSelectionFragment, "sensorselection");
    }

    @Override
    public void onButtonAddNoteClicked_ExperimentEditNotesFragment(String note) {
        Log.i("SensorDataCollector", "ExperimentEditNotesFragment: Button Cancel clicked.");
        Log.i("SensorDataCollector", "Entered note: [" + note + "]");        
        note = UserInterfaceUtil.filterOutNewLines(note); 
        Log.i("SensorDataCollector", "Filtered note: [" + note + "]");         
        if (note.trim().length() > 0)
            mExperiment.getNotes().add(new Note(note));
        getSupportFragmentManager().popBackStack();
    }   

    public ExperimentSensorSelectionFragment getExperimentSensorSensorSelectionFragment()
    {
        return mExperimentSensorSelectionFragment;
    }

    public ExperimentEditNotesFragment getExperimentEditNotesFragment()
    {
        return mExperimentEditNotesFragment;
    }

    public ExperimentSensorSetupFragment getSensorSetupFragment()
    {
        return mSensorSetupFragment;
    }

    public ExperimentSetupFragment getExperimentSetupFragment() {
        return mExperimentSetupFragment;
    }

    public ExperimentRunFragment getExperimentRunFragment() {
        return mExperimentRunFragment;
    }

    public DataCollectionState getCurrentCollectionState()
    {
        return mCollectionState;
    }

    public Experiment getExperiment() {
        return mExperiment;
    }

    public void selectSensors(List<AbstractSensor> lstSensorsTo) {
        for (AbstractSensor sensorTo : lstSensorsTo) {
            for (AbstractSensor sensorFrom : mExperiment.getSensors()) {
                if (sensorTo.isSameSensor(sensorFrom)) {
                    sensorTo.setSelected(true);
                    break;
                }
            }
        }
    }

    private void runExperiment(){
    	Intent intent=new Intent(this,RunExperimentService.class);
    	intent.putExtra("experiment", mExperiment);
    	startService(intent);
    	
    	updateSensorList();
    	
    	// TODO: retrieve the number of sensors checked from the service (getter?)
        CharSequence text = "Started data collection for "+ mExperiment.getSensors().size() + " Sensors";
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    
    private void updateSensorList() {
        Iterator<AbstractSensor> iter = SensorDiscoverer.discoverSensorList(this).iterator();
        ArrayList<AbstractSensor> lstSensors = new ArrayList<AbstractSensor>();

        while (iter.hasNext()) {
            AndroidSensor sensor = (AndroidSensor) iter.next();
            if (sensor.isSelected()) {
                lstSensors.add(sensor);
            }
        }

        mExperiment.setSensors(lstSensors); 
    }

    private void stopExperiment() throws IOException {
    	
    	Intent intent=new Intent(this,RunExperimentService.class);
    	stopService(intent);
    	
        mExperiment.setDateTimeDone(DateFormat.getDateTimeInstance().format(
                Calendar.getInstance().getTime()));

        StoreSingleton.getInstance().writeExperimentMetaData(mExperiment);

        StoreSingleton.getInstance().closeAllChannels();

        // TODO: retrieve the number of sensors checked from the service (getter?)
        CharSequence text = "Stopped data collection for " + mExperiment.getSensors().size() + " Sensors";
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTagClicked_ExperimentRunTaggingFragment(AdapterView<?> gridview, View view,
            int position) {
        if (mPreviousTagPosition != position) { // pressed different tags or
                                                // first press
            Log.i("SensorDataCollector", "Tagging: first tag or different tag pressed.");
            Log.i("SensorDataCollector", "previous tag position = " + mPreviousTagPosition
                    + "\t" + "current tag position = " + position);

            StateTag stateTag = (StateTag) gridview.getItemAtPosition(position);

            if (mPreviousTagPosition >= 0) { // pressed different tags

                switch (mStateTagPrevious.getState()) {
                    case TAG_ON:
                        // turn off previous tag
                        mStateTagPrevious.setState(TaggingState.TAG_OFF);
                        UserInterfaceUtil.setViewBackgroundCompatible(gridview.getChildAt(mPreviousTagPosition),
                                mDrawableBackground);
                        /*
                        gridview.getChildAt(mPreviousTagPosition).setBackgroundColor(
                                getResources().getColor(android.R.color.background_light));
                                */
                        mExperiment.getTaggingActions()
                                .add(new TaggingAction(mStateTagPrevious.getTag(),
                                        new ExperimentTime(),
                                        TaggingState.TAG_OFF));
                        break;
                    case TAG_OFF:
                    case TAG_CONTEXT:
                }
            } else {
                mDrawableBackground = view.getBackground();
            }

            // turn on current tag
            stateTag.setState(TaggingState.TAG_ON);
            mExperiment.getTaggingActions()
                    .add(new TaggingAction(stateTag.getTag(), new ExperimentTime(),
                            TaggingState.TAG_ON));
            view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

            mPreviousTagPosition = position;
            mStateTagPrevious = stateTag;
        } else { // pressed the same button

            Log.i("SensorDataCollector", "Tagging: first tag or different tag pressed.");
            Log.i("SensorDataCollector", "previous tag position = " + mPreviousTagPosition
                    + "\t" + "current tag position = " + position);

            StateTag stateTag = (StateTag) gridview.getItemAtPosition(position);

            switch (stateTag.getState()) {
                case TAG_ON:
                    // turn it off
                    stateTag.setState(TaggingState.TAG_OFF);
                    UserInterfaceUtil.setViewBackgroundCompatible(view, mDrawableBackground);
                    /*
                    view.setBackgroundColor(getResources().getColor(
                            android.R.color.background_light));
                            */
                    mExperiment.getTaggingActions()
                            .add(new TaggingAction(mStateTagPrevious.getTag(),
                                    new ExperimentTime(),
                                    TaggingState.TAG_OFF));
                    break;
                case TAG_OFF:
                    // turn it on
                    stateTag.setState(TaggingState.TAG_ON);
                    view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mExperiment.getTaggingActions()
                            .add(new TaggingAction(stateTag.getTag(), new ExperimentTime(),
                                    TaggingState.TAG_ON));
                    break;
                case TAG_CONTEXT:
            }

            mPreviousTagPosition = position;
            mStateTagPrevious = stateTag;
        }
    }
    
    @Override
    public void onBackPressed() {
        if(mExperimentRunFragment!=null){
        	if(mExperimentRunFragment.isFragmentUIActive()){
        		mExperimentRunFragment.setIsUserTrigger(true);
	        	Intent homeIntent = new Intent(this, SensorDataCollectorActivity.class);
	        	homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(homeIntent);
        	}
        }
        else
        	super.onBackPressed();
    }
    
    @Override
    public void onBtnDoneClicked_ExperimentRunFragment() {
    	mExperimentRunFragment.setIsUserTrigger(true);
        Intent intent = new Intent(this, SensorDataCollectorActivity.class);
        startActivity(intent);
    }
     
    @Override
    public void onTagsClicked_ExperimentSetupFragment() {
        if (mExperimentEditTagsFragment == null) {
            mExperimentEditTagsFragment = new ExperimentEditTagsFragment();
        }
        FragmentUtil.switchToFragment(this, mExperimentEditTagsFragment, "edittags");
    }

    @Override
    public void onNotesClicked_ExperimentSetupFragment() {
        if (mExperimentEditNotesFragment == null) {
            mExperimentEditNotesFragment = new ExperimentEditNotesFragment();
        }
        FragmentUtil.switchToFragment(this, mExperimentEditNotesFragment, "editnotes");

    }

    @Override
    public void onSensorsClicked_ExperimentSetupFragment() {
        if (mExperimentSensorSelectionFragment == null) {
            mExperimentSensorSelectionFragment = new ExperimentSensorSelectionFragment();
        }
        getIntent().putExtra("havingheader", true);
        getIntent().putExtra("havingfooter", true);
        FragmentUtil.switchToFragment(this, mExperimentSensorSelectionFragment, "sensorselection");
    }

    @Override
    public void onBtnRunClicked_ExperimentSetupFragment(View view) {
        if (mExperimentRunFragment == null)
            mExperimentRunFragment = new ExperimentRunFragment();

        Log.i("SensorDataCollector",
                "CreateExperimentActivity::onBtnRunClicked_ExperimentSetupFragment(): "
                        + view.findViewById(R.id.et_experiment_setup_name));
        mExperiment.setName(((EditText) view
                .findViewById(R.id.et_experiment_setup_name)).getText()
                .toString());

        FragmentUtil.switchToFragment(this, mExperimentRunFragment,
                "experimentrun");
    }

/*    @Override
    public void onBtnBackClicked_ExperimentSetupFragment() {
        Intent intent = new Intent(this, SensorDataCollectorActivity.class);
        startActivity(intent);
    }*/
    
    @Override
    public void runExperiment_ExperimentRunFragment(View v) {
        if (mCollectionState == DataCollectionState.DATA_COLLECTION_STOPPED) {
            runExperiment();
			mCollectionState = DataCollectionState.DATA_COLLECTION_IN_PROGRESS;
        } else {
            Toast.makeText(this, "Unsupported Button Action", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void stopExperiment_ExperimentRunFragment(View v) {
        if (mCollectionState == DataCollectionState.DATA_COLLECTION_IN_PROGRESS) {
            try {
                stopExperiment();
            } catch (IOException e) {
                Log.e("SensorDataCollector", e.toString());
            }
            mCollectionState = DataCollectionState.DATA_COLLECTION_STOPPED;
        } else {
        	Toast.makeText(CreateExperimentActivity.this, "Unsupported Button Action", Toast.LENGTH_SHORT).show();
        }
    }
     
/*    @Override
    public void onBtnConfirmClicked_ExperimentSensorSelectionFragment() {
        getIntent().putExtra("havingheader", false);
        getIntent().putExtra("havingfooter", false);
        //FragmentUtil.switchToFragment(this, mExperimentSetupFragment, "sensorsetup");
    }*/

    @Override
    public void onBtnClearClicked_ExperimentSensorSelectionFragment() {
        Iterator<AbstractSensor> iter = SensorDiscoverer.discoverSensorList(this).iterator();
        while (iter.hasNext()) {
            AndroidSensor sensor = (AndroidSensor) iter.next();
            if (sensor.isSelected()) {
                sensor.setSelected(false);
            }
        }

        mExperimentSensorSelectionFragment.getSensorListAdapter().notifyDataSetChanged();
    }


    @Override
    protected void onStop() {
    	super.onStop();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	// Toast.makeText(this, "onDestroy()", Toast.LENGTH_LONG).show();
    	Log.i("onDestroy", "onDestroy()");

    }
    
    @Override
    public void onDetachedFromWindow() {
    	super.onDetachedFromWindow();
    }
    
    public void onSensorClicked_ExperimentSensorSelectionFragment(AndroidSensor sensor) {
        Log.i("SensorDataCollector",
                "CreateExperimentActivity::onSensorClicked_ExperimentSensorSelectionFragment() called.");
        if (mSensorSetupFragment == null) {
            mSensorSetupFragment = new ExperimentSensorSetupFragment();
        }
        mSensorSetupFragment.setSensor(sensor);
        FragmentUtil.switchToFragment(this, mSensorSetupFragment, "sensorsetup");
    }
    
	public ExperimentSensorSelectionFragment getExperimentSensorSelectionFragment() {
		return mExperimentSensorSelectionFragment;
	}

	public ExperimentEditTagsFragment getExperimentEditTagsFragment() {
		return mExperimentEditTagsFragment;
	}

	public ExperimentSensorSetupFragment getExperimentSensorSetupFragment() {
		return mSensorSetupFragment;
	}
	
	@Override
	public void onBtnAddTagClicked_ExperimentEditTagsFragment(String strTag, String strDescription) {
		mExperiment.addTag(strTag, strDescription);

	}

	public RunExperimentService getRunExperimentService() {
		return mRunExperimentService;
	}

	public void setRunExperimentService(RunExperimentService runExperimentService) {
		this.mRunExperimentService = runExperimentService;
	}
}
