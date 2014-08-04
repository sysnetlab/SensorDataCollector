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

package sysnetlab.android.sdc.sensor;

import sysnetlab.android.sdc.datacollector.AndroidSensorEventListener;
import sysnetlab.android.sdc.datastore.AbstractStore;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

public class AndroidSensor extends AbstractSensor {
    private Sensor mSensor;
    private int mSamplingInterval; /* in microseconds */
    private int mSensingType;
    private boolean mIsStreamingSensor;
    
    private AndroidSensorEventListener mListener;

    public AndroidSensor() {
        mSensor = null;
        mSamplingInterval = 0;
        mSensingType = -1;
        mIsStreamingSensor = false;
    }
    
    @SuppressLint("NewApi")
    public AndroidSensor(Sensor sensor) {
        mSamplingInterval = 0;
        mSensingType = -1;
        mSensor = sensor;
        super.setMajorType(ANDROID_SENSOR);
        super.setMinorType(mSensor.getType());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mSamplingInterval = mSensor.getMinDelay();
            if (mSamplingInterval > 0) {
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

    // TODO carefully check how Sensor objects should be reconstructed from 
    // persistent storage and copied from one instance to another.  
    @Override
    public int getVersion() {
        return mSensor.getVersion();
    }
    
    
    public void setListener(AndroidSensorEventListener listener) {
        mListener = listener;
    }

    public AndroidSensorEventListener getListener() {
        return mListener;
    }    
    
    public AbstractStore.Channel getChannel() {
        if (mListener != null) {
            return mListener.getChannel();
        } else {
            return null;
        }
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        
        // Log.d("SensorDataCollector", "AndroidSensor::equals(): checkpoint #1.");
        
        // it also takes care of the case that object is null
        if (!(object instanceof AndroidSensor)) return false;
        
        if (!super.equals(object)) {
            return false;
        }
        
        // Log.d("SensorDataCollector", "AndroidSensor::equals(): checkpoint #2.");
        
        switch (getMajorType()) {
            case AbstractSensor.ANDROID_SENSOR:
                AndroidSensor androidSensor = (AndroidSensor) object;
                
                if (this.mSamplingInterval != androidSensor.mSamplingInterval)
                    return false;

                // Log.d("SensorDataCollector", "AndroidSensor::equals(): checkpoint #3.");

                if (this.mSensingType != androidSensor.mSensingType)
                    return false;

                // Log.d("SensorDataCollector", "AndroidSensor::equals(): checkpoint #4.");

                if (this.mIsStreamingSensor != androidSensor.mIsStreamingSensor)
                    return false;

                // Log.d("SensorDataCollector", "AndroidSensor::equals(): checkpoint #5.");

                if (!this.mSensor.equals(androidSensor.mSensor))
                    return false;

                // Log.d("SensorDataCollector", "AndroidSensor::equals(): checkpoint #6.");
                break;              
        }

        return true;
    }
}
