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

package sysnetlab.android.sdc.sensor.camera;

import sysnetlab.android.sdc.sensor.AbstractSensor;
import android.hardware.Camera;

public class CameraSensor extends AbstractSensor {
    private Camera mCamera;
    private String mName;
    private int mCameraId;

    public CameraSensor(Camera camera, int id) {
        mCamera = camera;
        mCameraId = id;
        super.setMajorType(CAMERA_SENSOR);
        super.setMinorType(CAMERA_SENSOR);
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public String toString() {
        return mName;
    }

    public int getCameraId() {
        return mCameraId;
    }

    public void setSensor(Object camera) {
        mCamera = (Camera) camera;
    }

    public Object getSensor() {
        return mCamera;
    }

    @Override
    public boolean isSameSensor(AbstractSensor sensor) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getVendor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean equals(Object rhs) {
        // TODO Auto-generated method stub
        return false;
    }
}
