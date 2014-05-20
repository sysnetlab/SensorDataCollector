package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.datacollector.DeviceInformation;
import android.test.AndroidTestCase;

public class AndroidUtilClassTests extends AndroidTestCase {

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
