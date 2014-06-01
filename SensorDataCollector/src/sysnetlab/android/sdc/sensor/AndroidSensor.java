
package sysnetlab.android.sdc.sensor;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

public class AndroidSensor extends AbstractSensor {
    private Sensor mSensor;
    private int mSamplingInterval; /* in microseconds */
    private int mSensingType;
    private boolean mIsStreamingSensor;

    public AndroidSensor() {
        mSensor = null;
        mSamplingInterval = -1;
        mSensingType = -1;
        mIsStreamingSensor = false;
    }
    
    @SuppressLint("NewApi")
    public AndroidSensor(Sensor sensor) {
        this.mSensor = sensor;
        super.setMajorType(ANDROID_SENSOR);
        super.setMinorType(this.mSensor.getType());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mSamplingInterval = mSensor.getMinDelay();
            if (mSamplingInterval != 0) {
                mSamplingInterval = SensorManager.SENSOR_DELAY_NORMAL;
                mIsStreamingSensor = true;
            } else {
                mIsStreamingSensor = false;
            }
        } else {
            mIsStreamingSensor = false; /*
                                         * old android does not have streaming
                                         * sensor
                                         */
        }
    }

    public String getName() {
        return mSensor.getName();
    }

    public Object getSensor() {
        return mSensor;
    }

    public String toString() {
        return mSensor.toString();
    }
 
    @Override
    public boolean equals(Object rhs) {
        if (rhs == this) {
            return true;
        }
        
        if (!(rhs instanceof AndroidSensor)) {
            return false;
        }
        
        AndroidSensor s = (AndroidSensor) rhs;
        
        if (!getName().equals(s.getName())) {
            return false;
        }
        
        if (getMajorType() != s.getMajorType()) {
            return false;
        }
        
        if (getMinorType() != s.getMinorType()) {
            return false;
        }
        
        return true;
    }

    @Override
    public void setSensor(Object sensor) {
        this.mSensor = (Sensor) sensor;
    }

    public int getSamplingInterval() {
        return mSamplingInterval;
    }

    public void setSamplingInterval(int samplingInterval) {
        this.mSamplingInterval = samplingInterval;
    }

    public int getSensingType() {
        return mSensingType;
    }

    public void setSensingType(int sensingType) {
        this.mSensingType = sensingType;
    }

    public boolean isStreamingSensor() {
        return mIsStreamingSensor;
    }

    @Override
    public boolean isSameSensor(AbstractSensor sensor) {
        return mSensor.getName().equals(sensor.getName());
    }

    @Override
    public String getVendor() {
        return mSensor.getVendor();
    }

    @Override
    public int getVersion() {
        return mSensor.getVersion();
    }
}
