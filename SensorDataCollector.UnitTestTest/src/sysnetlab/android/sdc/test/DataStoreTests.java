package sysnetlab.android.sdc.test;

import java.util.List;

import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datastore.AbstractStore;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.datastore.SimpleFileStore;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import android.test.AndroidTestCase;

public class DataStoreTests extends AndroidTestCase {
	
	
    public void testAbstractStoreBehavior() {
    	AbstractStore store = StoreSingleton.getInstance();
    	Experiment exp = new Experiment("testExperiment","01/01/2011");
    	store.setupNewExperimentStorage(exp);
    	store.writeExperimentMetaData(exp);
    	List<Experiment> storedExps = store.listStoredExperiments();
    	
    	boolean foundCreatedExperiment = false;
    	for (Experiment storedExp : storedExps)
    	{
    		if(storedExp.getName().equals("testExperiment") && 
    		   storedExp.getDateTimeCreated().equals("01/01/2011"))
    		{
    			foundCreatedExperiment = true;
    		}
    	}
    	assertTrue("Could not recover stored experiment", foundCreatedExperiment);
    }
    
    public void testAbstractChannelBehavior()
    {
    	AbstractStore store = StoreSingleton.getInstance();
    	store.setupNewExperimentStorage(null);
    	Channel channel = store.createChannel("testTag");
    	assertNotNull("Created null channel", channel);
    	channel.open();
    	channel.write("aaa");
    	channel.close();
    	store.closeAllChannels();    	
    }
    
    public void testSimpleFileStore()
    {
    	SimpleFileStore store = new SimpleFileStore();
    	store.setupNewExperimentStorage(null);
    	assertNotNull(store.getNewExperimentPath());	
    }
}
