/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.sensor.audio.AudioRecordSettingDataSource;
import sysnetlab.android.sdc.ui.SensorDataCollectorActivity;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class ActivityIntegrationFunctionTest extends
        ActivityInstrumentationTestCase2<SensorDataCollectorActivity> {
    private int mMaximumNumberOfTags;
    private Activity mCeActivity;
    private Activity mVeActivity;
    private Activity mSdcActivity;

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
        
        AudioRecordSettingDataSource.initializeInstance(getInstrumentation().getTargetContext());
        AudioRecordSettingDataSource dbSource = AudioRecordSettingDataSource.getInstance();
        dbSource.open();
        if (!dbSource.isDataSourceReady()) {
            dbSource.prepareDataSource();
        }
        dbSource.close();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (mCeActivity != null) mCeActivity.finish();
        if (mVeActivity != null) mVeActivity.finish();
        if (mSdcActivity != null) mSdcActivity.finish();
    }

    public void testCloneExperimentWithTags() {
        // start SensorDataCollector activity
        mSdcActivity = TestHelper.launchSensorDataCollectorActivity(this);

        // launch CreateExperimentActivity by clicking the Create-New-Experiment button
        mCeActivity = TestHelper.launchCreateExperimentActivity(this, mSdcActivity);

        // add a few tags
        TestHelper.enterTags(this, mCeActivity, mMaximumNumberOfTags);
        
        // go back to experiment setup screen
        TestHelper.goBack(this);
        
        // run the experiment
        TestHelper.startExperiment(this, mCeActivity);
        
        // stop the experiment
        mSdcActivity = TestHelper.stopExperiment(this, mCeActivity);
        
        // view the most recent experiment
        mVeActivity = TestHelper.viewMostRecentExperiment(this, mSdcActivity);
      
        // clone the experiment
        mCeActivity = TestHelper.cloneExperiment(this, mVeActivity);

        // run the experiment
        TestHelper.startExperiment(this, mCeActivity);
        
        // stop the experiment
        TestHelper.stopExperiment(this, mCeActivity);
    }
}
