package sysnetlab.android.sdc.test;
import android.test.AndroidTestCase;
import sysnetlab.android.sdc.datacollector.Tag;

public class TagTests extends AndroidTestCase {

	private Tag mTag, mTag2, mTag3;
	
	protected void setUp() throws Exception {
        super.setUp();
        mTag = new Tag("Tag name");
    	mTag2 = new Tag("Second tag", "Short Description");
    	mTag3 = new Tag("Third tag", "Short Description", "Long Description");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testTagName(){
    	assertEquals("Tag name", mTag.getName());
    	mTag.setName("First tag");
    	assertEquals("First tag", mTag.getName());    	
    	mTag.setName("Tag name");
    	assertEquals("Second tag", mTag2.getName());
    	assertEquals("Third tag", mTag3.getName());
    }
    
    public void testTagDescription(){
    	assertEquals("", mTag.getLongDescription());
    	assertEquals("", mTag.getShortDescription());    	    
    	mTag.setLongDescription("mtag Long Description");
    	mTag.setShortDescription("mtag Short Description");
    	assertEquals("mtag Long Description", mTag.getLongDescription());
    	assertEquals("mtag Short Description", mTag.getShortDescription());
    	assertEquals("Short Description", mTag2.getShortDescription());
    	assertEquals("Long Description", mTag3.getLongDescription());
    	mTag = new Tag("Tag name");
    }
    
    public void testCreatorInstantiation() {
		assertNotNull("The CREATOR was not generated",mTag.getCreator());
	}
    
	public void testEquals(){
		assertFalse(mTag.equals(mTag2));
		assertTrue(mTag.equals(mTag));
		mTag3.setName("First tag");
		assertTrue(mTag3.equals(mTag3));
		mTag3.setName("Third tag");
	}
}
