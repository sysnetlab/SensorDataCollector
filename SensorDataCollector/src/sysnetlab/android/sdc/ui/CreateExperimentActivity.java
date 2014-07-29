
package sysnetlab.android.sdc.ui;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datacollector.ExperimentTime;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.StateTag;
import sysnetlab.android.sdc.datacollector.TaggingAction;
import sysnetlab.android.sdc.datacollector.TaggingState;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.services.RunExperimentService;
import sysnetlab.android.sdc.ui.fragments.ExperimentDataStoreFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentEditNotesFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentEditTagsFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentRunFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentRunTaggingFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorSelectionFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorListFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorSetupFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSetupFragment;
import sysnetlab.android.sdc.ui.fragments.FragmentUtil;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateExperimentActivity extends FragmentActivityBase
        implements
        ExperimentSensorSetupFragment.OnFragmentClickListener,
        ExperimentSetupFragment.OnFragmentClickListener,
        ExperimentSensorSelectionFragment.OnFragmentClickListener,
        ExperimentSensorListFragment.OnFragmentClickListener,
        ExperimentEditTagsFragment.OnFragmentClickListener,
        ExperimentRunFragment.OnFragmentClickListener,
        ExperimentRunFragment.ExperimentHandler,
        ExperimentRunTaggingFragment.OnFragmentClickListener,
        ExperimentEditNotesFragment.OnFragmentClickListener
{
    private final int LEAVE_ACTION_BACK_BUTTON = 0;
    private final int LEAVE_ACTION_UP_BUTTON = 1;

	private final int MICROSECONS_IN_A_SECOND = 1000000;
    
    private ExperimentSetupFragment mExperimentSetupFragment;
    private ExperimentSensorSelectionFragment mExperimentSensorSelectionFragment;
    private ExperimentSensorSetupFragment mSensorSetupFragment;
    private ExperimentEditNotesFragment mExperimentEditNotesFragment;
    private ExperimentEditTagsFragment mExperimentEditTagsFragment;
    private ExperimentDataStoreFragment mExperimentDataStoreFragment;
    private ExperimentRunFragment mExperimentRunFragment;

    private DataCollectionState mCollectionState;

    private Experiment mExperiment;
    
    private int mPreviousTagPosition;
    private StateTag mStateTagPrevious;
    private Drawable mDrawableBackground;
    private TextView mTextView;
    private AlertDialog mAlertDialog;

	private int mOperation;     

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

    public AlertDialog getAlertDialog(){
        return mAlertDialog;
    }
    
    public RunExperimentService getRunExperimentService() {
        return mRunExperimentService;
    }

    public void setRunExperimentService(RunExperimentService runExperimentService) {
        this.mRunExperimentService = runExperimentService;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO handle configuration change
		super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        
        if (!SensorDiscoverer.isInitialized())
            SensorDiscoverer.initialize(getApplicationContext());
        
        mLoadingTask = new TaskLoadingSpinner();
        
        mOperation = getIntent().getIntExtra(SensorDataCollectorActivity.APP_OPERATION_KEY,
                SensorDataCollectorActivity.APP_OPERATION_CREATE_NEW_EXPERIMENT);
        
        mLoadingTask.execute();
    }
    
    @Override
    public void loadTask() {
    	ViewPager vp=new ViewPager(this);
		vp.setOffscreenPageLimit(5);
		mPreviousTagPosition = -1;
    	
    	View view = findViewById(R.id.fragment_container);
    	mExperimentSetupFragment=new ExperimentSetupFragment();
    	switch (mOperation) {
			case SensorDataCollectorActivity.APP_OPERATION_CREATE_NEW_EXPERIMENT:
				mExperiment = new Experiment();
		    	ExperimentManagerSingleton.getInstance().setActiveExperiment(mExperiment);
		        for (AbstractSensor sensor : SensorDiscoverer.discoverSensorList()) {
		            sensor.setSelected(false);
		        }
		        if (view != null) {
		            FragmentUtil.addFragment(this,mExperimentSetupFragment);
		        }
		        mCollectionState = DataCollectionState.DATA_COLLECTION_STOPPED;
		        Log.i("SensorDataCollector", "Leaving CreateExperimentActivit::onCreate."); 
				break;
				
			case SensorDataCollectorActivity.APP_OPERATION_CLONE_EXPERIMENT:
				mExperiment = ExperimentManagerSingleton.getInstance().getActiveExperiment().clone();
				if (view != null) {
		            FragmentUtil.addFragment(this, mExperimentSetupFragment);
		        }
		        mCollectionState = DataCollectionState.DATA_COLLECTION_STOPPED;
		        Log.d("SensorDataCollector", "Leaving CreateExperimentActivit::onCreate:initializeCloneExperimentUI().");
		        break;
		}
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        // Start the local service      
        startService(new Intent(this, RunExperimentService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        
        // Stop the local service
        //stopService(new Intent(this, RunExperimentService.class));
    }    
    
    @Override
    protected void onPause() {
        super.onPause();      
        
        // unbind the local service 
        if (mRunExperimentServiceBound) {
            unbindService(mRunExperimentServiceConnection);
            mRunExperimentServiceBound = false;      
        }        
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        // bind the local service
        boolean status = UserInterfaceUtil.bindRunExperimentServiceCompatible(this, new Intent(
                this, RunExperimentService.class), mRunExperimentServiceConnection);
        Log.d("SensorDataCollector", "CreateExperimentActivity::onStart() called. status = "
                + status);
    }           

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
        switch(item.getItemId()) {
	        case android.R.id.home:
	        	if(mExperimentRunFragment!=null && mExperimentRunFragment.isFragmentUIActive()){	            	
	        		confirmToStopExperiment();
	            	//return false;	    	        		            	
	            }
	        	else{
	            	confirmToLeaveActivity(LEAVE_ACTION_UP_BUTTON, item);
	            	//return false;
	            }
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }	
    
    @Override
    public void onBackPressed() {
        if (mExperimentRunFragment != null && mExperimentRunFragment.isFragmentUIActive()) {            
            confirmToStopExperiment();            
        } else if(mExperimentSetupFragment != null && mExperimentSetupFragment.isFragmentUIActive()){
            confirmToLeaveActivity(LEAVE_ACTION_BACK_BUTTON,null);
        } else {
            super.onBackPressed();
        }
        changeActionBarTitle(R.string.text_creating_experiment, R.drawable.ic_launcher);
    }       
     
    @Override
    public void onTagsClicked_ExperimentSetupFragment() {
        if (mExperimentEditTagsFragment == null) {
            mExperimentEditTagsFragment = new ExperimentEditTagsFragment();
        }
        FragmentUtil.switchFragment(this, 
        		mExperimentEditTagsFragment, 
        		"edittags",
        		FragmentUtil.FRAGMENT_SWITCH_ADD_TO_BACKSTACK);
        changeActionBarTitle(R.string.text_creating_tags, R.drawable.icon_tags_inverse);
    }

    @Override
    public void onNotesClicked_ExperimentSetupFragment() {
    	if (mExperimentEditNotesFragment == null) {
            mExperimentEditNotesFragment = new ExperimentEditNotesFragment();
        }
        FragmentUtil.switchFragment(this, 
        		mExperimentEditNotesFragment, 
        		"editnotes",
        		FragmentUtil.FRAGMENT_SWITCH_ADD_TO_BACKSTACK);        
        changeActionBarTitle(R.string.text_creating_notes, R.drawable.icon_notes_inverse);
    }

    @Override
    public void onSensorsClicked_ExperimentSetupFragment() {
        Log.d("SensorDataCollector", this.getClass().getSimpleName()
                + "::onSensorsClicked_ExperimentSetupFragment() called");
        if (mExperimentSensorSelectionFragment == null) {
            mExperimentSensorSelectionFragment = new ExperimentSensorSelectionFragment();
        }
        getIntent().putExtra("havingheader", true);
        FragmentUtil.switchFragment(this, 
        		mExperimentSensorSelectionFragment, 
        		"sensorselection",
        		FragmentUtil.FRAGMENT_SWITCH_ADD_TO_BACKSTACK);
        changeActionBarTitle(R.string.text_selecting_sensors, R.drawable.icon_sensors_inverse);
    }
    
    @Override
    public void onDataStoreClicked_ExperimentSetupFragment() {
        Log.d("SensorDataCollector", this.getClass().getSimpleName()
                + "::onDataStoreClicked_ExperimentSetupFragment() called");
        
        if (mExperimentDataStoreFragment == null) {
            mExperimentDataStoreFragment = new ExperimentDataStoreFragment();
        }
        FragmentUtil.switchFragment(this, 
        		mExperimentDataStoreFragment, 
        		"datastoreselection",
        		FragmentUtil.FRAGMENT_SWITCH_ADD_TO_BACKSTACK);
    }

    @Override
    public void onBtnRunClicked_ExperimentSetupFragment(View view) {
    	
    	if(mExperimentSensorSelectionFragment==null || 
    			!mExperimentSensorSelectionFragment.hasSensorsSelected()){
    		
    		alertNoSensorSelected();
    		
    	}else{
    		if (mExperimentRunFragment == null)
                mExperimentRunFragment = new ExperimentRunFragment();

            Log.d("SensorDataCollector",
                    "CreateExperimentActivity::onBtnRunClicked_ExperimentSetupFragment(): "
                            + view.findViewById(R.id.et_experiment_setup_name));
            mExperiment.setName(((EditText) view
                    .findViewById(R.id.et_experiment_setup_name)).getText()
                    .toString());

            FragmentUtil.switchFragment(this, 
            		mExperimentRunFragment,
                    "experimentrun",
                    FragmentUtil.FRAGMENT_SWITCH_NO_BACKSTACK);
    	}
    }
    
    @Override
    public boolean onBtnAddTagClicked_ExperimentEditTagsFragment(String strTag,
            String strDescription) {
        if (strTag.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.text_enter_a_tag),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!mExperiment.addTag(strTag, strDescription)) {
            Toast.makeText(this, getResources().getString(R.string.text_tag_must_be_unique),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onButtonAddNoteClicked_ExperimentEditNotesFragment(String note) {
        Log.d("SensorDataCollector", "ExperimentEditNotesFragment: Button Cancel clicked.");
        Log.d("SensorDataCollector", "Entered note: [" + note + "]");      
        
        note = UserInterfaceUtil.filterOutNewLines(note); 
        
        Log.d("SensorDataCollector", "Filtered note: [" + note + "]");      
        
        if (note.trim().length() > 0)
        	mExperiment.addNote(new Note(note));
        
        ((EditText)this.findViewById(
    			R.id.edittext_experiment_note_editing_note)).
    			getText().clear();
        
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBtnDoneClicked_ExperimentRunFragment() {
        confirmToStopExperiment();
    }
    
    @Override
    public void runExperiment_ExperimentRunFragment() {
        if (mCollectionState == DataCollectionState.DATA_COLLECTION_STOPPED) {
            runExperiment();
			mCollectionState = DataCollectionState.DATA_COLLECTION_IN_PROGRESS;
        } else {
            Toast.makeText(this, "Unsupported Button Action", Toast.LENGTH_LONG).show();
        }
    }

    public void runTimer_ExperimentRunFragment() {
        mTextView = (TextView) mExperimentRunFragment.getView().findViewById(
                R.id.textview_experiment_run_timer);
        mTextView.setText("00:00:00");
        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            int seconds = 0;
            long startMilliSeconds = new Date().getTime();
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                    	seconds = (int)((new Date().getTime()-startMilliSeconds)/1000); 
                        int hr = seconds / 3600;
                        int rem = seconds % 3600;
                        int min = rem / 60;
                        int sec = rem % 60;
                        String hrStr = (hr < 10 ? "0" : "") + hr;
                        String mnStr = (min < 10 ? "0" : "") + min;
                        String secStr = (sec < 10 ? "0" : "") + sec;
                        mTextView.setText(hrStr + ":" + mnStr + ":" + secStr);                        
                    }
                });
            }
        }, 250, 250);
    }
        
    public void notifyInBackground_ExperimentRunFragment(){
    	Intent intent = new Intent(getBaseContext(), CreateExperimentActivity.class);        
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);        
        NotificationCompat.Builder builder = 
        	new NotificationCompat.Builder(this)
        	.setSmallIcon(R.drawable.ic_launcher)
        	.setContentTitle(mExperiment.getName())
        	.setContentText(getText(R.string.text_running_in_background))
        	.setAutoCancel(true)
        	.setTicker(getText(R.string.text_running_in_background))
        	.setContentIntent(pIntent);
        
        NotificationManager notificationManager =
        		(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
    
    public void removeInBackgroundNotification_ExperimentRunFragment(){
    	NotificationManager notificationManager =
        		(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    	notificationManager.cancel(1);
    }
    
    @Override
    public void stopExperiment_ExperimentRunFragment() {
        if (mCollectionState == DataCollectionState.DATA_COLLECTION_IN_PROGRESS) {
            stopExperiment();
            mCollectionState = DataCollectionState.DATA_COLLECTION_STOPPED;
        } else {
        	Toast.makeText(CreateExperimentActivity.this, "Unsupported Button Action", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTagClicked_ExperimentRunTaggingFragment(AdapterView<?> gridview, View view,
            int position) {
        if (mPreviousTagPosition != position) { // pressed different tags or
                                                // first press
            Log.d("SensorDataCollector", "Tagging: first tag or different tag pressed.");
            Log.d("SensorDataCollector", "previous tag position = " + mPreviousTagPosition
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
    
            Log.d("SensorDataCollector", "Tagging: first tag or different tag pressed.");
            Log.d("SensorDataCollector", "previous tag position = " + mPreviousTagPosition
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
    public void onBtnClearClicked_ExperimentSensorSelectionFragment(boolean checked) {
        Iterator<AbstractSensor> iter = SensorDiscoverer.discoverSensorList().iterator();
        while (iter.hasNext()) {
            AbstractSensor sensor = (AbstractSensor) iter.next();            
                sensor.setSelected(checked);            
        }

        mExperimentSensorSelectionFragment.getSensorListAdapter().notifyDataSetChanged();
    }              

    public void onSensorClicked_ExperimentSensorSelectionFragment(AbstractSensor sensor) {
        Log.d("SensorDataCollector",
                "CreateExperimentActivity::onSensorClicked_ExperimentSensorSelectionFragment() called.");
        if (mSensorSetupFragment == null) {
            mSensorSetupFragment = new ExperimentSensorSetupFragment();
        }
        mSensorSetupFragment.setSensor(sensor);
        FragmentUtil.switchFragment(this, 
        		mSensorSetupFragment, 
        		"sensorsetup",
        		FragmentUtil.FRAGMENT_SWITCH_ADD_TO_BACKSTACK);
    }
    

    @Override
    public void onSensorClicked_ExperimentSensorListFragment(AbstractSensor sensor) {
        Log.d("SensorDataCollector",
                "CreateExperimentActivity::onSensorClicked_ExperimentSensorListFragment() called.");        
        if (mSensorSetupFragment == null) {
            mSensorSetupFragment = new ExperimentSensorSetupFragment();
        }
        mSensorSetupFragment.setSensor(sensor);
        FragmentUtil.switchFragment(this, 
        		mSensorSetupFragment, 
        		"sensorsetup",
        		FragmentUtil.FRAGMENT_SWITCH_ADD_TO_BACKSTACK);        
    }
    

    @Override
    public void onSensorClicked_ExperimentSensorListFragment(int sensorNo) {
        // do nothing
    }    

	@Override
    public void onBtnSetParameterClicked_SensorSetupFragment(View v, AbstractSensor sensor) {
        Log.d("SensorDataCollector", "SensorSetupFragment: Button Confirm clicked.");
    
        EditText et = (EditText) findViewById(R.id.edittext_sensor_steup_sampling_rate);
    
        switch (sensor.getMajorType()) {
            case AbstractSensor.ANDROID_SENSOR:
                AndroidSensor androidSensor = (AndroidSensor) sensor;
                if (androidSensor.isStreamingSensor()) {
                    androidSensor.setSamplingInterval((int) (MICROSECONS_IN_A_SECOND / Double
                            .parseDouble(et.getText().toString())));
                    String strSamplingRateMsgFormatter = getResources().getString(
                            R.string.text_sampling_rate_is_now_x);
                    String strSamplingRateMsg = String.format(strSamplingRateMsgFormatter,
                            1. / androidSensor.getSamplingInterval());
                    Toast.makeText(this, strSamplingRateMsg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.text_not_applicable),
                            Toast.LENGTH_LONG).show();
                }
                break;
            case AbstractSensor.AUDIO_SENSOR:
                break;
            case AbstractSensor.CAMERA_SENSOR:
                // TODO: to do ...
                Log.d("SensorDataCollector", "Camera Sensor is a todo.");
                break;
            case AbstractSensor.WIFI_SENSOR:
                // TODO: to do ...
                Log.d("SensorDataCollector", "WiFi Sensor is a todo.");
                break;
            case AbstractSensor.BLUETOOTH_SENSOR:
                // TODO: to do ...
                Log.d("SensorDataCollector", "Bluetooth Sensor is a todo.");
                break;
            default:
                // TODO: to do ...
                Log.d("SensorDataCollector", "unknown sensor. unexpected.");
                break;
        }
    }	
	
	private void confirmToLeaveActivity(final int action,final MenuItem item){
		if(hasChanges()){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(R.string.text_do_you_want_to_leave_experiment)
	                .setTitle(R.string.text_experiment);
	        builder.setPositiveButton(R.string.text_leave_experiment,
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int id) {
	                    	if(action==LEAVE_ACTION_BACK_BUTTON)
	                    		CreateExperimentActivity.super.onBackPressed();
	                    	else if(action==LEAVE_ACTION_UP_BUTTON)
	                    		CreateExperimentActivity.super.onOptionsItemSelected(item);
	                    }
	                });	        
	        builder.setNegativeButton(R.string.text_continue_experiment,
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int id) {
	                        
	                    }
	                });
	        mAlertDialog = builder.create();
	        mAlertDialog.show();
		}
		else{
			if(action==LEAVE_ACTION_BACK_BUTTON)
        		CreateExperimentActivity.super.onBackPressed();
        	else if(action==LEAVE_ACTION_UP_BUTTON)
        		CreateExperimentActivity.super.onOptionsItemSelected(item);
		}
	}

	private void alertNoSensorSelected(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.text_need_to_select_sensors);
                
        builder.setNeutralButton(R.string.text_dismiss,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAlertDialog.dismiss();
                    }
                });
        
        mAlertDialog = builder.create();

        mAlertDialog.show();
	}
	
    private void confirmToStopExperiment() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.text_do_you_want_to_stop_experiment)
                .setTitle(R.string.text_experiment);

        builder.setPositiveButton(R.string.text_stop_experiment,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(CreateExperimentActivity.this,
                                R.string.text_stopping_experiment, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        
                        mExperimentRunFragment.setIsUserTrigger(true);
                        Intent homeIntent = new Intent(CreateExperimentActivity.this,
                                SensorDataCollectorActivity.class);                        
                        homeIntent.putExtra("newExperiment", mExperiment);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        finish();
                    }
                });

        builder.setNegativeButton(R.string.text_continue_experiment,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(CreateExperimentActivity.this,
                                R.string.text_continuing_experiment, Toast.LENGTH_SHORT).show();
                    }
                });

        mAlertDialog = builder.create();

        mAlertDialog.show();
    }        
    
	public void changeActionBarTitle(int titleResId, int iconResId){    	
    	getSupportActionBar().setTitle(titleResId);
    	getSupportActionBar().setIcon(iconResId);    	
    }
    
    private RunExperimentService mRunExperimentService;
    private boolean mRunExperimentServiceBound = false; 
    
    /** Defines call backs for service binding, passed to bindService() */
    private ServiceConnection mRunExperimentServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            mRunExperimentService = ((RunExperimentService.LocalBinder) service).getService();

            mRunExperimentServiceBound = true;

            Log.d("SensorDataCollector", "ServiceConnection::onServiceConnected() called.");
            //Toast.makeText(CreateExperimentActivity.this, "Connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mRunExperimentServiceBound = false;
            //Toast.makeText(CreateExperimentActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
        }
    };
    
    private void runExperiment() {
    	
        if (mRunExperimentServiceBound) {
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    
            mRunExperimentService.runExperimentInService(sensorManager, mExperiment);
    
            CharSequence text = "Started data collection for " + mExperiment.getSensors().size()
                    + " Sensors";
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }

    private void stopExperiment() {
    
        if (mRunExperimentServiceBound) {
    
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    
            mRunExperimentService.stopExperimentInService(sensorManager, mExperiment);
    
            CharSequence text = "Stopped data collection for " + mExperiment.getSensors().size()
                    + " Sensors";
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }
    
    private boolean hasChanges(){
    	if(mExperiment.hasChanges())
    		return true;    	
    	if(mExperimentEditNotesFragment!=null && mExperimentEditNotesFragment.hasNotes())    		
    		return true;
    	if(mExperimentEditTagsFragment!=null && mExperimentEditTagsFragment.hasTags())
    		return true;
    	if(mExperimentSensorSelectionFragment!=null && mExperimentSensorSelectionFragment.hasSensorsSelected())
    		return true;
    	if(mExperimentSetupFragment!=null && mExperimentSetupFragment.hasChanges())
    		return true;
    		
    	return false;
    }
}
