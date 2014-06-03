package sysnetlab.android.sdc.sensor;

import android.content.Context;

public class SensorUtilsSingleton {
    private static SensorUtils instance = null;
    
    public static SensorUtils getInstance() {
        if (instance == null) {
            instance =  new SensorUtils();
        } 
        return instance;
    }
    
    public static SensorUtils getInstance(Context context) {
        if (instance == null) {
            instance =  new SensorUtils();
            instance.setContext(context);
        } 
        return instance;
    }    
}
