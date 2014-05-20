package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.datastore.AbstractStore;
import sysnetlab.android.sdc.datastore.SimpleFileStore;
import sysnetlab.android.sdc.datastore.SimpleFileStoreSingleton;
import android.test.AndroidTestCase;

public class DataStoreTests extends AndroidTestCase {
	
	
    public void testSimpleFileStore() {
    	AbstractStore store = SimpleFileStoreSingleton.getInstance();
    	store.addExperiment();
    	assertNotNull(((SimpleFileStore)store).getNewExperimentPath());	
    }
    
}
