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

import sysnetlab.android.sdc.datacollector.DeviceInformation;
import android.test.AndroidTestCase;

public class DeviceInformationTests extends AndroidTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testDeviceInformation() {
    	DeviceInformation deviceInfo = new DeviceInformation();
    	
    	// model has maker, one space in between
    	deviceInfo.setDeviceInformation("SysNetLabMaker", "SysNetLabMaker T151");
    	assertEquals("SysNetLabMaker", deviceInfo.getManufacturer());   	
    	assertEquals("T151", deviceInfo.getModel());   	
    	assertEquals("SysNetLabMaker T151", deviceInfo.toString());
    	
    	// model has maker, multiple spaces between maker and model
    	deviceInfo.setDeviceInformation("SysNetLabMaker", "SysNetLabMaker     T151");
    	assertEquals("SysNetLabMaker", deviceInfo.getManufacturer());   	
    	assertEquals("T151", deviceInfo.getModel());   	
    	assertEquals("SysNetLabMaker T151", deviceInfo.toString());
    	
    	// model has maker, but model starts with lower case letter
    	deviceInfo.setDeviceInformation("SysNetLabMaker", "SysNetLabMaker     tt151");
    	assertEquals("SysNetLabMaker", deviceInfo.getManufacturer());   	
    	assertEquals("Tt151", deviceInfo.getModel());   	
    	assertEquals("SysNetLabMaker Tt151", deviceInfo.toString());
    	
    	// model does not have maker
    	deviceInfo.setDeviceInformation("SysNetLabMaker", "T151");
    	assertEquals("SysNetLabMaker", deviceInfo.getManufacturer());   	
    	assertEquals("T151", deviceInfo.getModel());   	
    	assertEquals("SysNetLabMaker T151", deviceInfo.toString());
    }
    
}
