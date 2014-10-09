package sysnetlab.android.sdc.test.robo;

import com.robotium.solo.Solo;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import android.test.ActivityInstrumentationTestCase2;

public class RunExperimentTest extends 
	ActivityInstrumentationTestCase2<CreateExperimentActivity> {

	private Solo solo;
	
	public RunExperimentTest() {
		super(CreateExperimentActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		solo.unlockScreen();
	}

	public void testRunExperiment() throws Exception {
		solo.assertCurrentActivity("wrong activity", CreateExperimentActivity.class);
		//assertTrue(solo.searchText("Probing sensors"));
		assertTrue(solo.waitForText("Create New Experiment", 1, 5000));
		solo.enterText(0, "roboExperiment");
		solo.clickOnText("Sensors");
		solo.clickOnCheckBox(1);
		solo.goBack();
		solo.clickOnButton("Run");		
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}	
}
