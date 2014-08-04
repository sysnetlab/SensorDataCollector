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

import junit.framework.Assert;
import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.SensorDataCollectorActivity;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import sysnetlab.android.sdc.ui.fragments.ExperimentEditTagsFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentRunFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentSetupFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation.ActivityMonitor;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class TestHelper {

    
    public static Activity launchSensorDataCollectorActivity(ActivityInstrumentationTestCase2<?> testCase) {
        Activity activity;
        
        testCase.setActivityInitialTouchMode(true);
        activity = testCase.getActivity();
        testCase.getInstrumentation().waitForIdleSync();

        Assert.assertTrue("The activity must be a SensorDataCollectorActivity.",
                activity instanceof SensorDataCollectorActivity);
        return activity;
    }
    
    public static Activity launchCreateExperimentActivity(
            ActivityInstrumentationTestCase2<?> testCase, Activity activity) {
        
        // click the Create-New-Experiment button
        ActivityMonitor activityMonitor = testCase.getInstrumentation().addMonitor(
                CreateExperimentActivity.class.getName(), null, false);

        Button buttonCreateExperiment = (Button) activity
                .findViewById(R.id.button_create_experiment);
        Assert.assertNotNull("The Create-Experiment Button should not be null",
                buttonCreateExperiment);

        TouchUtils.clickView(testCase, buttonCreateExperiment);

        testCase.getInstrumentation().waitForIdleSync();

        activity = testCase.getInstrumentation().waitForMonitor(activityMonitor);

        testCase.getInstrumentation().removeMonitor(activityMonitor);
        return activity;
    }
    
    public static void goBack(ActivityInstrumentationTestCase2<?> testCase) {
        testCase.sendKeys(KeyEvent.KEYCODE_BACK);
        testCase.getInstrumentation().waitForIdleSync();        
    }
    
    public static void enterTags(ActivityInstrumentationTestCase2<?> testCase, Activity activity, int maximumNumberOfTags) {
        Fragment fragment; 
        
        Assert.assertTrue("The activity must be a CreateExperimentActivity.",
                activity instanceof CreateExperimentActivity);   
        
        // choose to enter tags
        CreateExperimentActivity createExperimentActivity = (CreateExperimentActivity) activity;

        fragment = createExperimentActivity.getSupportFragmentManager().findFragmentById(
                R.id.fragment_container);
        Assert.assertNotNull("The ExperimentSetupFragment should not be null.", fragment);
        Assert.assertTrue("The fragment should be an ExperimentSetupFragment.",
                fragment instanceof ExperimentSetupFragment);

        ListView listViewOperations = (ListView) fragment.getView()
                .findViewById(R.id.lv_operations);
        Assert.assertNotNull("The operations ListView should not be null.", listViewOperations);
        Assert.assertEquals("The operations ListView should has 3 items", listViewOperations.getCount(), 3);

        TouchUtils.clickView(testCase, listViewOperations.getChildAt(0));

        testCase.getInstrumentation().waitForIdleSync();

        // enter a random number of tags without description
        fragment = createExperimentActivity.getSupportFragmentManager().findFragmentById(
                R.id.fragment_container);
        Assert.assertNotNull("The ExperimentEditTagsFragment should not be null.", fragment);
        Assert.assertTrue("The fragment should be an ExperimentEditTagsFragment.",
                fragment instanceof ExperimentEditTagsFragment);

        final EditText editTextTag = (EditText) fragment.getView().findViewById(R.id.edittext_tag);
        Assert.assertNotNull("EditText for Tag should not be null.", editTextTag);

        Button buttonAddTag = (Button) fragment.getView().findViewById(R.id.btn_add_tag);
        Assert.assertNotNull("The Add-Tag button should not be null.", buttonAddTag);

        int numberOfTags = (int) (Math.random() * maximumNumberOfTags + 1);
        for (int i = 0; i < numberOfTags; i++) {
            final String strTag = "Tag_" + (i + 1);
            createExperimentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    editTextTag.setText(strTag);
                }
            });
            testCase.getInstrumentation().waitForIdleSync();
            TouchUtils.clickView(testCase, buttonAddTag);
            testCase.getInstrumentation().waitForIdleSync();
        }

        ListView listViewTags = (ListView) fragment.getView().findViewById(R.id.listview_tags);
        Assert.assertNotNull("The ListView for entered tags should not be null.", listViewTags);
        Assert.assertEquals("The number of tags entered must be " + numberOfTags, listViewTags.getCount(),
                numberOfTags);        
    }    
    
    public static void startExperiment(ActivityInstrumentationTestCase2<?> testCase, Activity activity) {
        Assert.assertTrue("The activity must be a CreateExperimentActivity.",
                activity instanceof CreateExperimentActivity);
        
        Button buttonRunExperiment = (Button) activity.findViewById(R.id.button_experiment_run);
        Assert.assertNotNull("The Run-Experiment button should not be null.", buttonRunExperiment);
        
        TouchUtils.clickView(testCase, buttonRunExperiment);
        testCase.getInstrumentation().waitForIdleSync();
     }
    
    
    public static Activity stopExperiment(ActivityInstrumentationTestCase2<?> testCase, Activity activity) {
        Assert.assertTrue("The activity must be a CreateExperimentActivity.",
                activity instanceof CreateExperimentActivity);
        
        Fragment fragment = ((CreateExperimentActivity) activity).getSupportFragmentManager().findFragmentById(
                R.id.fragment_container);
        Assert.assertNotNull("The ExperimentRunFragment should not be null.", fragment);
        Assert.assertTrue("The fragment should be an ExperimentRunFragment.",
                fragment instanceof ExperimentRunFragment);
        
        Button buttonExperimentDone = (Button) fragment.getView().findViewById(R.id.button_experiment_done);
        Assert.assertNotNull("The Experiment-Done button shoud not be null.", buttonExperimentDone);
        
        TouchUtils.clickView(testCase,  buttonExperimentDone);
        testCase.getInstrumentation().waitForIdleSync();
        
        AlertDialog dialog = ((CreateExperimentActivity) activity).getAlertDialog();
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        ActivityMonitor monitor = testCase.getInstrumentation().addMonitor(SensorDataCollectorActivity.class.getName(), null, false);        

        TouchUtils.clickView(testCase, positiveButton);
        testCase.getInstrumentation().waitForIdleSync();  
        
        SensorDataCollectorActivity sensorDataCollectorActivity = 
                (SensorDataCollectorActivity) testCase.getInstrumentation().waitForMonitor(monitor);
        testCase.getInstrumentation().removeMonitor(monitor);
        Assert.assertNotNull("SensorDataCollector Activity was not loaded", sensorDataCollectorActivity);
        
        return sensorDataCollectorActivity;
    }    
    
    public static Activity viewMostRecentExperiment(ActivityInstrumentationTestCase2<?> testCase, Activity activity) {
        // view the most recent experiment
        Fragment fragment = ((SensorDataCollectorActivity) activity).getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Assert.assertNotNull("The Sensor List Fragment should not be null.", fragment);
        
        ListView listView = ((ListFragment) fragment).getListView();
        Assert.assertNotNull("ListView should not be null.", listView);
        
        ActivityMonitor monitor = testCase.getInstrumentation().addMonitor(ViewExperimentActivity.class.getName(), null, false);
        TouchUtils.clickView(testCase, listView.getChildAt( listView.getHeaderViewsCount()));
        testCase.getInstrumentation().waitForIdleSync();
        
        ViewExperimentActivity viewExperimentActivity = (ViewExperimentActivity) testCase.getInstrumentation().waitForMonitor(monitor);
        Assert.assertTrue("activity should be a ViewExperimentActivity.", viewExperimentActivity instanceof ViewExperimentActivity);
        testCase.getInstrumentation().removeMonitor(monitor);
      
        // clone the experiment
        testCase.getInstrumentation().waitForIdleSync();     
        return viewExperimentActivity;
    }
    
    public static Activity cloneExperiment(ActivityInstrumentationTestCase2<?> testCase,
            Activity activity) {
        Assert.assertTrue("The acitivty must be a ViewExperimentActivity.", activity instanceof ViewExperimentActivity);
        
        Button buttonCloneExperiment = (Button) activity
                .findViewById(R.id.button_experiment_view_clone);
        Assert.assertNotNull("Button should not be null.", buttonCloneExperiment);

        ActivityMonitor monitor = testCase.getInstrumentation().addMonitor(
                CreateExperimentActivity.class.getName(), null, false);
        TouchUtils.clickView(testCase, buttonCloneExperiment);
        activity = testCase.getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        Assert.assertTrue("activity should be CreateExperimentActivity.",
                activity instanceof CreateExperimentActivity);
        testCase.getInstrumentation().removeMonitor(monitor);

        return activity;
    }
}
