package sysnetlab.android.sdc.test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sysnetlab.android.sdc.datacollector.AndroidSensorEventListener;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.datastore.AbstractStore;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.datastore.SimpleFileStore;
import sysnetlab.android.sdc.datastore.SimpleXmlFileStore;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.sensor.SensorUtilsSingleton;
import sysnetlab.android.sdc.sensor.audio.AudioSensor;
import android.test.AndroidTestCase;

public class DataStoreTests extends AndroidTestCase {
	
	
    public void testAbstractStoreBehavior() {
    	AbstractStore store = StoreSingleton.getInstance();
    	Experiment exp = new Experiment("testExperiment");
    	store.setupNewExperimentStorage(exp);
    	store.writeExperimentMetaData(exp);
    	
    	SensorUtilsSingleton.getInstance().setContext(getContext());
    	
    	List<Experiment> storedExps = store.listStoredExperiments();
    	
    	boolean foundCreatedExperiment = false;
    	for (Experiment storedExp : storedExps)
    	{
    		if(storedExp.getName().equals("testExperiment"))
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
    	Channel channel = store.createChannel("testTag", Channel.WRITE_ONLY, Channel.CHANNEL_TYPE_CSV);
    	assertNotNull("Created null channel", channel);
    	channel.open();
    	channel.write("aaa");
    	channel.close();
    	store.closeAllChannels();    	
    }
    
    public void testSimpleFileStore()
    {
    	SimpleFileStore store = new SimpleFileStore();
    	
    	int expNumber = store.getNextExperimentNumber();
    	store.setupNewExperimentStorage(null);
    	assertTrue(expNumber == (store.getNextExperimentNumber() - 1));
    	assertNotNull(store.getNewExperimentPath());	
    	
    	int channelNumber = store.getNextChannelNumber();
    	store.createChannel("", Channel.WRITE_ONLY, Channel.CHANNEL_TYPE_CSV);
    	store.createChannel("", Channel.WRITE_ONLY, Channel.CHANNEL_TYPE_CSV);
    	assertTrue(channelNumber == (store.getNextChannelNumber() - 2));
    }
    
    public void testSimpleXMLFileStore() {
        SimpleXmlFileStore store = new SimpleXmlFileStore();
        
        int expNumber = store.getNextExperimentNumber();
        store.setupNewExperimentStorage(null);
        assertTrue(expNumber == (store.getNextExperimentNumber() - 1));
        assertNotNull(store.getNewExperimentPath());
        
        int channelNumber = store.getNextChannelNumber();
        store.createChannel("", Channel.WRITE_ONLY, Channel.CHANNEL_TYPE_CSV);
        store.createChannel("", Channel.WRITE_ONLY, Channel.CHANNEL_TYPE_CSV);
        assertTrue(channelNumber == (store.getNextChannelNumber() - 2));

        Experiment exp1 = new Experiment();
        
        List<Tag> listTags = new ArrayList<Tag>();
        int n = (int)(Math.random() * 5) + 1;
        for (int i = 0; i < n; i ++) {
            String name = "Tag_" + i;
            String shortDescription = "Short description for tag " + i;
            String longDescription = "Long description for tag " + i;
            Tag tag = new Tag(name, shortDescription, longDescription);
            listTags.add(tag);        
        }
        exp1.setTags(listTags);
        
        List<Note> listNotes = new ArrayList<Note>();
        n = (int)(Math.random() * 5) + 1;
        for (int i = 0; i < n; i ++) {
            String text = "Note_" + i;
            Date date = Calendar.getInstance().getTime();
            Note note = new Note(text, date);
            listNotes.add(note);
        }
        exp1.setNotes(listNotes);
        
        List<AbstractSensor> listSensors = SensorDiscoverer.discoverSensorList(getContext());

        String channelPath = "";
        AbstractStore.Channel channel = null;
        for (AbstractSensor sensor : listSensors) {
            switch (sensor.getMajorType()) {
                case AbstractSensor.ANDROID_SENSOR:
                    channelPath = store.getNewExperimentPath() + "/" + sensor.getName().replace(' ', '_') + ".txt";
                    channel = null;
                    try {
                        channel = store.new SimpleFileChannel(channelPath, AbstractStore.Channel.WRITE_ONLY);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    AndroidSensorEventListener listener =
                            new AndroidSensorEventListener(channel);
                    ((AndroidSensor) sensor).setListener(listener);  
                    break;
                case AbstractSensor.AUDIO_SENSOR:
                    channelPath = store.getNewExperimentPath() + "/" + sensor.getName().replace(' ', '_') + ".txt";
                    channel = null;
                    try {
                        channel = store.new SimpleFileChannel(channelPath, AbstractStore.Channel.WRITE_ONLY);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    ((AudioSensor) sensor).setChannel(channel);
                    break;
            }
        }
        
        exp1.setSensors(listSensors);
        
        store.writeExperimentMetaData(exp1);
        SensorUtilsSingleton.getInstance().setContext(getContext());
        Experiment exp2 = store.loadExperiment(store.getNewExperimentPath());
        assertEquals("experiment written and experiment read should be the same", exp1, exp2);
    }
}
