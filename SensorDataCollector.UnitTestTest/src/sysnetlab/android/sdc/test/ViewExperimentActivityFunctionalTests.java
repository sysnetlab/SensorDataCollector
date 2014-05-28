package sysnetlab.android.sdc.test;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.sensor.SensorUtilSingleton;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import sysnetlab.android.sdc.ui.fragments.ExperimentViewSensorDataFragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;

public class ViewExperimentActivityFunctionalTests 
	extends ActivityInstrumentationTestCase2<ViewExperimentActivity> {
	
	ViewExperimentActivity mViewExperimentActivity;

	public ViewExperimentActivityFunctionalTests(
			Class<ViewExperimentActivity> activityClass) {
		super(activityClass);
	}
	
	public ViewExperimentActivityFunctionalTests() {
		super(ViewExperimentActivity.class);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		SensorUtilSingleton.getInstance().setContext(getActivity().getBaseContext());
		ExperimentManagerSingleton.getInstance().addExperimentStore(
                StoreSingleton.getInstance());
		ExperimentManagerSingleton.getInstance().setActiveExperiment(ExperimentManagerSingleton.getInstance().getExperimentsSortedByDate().get(0));
		mViewExperimentActivity=(ViewExperimentActivity)getActivity();
	}
	
	public void testSensorListClicked() {
		final ListView listSensors;
		ExperimentViewSensorDataFragment mExperimentViewSensorDataFragment;
		String sensor;
		
		listSensors = (ListView) mViewExperimentActivity.findViewById(R.id.layout_experiment_view_sensor_list);
		assertNotNull("The list of sensors has not been loaded.",listSensors);
		
		if(listSensors.getCount()>0){
			sensor=listSensors.getItemAtPosition(0).toString();
			getInstrumentation().runOnMainSync(new Runnable() {
				
				@Override
				public void run() {
					listSensors.performItemClick(
							listSensors.getAdapter().getView(0, null, null), 
							0, 
							listSensors.getAdapter().getItemId(0));
				}
			});
			
			
		}
	 	
	}

}
