
package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.ui.SensorDataCollectorActivity;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class ActivityIntegrationFunctionTest extends
        ActivityInstrumentationTestCase2<SensorDataCollectorActivity> {
    private int mMaximumNumberOfTags;

    public ActivityIntegrationFunctionTest() {
        super(SensorDataCollectorActivity.class);
    }

    public ActivityIntegrationFunctionTest(Class<SensorDataCollectorActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMaximumNumberOfTags = 5;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCloneExperimentWithTags() {
        Activity activity;

        // start SensorDataCollector activity
        activity = TestHelper.launchSensorDataCollectorActivity(this);

        // launch CreateExperimentActivity by clicking the Create-New-Experiment button
        activity = TestHelper.launchCreateExperimentActivity(this, activity);

        // add a few tags
        TestHelper.enterTags(this, activity, mMaximumNumberOfTags);
        
        // go back to experiment setup screen
        TestHelper.goBack(this);
        
        // run the experiment
        TestHelper.startExperiment(this, activity);
        
        // stop the experiment
        activity = TestHelper.stopExperiment(this, activity);
        
        // view the most recent experiment
        activity = TestHelper.viewMostRecentExperiment(this, activity);
      
        // clone the experiment
        activity = TestHelper.cloneExperiment(this, activity);

        // run the experiment
        TestHelper.startExperiment(this, activity);
        
        // stop the experiment
        TestHelper.stopExperiment(this, activity);
    }
}
