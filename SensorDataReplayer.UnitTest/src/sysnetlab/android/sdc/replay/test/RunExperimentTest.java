package sysnetlab.android.sdc.replay.test;

import junit.framework.Assert;

import com.robotium.solo.Solo;

import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import android.test.ActivityInstrumentationTestCase2;

public class RunExperimentTest extends 
	ActivityInstrumentationTestCase2<CreateExperimentActivity> {

	private Solo solo;

	public RunExperimentTest(Class<CreateExperimentActivity> activityClass) {
		super(activityClass);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		solo.unlockScreen();
	}

	public void testRunExperiment() throws Exception {
			
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}	
}
