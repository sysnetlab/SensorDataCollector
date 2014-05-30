package sysnetlab.android.sdc.test;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.sensor.SensorUtilSingleton;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ViewExperimentActivityFunctionTests extends
ActivityInstrumentationTestCase2<ViewExperimentActivity> {

	private ViewExperimentActivity mViewExperimentActivity;
	private Experiment mExperiment;

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
        
        
        mExperiment=new Experiment();
        ExperimentManagerSingleton.getInstance().setActiveExperiment(mExperiment);        
        mViewExperimentActivity = (ViewExperimentActivity) getActivity();
        SensorUtilSingleton.getInstance().setContext(mViewExperimentActivity.getBaseContext());
        ExperimentManagerSingleton.getInstance().addExperimentStore(
                StoreSingleton.getInstance());
        List<Experiment> listExperiments= ExperimentManagerSingleton.getInstance().getExperimentsSortedByDate();        
        mExperiment = listExperiments.get(0);
        ExperimentManagerSingleton.getInstance().setActiveExperiment(mExperiment);
        Intent intent = new Intent(getInstrumentation().getTargetContext(), ViewExperimentActivity.class);
        setActivityIntent(intent);
        mViewExperimentActivity.startActivity(intent);
    }
    
//    public void testViewExperimentActivityLoaded()
//	{
//		assertNotNull("The ExperimentViewFragment was not loaded", mViewExperimentActivity.getExperimentViewFragment());
//		assertNotNull("The activity was not loaded", mViewExperimentActivity.findViewById(R.id.fragment_container));
//		assertEquals("The set experiment and the activity experiment must be the same", mExperiment, mViewExperimentActivity.getExperiment());
//	}
    
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
}
