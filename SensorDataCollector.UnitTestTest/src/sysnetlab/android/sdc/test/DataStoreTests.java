package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.datastore.AbstractStore;
import sysnetlab.android.sdc.datastore.SimpleFileStore;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import android.test.AndroidTestCase;

public class DataStoreTests extends AndroidTestCase {
	
	
    public void testSimpleFileStore() {
    	AbstractStore store = StoreSingleton.getInstance();
    	store.setupExperiment();
    	assertNotNull(((SimpleFileStore)store).getNewExperimentPath());	
    }
    
}
