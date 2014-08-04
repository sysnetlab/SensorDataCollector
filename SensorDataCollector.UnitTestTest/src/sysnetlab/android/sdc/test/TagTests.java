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
