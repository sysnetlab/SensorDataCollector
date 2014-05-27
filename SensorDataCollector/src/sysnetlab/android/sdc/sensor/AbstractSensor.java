
package sysnetlab.android.sdc.sensor;

import sysnetlab.android.sdc.datacollector.AndroidSensorEventListener;

public abstract class AbstractSensor {
    private int mMajorType;
    private int mMinorType;
    private boolean mSelected;
    // add multiple listeners later. which one to use depending
    // on sensor types (major & minor types)
    private AndroidSensorEventListener mListener;

    public final static int ANDROID_SENSOR = 1;
    public final static int CAMERA_SENSOR = 2;
    public final static int AUDIO_SENSOR = 3;
    public final static int WIFI_SENSOR = 4; /* RSSI */
    public final static int BLUETOOTH_SENSOR = 5; /* RSSI */

    public int getMajorType() {
        return mMajorType;
    }

    protected void setMajorType(int major) {
        this.mMajorType = major;
    }

    public int getMinorType() {
        return mMinorType;
    }

    protected void setMinorType(int minor) {
        this.mMinorType = minor;
    }

    public abstract String getName();
    
    public abstract String getVendor();
    
    public abstract int getVersion();

    public abstract Object getSensor();
    
    public abstract boolean isSameSensor(AbstractSensor sensor);

    public abstract void setSensor(Object sensor);

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public abstract String toString();

    public void setListener(AndroidSensorEventListener listener) {
        mListener = listener;
    }

    public AndroidSensorEventListener getListener() {
        return mListener;
    }
}
