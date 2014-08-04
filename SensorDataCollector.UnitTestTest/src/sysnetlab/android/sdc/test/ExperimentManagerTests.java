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

import sysnetlab.android.sdc.datacollector.ExperimentManager;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import android.test.AndroidTestCase;

public class ExperimentManagerTests extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testExperimentManager() {
        ExperimentManager manager = ExperimentManagerSingleton.getInstance();
        
        assertNotNull("ExperimentManagerSingleton does not return null", manager);
        
        manager.addExperimentStore(StoreSingleton.getInstance());
        assertTrue("ExperimentManager should now have 1 store.", manager.getStores().size() == 1);
    }

}
