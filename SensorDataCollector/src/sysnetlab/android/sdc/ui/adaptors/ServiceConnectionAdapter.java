package sysnetlab.android.sdc.ui.adaptors;

import sysnetlab.android.sdc.services.RunExperimentService.RunExperimentBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceConnectionAdapter implements ServiceConnection {
	
	private int mCheckedSensors;
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		RunExperimentBinder binder=(RunExperimentBinder) service;
		mCheckedSensors=binder.getCheckedSensors();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		
	}
	
	public int getCheckedSensors() {
		return mCheckedSensors;
	}
}
