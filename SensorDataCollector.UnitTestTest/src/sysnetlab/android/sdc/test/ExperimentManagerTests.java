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
        
        manager.addExperimentStore(StoreSingleton.getSimpleXmlFileStoreInstance());
        assertTrue("ExperimentManager should now have 2 stores.", manager.getStores().size() == 2);
    }

}
