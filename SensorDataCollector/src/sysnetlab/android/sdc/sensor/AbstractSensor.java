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

public abstract class AbstractSensor {
    private int mSensorId;
    private int mMajorType;
    private int mMinorType;
    private boolean mSelected;

    public final static int ANDROID_SENSOR = 1;
    public final static int CAMERA_SENSOR = 2;
    public final static int AUDIO_SENSOR = 3;
    public final static int WIFI_SENSOR = 4; /* RSSI */
    public final static int BLUETOOTH_SENSOR = 5; /* RSSI */
    
    public int getId() {
        return mSensorId;
    }
    
    public void setId(int id) {
        mSensorId = id;
    }

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

    public abstract boolean isSameSensor(AbstractSensor sensor);

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public abstract String toString();
    
    public boolean equals(Object object) {
        // Log.d("SensorDataCollector", "AbstractSensor::equals(): checkpoint #1");
        if (this == object) return true;
        
        // it also takes care of the case that object is null
        if (!(object instanceof AbstractSensor)) return false;
        
        AbstractSensor rhs = (AbstractSensor) object; 
        if (mMajorType != rhs.mMajorType) return false;
        if (mMinorType != rhs.mMinorType) return false; 
        if (mSelected != rhs.mSelected) return false; 
        
        return true;
    }
}
