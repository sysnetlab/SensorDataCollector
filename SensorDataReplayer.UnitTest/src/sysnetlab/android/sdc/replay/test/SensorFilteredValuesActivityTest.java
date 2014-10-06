package sysnetlab.android.sdc.replay.test;

import sysnetlab.android.sdc.replay.MockSensingContext;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;

public class SensorFilteredValuesActivityTest 
		extends ActivityUnitTestCase<SensorFilteredValuesActivity> {

	public SensorFilteredValuesActivityTest() {
		super(SensorFilteredValuesActivity.class);
	}

	public void setUp() throws Exception {
	}

	public void tearDown() throws Exception {
	}
	
	public void testCreateActivity() throws Exception {
        Context mockContext = new MockSensingContext(getInstrumentation().getTargetContext());
        setActivityContext(mockContext);
        startActivity(new Intent(), null, null);
        final SensorFilteredValuesActivity activity = (SensorFilteredValuesActivity) getActivity();
        assertNotNull(activity);
        //assertEquals("This is sample text", activity.getText());
	}
}
