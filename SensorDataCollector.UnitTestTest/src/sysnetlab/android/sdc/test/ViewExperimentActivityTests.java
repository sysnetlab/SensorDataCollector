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
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import android.content.Context;
import android.content.Intent;



public class ViewExperimentActivityTests extends android.test.ActivityUnitTestCase<ViewExperimentActivity> {

	private ViewExperimentActivity veActivity;
	private Experiment mExperiment;
	
	public ViewExperimentActivityTests() {
		super(ViewExperimentActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();		
		
		Context context = getInstrumentation().getTargetContext();
		context.setTheme(R.style.Theme_AppCompat);
	    Intent intent = new Intent(context, ViewExperimentActivity.class);
	    
	    mExperiment= new Experiment();
	    ExperimentManagerSingleton.getInstance().setActiveExperiment(mExperiment);
	    
        startActivity(intent, null, null);
        veActivity = getActivity();
        
        getInstrumentation().callActivityOnStart(veActivity);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testViewExperimentActivityLoaded()
	{
		assertNotNull("The ExperimentViewFragment was not loaded", veActivity.getExperimentViewFragment());
		assertNotNull("The activity was not loaded", veActivity.findViewById(R.id.fragment_container));		
	}			

}
