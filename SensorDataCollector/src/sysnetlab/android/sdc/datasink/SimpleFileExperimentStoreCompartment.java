package sysnetlab.android.sdc.datasink;

import java.io.File;

import android.util.Log;

public class SimpleFileExperimentStoreCompartment extends ExperimentStoreCompartment {
	private File mExperimentDir;
	
	SimpleFileExperimentStoreCompartment(String dirPath)
			throws 
				ExperimentStoreCompartmentNotFoundException,
				IllegalArgumentException {
		createSimpleFileExperimentStoreCompartment(dirPath, SCF_CREAT);
	}
	
	SimpleFileExperimentStoreCompartment(String dirPath, int flags) 
			throws 
				ExperimentStoreCompartmentNotFoundException,
				IllegalArgumentException {
		createSimpleFileExperimentStoreCompartment(dirPath, flags);
	}
	
	private void createSimpleFileExperimentStoreCompartment(String dirPath, int flags) 
			throws 
				ExperimentStoreCompartmentNotFoundException,
				IllegalArgumentException {
		mExperimentDir = new File(dirPath);
		if ((flags & SCF_EXIST) == SCF_EXIST) {
			if (!mExperimentDir.exists())
				throw new ExperimentStoreCompartmentNotFoundException();
		} else if ((flags & SCF_CREAT) == SCF_CREAT) {
			if (!mExperimentDir.exists())
				mExperimentDir.mkdir();
		} else {
			Log.e("SensorDataCollector", "Unexpected creation flag");
			throw new IllegalArgumentException();
		}
	}
}
