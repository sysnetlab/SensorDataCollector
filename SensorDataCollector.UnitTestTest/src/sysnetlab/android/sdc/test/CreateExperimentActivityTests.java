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
import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import android.content.Context;
import android.view.KeyEvent;

public class CreateExperimentActivityTests
        extends android.test.ActivityUnitTestCase<CreateExperimentActivity> {

    private CreateExperimentActivity mCreateExperimentActivity;

    public CreateExperimentActivityTests() {
        super(CreateExperimentActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        /*
         * // // This somehow leads to an error similar to //
         * ActionBarActivityDelegate getUiOptionsFromMetadata: Activity not in
         * manifest // To work around, use launchActivity instead // Context
         * context = getInstrumentation().getTargetContext();
         * context.setTheme(R.style.Theme_AppCompat); Intent intent = new
         * Intent(context, CreateExperimentActivity.class);
         * startActivity(intent, null, null); mCreateExperimentActivity =
         * getActivity();
         */
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testLayout() {

        Context context = getInstrumentation().getTargetContext();
        context.setTheme(R.style.Theme_AppCompat);
        mCreateExperimentActivity = launchActivity(context.getPackageName(),
                CreateExperimentActivity.class, null);
        getInstrumentation().waitForIdleSync();

        assertNotNull("The activity cannot be null.", mCreateExperimentActivity);
        assertNotNull("Fragment container must exist.",
                mCreateExperimentActivity.findViewById(R.id.fragment_container));
        assertNotNull("ProgressBar Layout must exist.",
                mCreateExperimentActivity.findViewById(R.id.progressbar_task_in_progress));

        sendKeys(KeyEvent.KEYCODE_BACK);
        mCreateExperimentActivity.finish();
    }

    public void testSensorDataCollectionState() {
        Context context = getInstrumentation().getTargetContext();
        context.setTheme(R.style.Theme_AppCompat);
        mCreateExperimentActivity = launchActivity(context.getPackageName(),
                CreateExperimentActivity.class, null);
        getInstrumentation().waitForIdleSync();

        assertNotNull("The activity cannot be null.", mCreateExperimentActivity);
        assertEquals("The initial collection state should be DATA_COLLECTION_STOPPED",
                mCreateExperimentActivity.getCurrentCollectionState(),
                DataCollectionState.DATA_COLLECTION_STOPPED);
        sendKeys(KeyEvent.KEYCODE_BACK);
        mCreateExperimentActivity.finish();
    }
}
