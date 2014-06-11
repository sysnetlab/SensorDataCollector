
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
