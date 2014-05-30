package sysnetlab.android.sdc.test;

import java.util.ArrayList;
import java.util.Calendar;

import sysnetlab.android.sdc.datacollector.DeviceInformation;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.Tag;
import android.os.Parcel;
import android.test.AndroidTestCase;

public class ExperimentTests extends AndroidTestCase{
	
	private Experiment mExperiment;
	
	public ExperimentTests() {
		super();
		setUp();
	}
	
	protected void setUp() {
		mExperiment = new Experiment();
	}
	
	public void testDefaultConstructor(){
		assertNotSame("The name was not set by the default constructor",null,mExperiment.getName());
		assertNotSame("The date was not set by the default constructor",null,mExperiment.getDateTimeCreatedAsString());
		assertNotSame("The device info was not set by the default constructor",null,mExperiment.getDeviceInformation());
		
		assertNotSame("The name was set empty by the default constructor","",mExperiment.getName());
		assertNotSame("The date was set empty by the default constructor","",mExperiment.getDateTimeCreatedAsString());
		assertNotSame("The device info was set empty by the default constructor","",mExperiment.getDeviceInformation());
		
		mExperiment = new Experiment(null, null);
        assertEquals("The constructor failed to initialize the object with null arguments", "",
                mExperiment.getName());
        assertNotNull("The constructor failed to initialize the object with null arguments",
                mExperiment.getDateTimeCreatedAsString());
        assertNotNull("The constructor failed to initialize the object with null arguments",
                mExperiment.getDateTimeDoneAsString());
	}
	
	public void testCreatorInstantiation() {
		assertNotNull("The CREATOR was not generated",mExperiment.getCreator());
	}
	
	public void testAddTag() {
		mExperiment.addTag("", "");
		assertNotSame("An empty tag was added",1,mExperiment.getTags().size());
		
		mExperiment.setTags(new ArrayList<Tag>());
		mExperiment.addTag(null, null);
		assertNotSame("A null tag was added",1,mExperiment.getTags().size());
		
		mExperiment.setTags(new ArrayList<Tag>());
		mExperiment.addTag("Test1", "");
		assertNotSame("Failed to add a new tag",0,mExperiment.getTags().size());
		
		mExperiment.addTag("Test1", "");
		assertNotSame("The same tag was added twice",2,mExperiment.getTags().size());
	}
	
	public void testSetNotes(){
		mExperiment.setNotes(null);
		assertNotSame("A null note array was added",1,mExperiment.getNotes().size());
		
		mExperiment.setNotes(new ArrayList<Note>());
		assertNotNull("Failed to add the notes array",mExperiment.getNotes());
	}
	
	public void testClone(){
		mExperiment.addTag("New tag", "Description");
		mExperiment.addTag("Tag 2", "2nd Tag Description");
		mExperiment.setName("Test Experiment");
		ArrayList<Note> NotesList = new ArrayList<Note>();
		NotesList.add(new Note("Teste Note 1"));
		NotesList.add(new Note("Teste Note 2"));
		mExperiment.setNotes(NotesList);
		mExperiment.setDeviceInformation(new DeviceInformation());
		Experiment cloneExperiment = mExperiment.clone();
		assertEquals("Failed to clone the Experiment tags List", mExperiment.getTags(), cloneExperiment.getTags());		
		assertEquals("Failed to clone the Experiment notes List", mExperiment.getNotes(), cloneExperiment.getNotes());
		assertEquals("Failed to clone the Experiment name", mExperiment.getName(), cloneExperiment.getName());
		assertEquals("Failed to clone the Experiment name", mExperiment.getDeviceInformation(), cloneExperiment.getDeviceInformation());
	}
	
	public void testParcel() {
        Experiment after;
        mExperiment = new Experiment("test", Calendar.getInstance().getTime());
        Parcel parcel = Parcel.obtain();
        mExperiment.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        after = Experiment.CREATOR.createFromParcel(parcel);        
        mExperiment.equals(after);        
        assertEquals("The Experiment failed to be constructed out of a Parcel", mExperiment, after);
        parcel.recycle();
    }
}
