/* $Id$ */
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
			mIsStreamingSensor = false; /* old android does not have streaming sensor */
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
	public void setSensor(Object sensor) {
		this.mSensor = (Sensor)sensor;
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
} 
