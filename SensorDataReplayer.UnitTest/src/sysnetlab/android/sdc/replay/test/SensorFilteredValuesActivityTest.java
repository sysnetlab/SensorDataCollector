package sysnetlab.android.sdc.replay.test;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import sysnetlab.android.sdc.replay.MockSensingContext;
import sysnetlab.android.sdc.replay.SensingReplayer;
import sysnetlab.android.sdc.replay.example.SensorFilteredValuesActivity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.SensorEventListener;
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
        MockSensingContext mockContext = new MockSensingContext(getInstrumentation().getTargetContext());
        setActivityContext(mockContext);
        startActivity(new Intent(), null, null);
        final SensorFilteredValuesActivity activity = (SensorFilteredValuesActivity) getActivity();
        assertNotNull(activity);
        activity.onResume();
        
        SensorEventListener capturedListener = mockContext.getCapturedEventListener();
        assertNotNull(capturedListener);        
        
        InputStream is = getInstrumentation().getContext().getResources().getAssets().open("sensee-accelerometer-sample.csv");
        SensingReplayer replayer = new SensingReplayer(new InputStreamReader(is, "UTF-8"), capturedListener);
        replayer.replay();
        //assert something
	}
}
