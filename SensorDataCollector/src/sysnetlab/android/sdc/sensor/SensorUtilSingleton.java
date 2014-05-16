package sysnetlab.android.sdc.sensor;

public class SensorUtilSingleton {
    private static SensorUtil instance = null;
    
    public static SensorUtil getInstance() {
        if (instance == null) {
            instance =  new SensorUtil();
        } 
        return instance;
    }
}
