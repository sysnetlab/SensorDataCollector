package sysnetlab.android.sdc.test;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManager;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.sensor.SensorUtilsSingleton;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.SensorDataCollectorActivity;
import sysnetlab.android.sdc.ui.fragments.ExperimentListFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentRunFragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class RunExperimentFunctionTests extends
        ActivityInstrumentationTestCase2<CreateExperimentActivity> {
    
    private CreateExperimentActivity mCreateExperimentActivity;        
    
    public RunExperimentFunctionTests() {
        super(CreateExperimentActivity.class);
      }    

    public RunExperimentFunctionTests(Class<CreateExperimentActivity> activityClass) {
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
        
    }

    public void testExperimentRun() throws Exception{
        // this is needed because in in two test cases, instances of 
        // SimpleFileStore and SimpleXMLFileStore have been created using
        // their constructors, which are not expected behavior since in
        // one app, only one instance is allowed. 
        StoreSingleton.resetInstance();
    	Instrumentation instrumentation = getInstrumentation();
    	//Create the experiment to be run
    	Experiment activeExperiment = new Experiment();
    	activeExperiment.setName("Unit Test Experiment");
    	
    	//Set the active experiment
    	Context context = getInstrumentation().getTargetContext();
        SensorUtilsSingleton.getInstance().setContext(context);
        ExperimentManager experimentManager = ExperimentManagerSingleton.getInstance();
        experimentManager.addExperimentStore(StoreSingleton.getInstance());
        experimentManager.setActiveExperiment(activeExperiment);
    	
    	//Start activity cloning the active experiment
    	Intent intent = new Intent(Intent.ACTION_MAIN);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	intent.putExtra(SensorDataCollectorActivity.APP_OPERATION_KEY,
                SensorDataCollectorActivity.APP_OPERATION_CLONE_EXPERIMENT);
    	this.setActivityIntent(intent);
    	mCreateExperimentActivity = (CreateExperimentActivity) this.getActivity();
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(CreateExperimentActivity.class.getName(), null, false);        
    	//Get the cloned experiment with new date
        activeExperiment = mCreateExperimentActivity.getExperiment();
        
    	Button runButton = (Button) mCreateExperimentActivity.findViewById(R.id.button_experiment_run);
    	
    	assertNotNull("The button experiment run failed to load", runButton);
    	TouchUtils.clickView(this, runButton);
    	getInstrumentation().waitForIdleSync();
    	ExperimentRunFragment runFragment = mCreateExperimentActivity.getExperimentRunFragment();
    	assertNotNull("The run fragment cannot be null", runFragment);
    	Button doneButton = (Button) mCreateExperimentActivity.findViewById(R.id.button_experiment_done);
    	
    	TouchUtils.clickView(this, doneButton);
    	instrumentation.waitForIdleSync();
    	
    	AlertDialog dialog = mCreateExperimentActivity.getAlertDialog();
    	assertNotNull("The Alert Dialog was not loaded", dialog);
    	assertTrue("Not showing the Alert Dialog", dialog.isShowing());
    	Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
    	assertNotNull("The positive button was not loaded", negativeButton);
    	
    	TouchUtils.clickView(this, negativeButton);
    	instrumentation.waitForIdleSync();
    	
    	assertTrue("The run is not UI Active", runFragment.isFragmentUIActive());    	
    	
    	TouchUtils.clickView(this, doneButton);
    	instrumentation.waitForIdleSync();
    	dialog = mCreateExperimentActivity.getAlertDialog();
    	Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
    	
    	instrumentation.removeMonitor(monitor);
    	monitor = instrumentation.addMonitor(SensorDataCollectorActivity.class.getName(), null, false);
    	TouchUtils.clickView(this, positiveButton);
    	getInstrumentation().waitForIdleSync();
    	
    	SensorDataCollectorActivity sensorDataCollectorActivity = 
    			(SensorDataCollectorActivity)instrumentation.waitForMonitor(monitor);
    	assertNotNull("SensorDataCollector Activity was not loaded", sensorDataCollectorActivity);
    	
    	ExperimentListFragment experimentListFragment = sensorDataCollectorActivity.getExperimentListFragment();
    	assertNotNull("The fragment was not loaded", experimentListFragment);
    	    	      
        List<Experiment> listExperiments = experimentManager.getExperimentsSortedByDate();
    	assertEquals("The experiment created is not on the top of the list", listExperiments.get(0), activeExperiment);    	    
    	
    	
    	sensorDataCollectorActivity.finish();
    }       
}
