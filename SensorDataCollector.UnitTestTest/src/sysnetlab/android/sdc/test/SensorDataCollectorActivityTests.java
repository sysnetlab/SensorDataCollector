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

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.SensorDataCollectorActivity;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import sysnetlab.android.sdc.ui.fragments.ExperimentListFragment;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.test.TouchUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class SensorDataCollectorActivityTests
        extends android.test.ActivityUnitTestCase<SensorDataCollectorActivity> {

    private SensorDataCollectorActivity mSdcActivity;
    private ExperimentListFragment mExperimentListFragment;
    private ArrayAdapter<Experiment> mExperimentList;

    public SensorDataCollectorActivityTests() {
        super(SensorDataCollectorActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        /*
         * Context context = getInstrumentation().getTargetContext();
         * context.setTheme(R.style.Theme_AppCompat); Intent intent = new
         * Intent(context, SensorDataCollectorActivity.class);
         * startActivity(intent, null, null); sdcActivity = getActivity();
         * getInstrumentation().callActivityOnStart(sdcActivity);
         */
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSensorDataCollectionActivityLoaded()
    {
        Context context = getInstrumentation().getTargetContext();
        context.setTheme(R.style.Theme_AppCompat);

        mSdcActivity = launchActivity(context.getPackageName(), SensorDataCollectorActivity.class,
                null);
        getInstrumentation().waitForIdleSync();

        assertNotNull(mSdcActivity.findViewById(R.id.fragment_container));
        assertNotNull(mSdcActivity.getExperimentListFragment());
    }

    public void testCreateExperimentButtonClicked()
    {
        Context context = getInstrumentation().getTargetContext();
        context.setTheme(R.style.Theme_AppCompat);

        mSdcActivity = launchActivity(context.getPackageName(), SensorDataCollectorActivity.class,
                null);
        getInstrumentation().waitForIdleSync();

        Button view = (Button) mSdcActivity.findViewById(R.id.button_create_experiment);
        assertNotNull("Button not allowed to be null", view);

        ActivityMonitor monitor = getInstrumentation().addMonitor(
                CreateExperimentActivity.class.getName(), null, false);
        TouchUtils.clickView(this, view);
        getInstrumentation().waitForIdleSync();

        CreateExperimentActivity ceActivity =
                (CreateExperimentActivity) getInstrumentation().waitForMonitor(monitor);
        assertNotNull("CreateExperimentActivity Activity was not loaded", ceActivity);
        getInstrumentation().removeMonitor(monitor);

        monitor = getInstrumentation().addMonitor(SensorDataCollectorActivity.class.getName(),
                null, false);
        ceActivity.finish();
        getInstrumentation().waitForIdleSync();

        SensorDataCollectorActivity sdcActivity =
                (SensorDataCollectorActivity) getInstrumentation().waitForMonitor(monitor);
        assertNotNull("SensorDataCollector Activity was not loaded", sdcActivity);
        getInstrumentation().removeMonitor(monitor);

        sdcActivity.finish();
    }

    public void testExperimentListClicked() {
        Context context = getInstrumentation().getTargetContext();
        context.setTheme(R.style.Theme_AppCompat);

        mSdcActivity = launchActivity(context.getPackageName(), SensorDataCollectorActivity.class,
                null);
        getInstrumentation().waitForIdleSync();

        mExperimentListFragment = mSdcActivity.getExperimentListFragment();
        assertNotNull("ExperimentListFragment is null", mExperimentListFragment);

        mExperimentList = mExperimentListFragment.getExperimentListAdapter();
        assertNotNull("ExperimentList is null", mExperimentList);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertNotNull("Experiment list is null", mExperimentList);
        assertNotSame("Experiment list is empty, create an experiment to run next tests", 0,
                mExperimentList.getCount());

        if (!mExperimentList.isEmpty()) {
            ActivityMonitor monitor = getInstrumentation().addMonitor(
                    ViewExperimentActivity.class.getName(), null, false);
            mSdcActivity.onExperimentClicked_ExperimentListFragment(mExperimentList.getItem(0));
            getInstrumentation().waitForIdleSync();

            ViewExperimentActivity veActivity =
                    (ViewExperimentActivity) getInstrumentation().waitForMonitor(monitor);
            assertNotNull("ViewExperimentActivity Activity was not loaded", veActivity);
            getInstrumentation().removeMonitor(monitor);

            Experiment mExperiment = ExperimentManagerSingleton.getInstance().getActiveExperiment();
            assertNotNull("The loaded experiment is null", mExperiment);
            assertEquals("The loaded experiment is not correct", mExperiment,
                    mExperimentList.getItem(0));

            monitor = getInstrumentation().addMonitor(SensorDataCollectorActivity.class.getName(),
                    null, false);
            veActivity.finish();
            getInstrumentation().waitForIdleSync();

            SensorDataCollectorActivity sdcActivity =
                    (SensorDataCollectorActivity) getInstrumentation().waitForMonitor(monitor);
            assertNotNull("SensorDataCollector Activity was not loaded", sdcActivity);
            getInstrumentation().removeMonitor(monitor);

            sdcActivity.finish();
        }
    }
}
