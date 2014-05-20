package sysnetlab.android.sdc.test;

import java.util.ArrayList;

import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.Tag;
import android.test.AndroidTestCase;
import sysnetlab.android.sdc.datacollector.Experiment;

public class ExperimentTests extends AndroidTestCase{
	
	private Experiment mExperiment;
	
	public ExperimentTests() {
		super();
		setUp();
	}
	
	protected void setUp() {
		mExperiment=new Experiment();
	}
	
	public void testDefaultConstructor(){
		assertNotSame("The name was not set by the default constructor",null,mExperiment.getName());
		assertNotSame("The date was not set by the default constructor",null,mExperiment.getDateTimeCreated());
		assertNotSame("The device info was not set by the default constructor",null,mExperiment.getDeviceInformation());
		
		assertNotSame("The name was set empty by the default constructor","",mExperiment.getName());
		assertNotSame("The date was set empty by the default constructor","",mExperiment.getDateTimeCreated());
		assertNotSame("The device info was set empty by the default constructor","",mExperiment.getDeviceInformation());
	}
	
	public void testCreatorInstantiation() {
		assertNotNull("The CREATOR was not generated",mExperiment.getCreator());
	}
	
	public void testAddTag() {
		mExperiment.addTag("");
		assertNotSame("An empty tag was added",1,mExperiment.getTags().size());
		
		mExperiment.setTags(new ArrayList<Tag>());
		mExperiment.addTag(null);
		assertNotSame("A null tag was added",1,mExperiment.getTags().size());
		
		mExperiment.setTags(new ArrayList<Tag>());
		mExperiment.addTag("Test1");
		assertNotSame("Failed to add a new tag",0,mExperiment.getTags().size());
		
		mExperiment.addTag("Test1");
		assertNotSame("The same tag was added twice",2,mExperiment.getTags().size());
	}
	
	public void testSetNotes(){
		mExperiment.setNotes(null);
		assertNotSame("A null note array was added",1,mExperiment.getNotes().size());
		
		mExperiment.setNotes(new ArrayList<Note>());
		assertNotNull("Failed to add the notes array",mExperiment.getNotes());
	}
}
