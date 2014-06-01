package sysnetlab.android.sdc.test;

import java.util.Iterator;
import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datastore.AbstractStore;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.sensor.SensorUtilsSingleton;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import sysnetlab.android.sdc.ui.adaptors.SensorListAdapter;
import sysnetlab.android.sdc.ui.adaptors.SensorPropertyListAdapter;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ViewExperimentActivityFunctionTests extends
ActivityInstrumentationTestCase2<ViewExperimentActivity> {
	
	private ViewExperimentActivity mViewExperimentActivity;
	private Experiment mExperiment;
	private Context mContext;
	private AbstractStore mStore;

	public ViewExperimentActivityFunctionTests() {
        super(ViewExperimentActivity.class);
    }
	
	public ViewExperimentActivityFunctionTests(Class<ViewExperimentActivity> activityClass) {
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
        
        //setup the store and the sensorUtilSingleton
        mContext = getInstrumentation().getTargetContext();
        SensorUtilsSingleton.getInstance().setContext(mContext);
        mStore = StoreSingleton.getInstance();
        ExperimentManagerSingleton.getInstance().addExperimentStore(
                mStore);
        
        //list all experiments and get one suitable experiment
        List<Experiment> listExperiments= ExperimentManagerSingleton.getInstance().getExperimentsSortedByDate();
        Iterator<Experiment> it=listExperiments.iterator();
        while(it.hasNext()){
        	mExperiment=(Experiment) it.next();
        	if(mExperiment.getSensors().size()>0)
        		break;
        }
        if(mExperiment.getSensors().size()>0){
	        ExperimentManagerSingleton.getInstance().setActiveExperiment(mExperiment);	        
	        mViewExperimentActivity=getActivity();
	        getInstrumentation().waitForIdleSync();
        }
        else
        	Log.e("Test Setup Error", "At least one sensor is required in one of the created experimentes in order to run some tests");
    }
    
    public void testViewExperimentActivityLoaded()
	{
    	assertNotNull("Failed to get ViewExperimentActivity", mViewExperimentActivity);
    	assertNotNull("Failed to get the context", mContext);
    	assertNotNull("Failed to get the store", mStore);
		assertNotNull("The ExperimentViewFragment was not loaded", mViewExperimentActivity.getExperimentViewFragment());
		assertNotNull("The activity was not loaded", mViewExperimentActivity.findViewById(R.id.fragment_container));
		assertEquals("The set experiment and the activity experiment must be the same", mExperiment, mViewExperimentActivity.getExperiment());
	}
    
    public void testButtonsPosition(){
		RelativeLayout buttons = (RelativeLayout) mViewExperimentActivity.findViewById(R.id.layout_experiment_view_buttons);
		assertNotNull("View buttons not allowed to be null", buttons);
		LinearLayout sensorList = (LinearLayout) mViewExperimentActivity.findViewById(R.id.layout_experiment_view_sensor_list);
		assertNotNull("Sensor list not allowed to be null", sensorList);
		int buttonsTop, sensorsBottom;				
		buttonsTop = buttons.getTop();
		sensorsBottom = sensorList.getBottom();
		assertTrue("The sensor list is not above the clone button", buttonsTop >= sensorsBottom);
    }
    
    public void testSensorListClicked() throws Exception {
		String sensorClicked,sensorDisplayed;
		SensorPropertyListAdapter sensorPropertyListAdapter;
		ListView sensorProperties;
		final ListView sensorsList;
		
		sensorsList = (ListView) mViewExperimentActivity.findViewById(R.id.listview_experiment_view_sensor_list);
		
		if(sensorsList.getCount()>0){
			SensorListAdapter sensorListAdapter = (SensorListAdapter) sensorsList.getAdapter();
			sensorClicked=sensorListAdapter.getSensors().get(0).getName();
			
			getInstrumentation().runOnMainSync(new Runnable() {
				@Override
				public void run() {
					sensorsList.performItemClick(
							sensorsList.getAdapter().getView(0, null, null), 
							0, 
							sensorsList.getAdapter().getItemId(0));
				}
			});
			getInstrumentation().waitForIdleSync();
			sensorProperties=(ListView) mViewExperimentActivity.
					findViewById(R.id.listview_fragment_experiment_view_sensor_data_sensor_properties);
			sensorPropertyListAdapter = (SensorPropertyListAdapter) sensorProperties.getAdapter();
			sensorDisplayed=sensorPropertyListAdapter.getProperties().get(0).getValue();
			assertEquals("The sensor selected in the list differs from the sensor being displayed", 
					sensorClicked,
					sensorDisplayed);
		}
	}
}
