package sysnetlab.android.sdc.sensor;

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

	@Override
	public void setSensor(Object camera) {
		mCamera = (Camera)camera;
	}

	@Override
	public Object getSensor() {
		return mCamera;
	}
}
