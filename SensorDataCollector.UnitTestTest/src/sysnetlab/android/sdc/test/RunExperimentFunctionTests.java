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
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class RunExperimentFunctionTests extends
        ActivityInstrumentationTestCase2<CreateExperimentActivity> {
    
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
        setActivityInitialTouchMode(false);
        
    }

    public void testExperimentRun() throws Exception{
        // this is needed because in in two test cases, instances of 
        // SimpleFileStore and SimpleXMLFileStore have been created using
        // their constructors, which are not expected behavior since in
        // one app, only one instance is allowed. 
        StoreSingleton.resetInstance();

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
    	CreateExperimentActivity createExperimentActivity = (CreateExperimentActivity) this.getActivity();
    	getInstrumentation().waitForIdleSync();
    	assertNotNull("The CreateExperimentActivity should not be null.", createExperimentActivity);
    	//Get the cloned experiment with new date
        activeExperiment = createExperimentActivity.getExperiment();
        
    	Button runButton = (Button) createExperimentActivity.findViewById(R.id.button_experiment_run);
    	assertNotNull("The button experiment run failed to load", runButton);
    	
    	TouchUtils.clickView(this, runButton);
    	getInstrumentation().waitForIdleSync();
    	ExperimentRunFragment runFragment = createExperimentActivity.getExperimentRunFragment();
    	assertNotNull("The run fragment cannot be null", runFragment);
    	
    	Button doneButton = (Button) createExperimentActivity.findViewById(R.id.button_experiment_done);
    	assertNotNull("The done button cannot be null.", doneButton);
    	
    	TouchUtils.clickView(this, doneButton);
    	getInstrumentation().waitForIdleSync();
     	AlertDialog dialog = createExperimentActivity.getAlertDialog();
    	assertNotNull("The Alert Dialog was not loaded", dialog);
    	assertTrue("Not showing the Alert Dialog", dialog.isShowing());
    	Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
    	assertNotNull("The negative button was not loaded", negativeButton);
    	
    	TouchUtils.clickView(this, negativeButton);
    	getInstrumentation().waitForIdleSync();
    	
    	assertTrue("The run is not UI Active", runFragment.isFragmentUIActive());    	
    	
    	TouchUtils.clickView(this, doneButton);
    	getInstrumentation().waitForIdleSync();
    	dialog = createExperimentActivity.getAlertDialog();
    	Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        assertNotNull("The positive button was not loaded", positiveButton);
    	
    	ActivityMonitor monitor = getInstrumentation().addMonitor(SensorDataCollectorActivity.class.getName(), null, false);
    	TouchUtils.clickView(this, positiveButton);
        getInstrumentation().waitForIdleSync();  
    	
    	SensorDataCollectorActivity sensorDataCollectorActivity = 
    			(SensorDataCollectorActivity)getInstrumentation().waitForMonitor(monitor);
    	assertNotNull("SensorDataCollector Activity was not loaded", sensorDataCollectorActivity);
    	getInstrumentation().removeMonitor(monitor);

        
    	ExperimentListFragment experimentListFragment = sensorDataCollectorActivity.getExperimentListFragment();
    	assertNotNull("The fragment was not loaded", experimentListFragment);
    	    	      
        List<Experiment> listExperiments = experimentManager.getExperimentsSortedByDate();
    	assertEquals("The experiment created is not on the top of the list", listExperiments.get(0), activeExperiment);
    }       
}
