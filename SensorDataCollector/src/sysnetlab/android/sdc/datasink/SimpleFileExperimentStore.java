package sysnetlab.android.sdc.datasink;

import java.io.File;
import java.text.DecimalFormat;

import android.os.Environment;
import android.util.Log;

public class SimpleFileExperimentStore extends ExperimentStore {
	private final String DIR_PREFIX = "exp";
	private String mParentPath;
	private int mNextExperimentNumber;
	private int mCompartmentCursor;

	public SimpleFileExperimentStore() {
		mType = ExperimentStoreType.SIMPLE_FILE_EXPERIMENT_STORE;
		mCompartmentCursor = 0;

		Log.i("SensorDataCollector",
				"entering SimpleFileStore.constructor() ...");
		String parentPath = Environment.getExternalStorageDirectory().getPath();
		parentPath = parentPath + "/SensorData";
		File dataDir = new File(parentPath);
		if (!dataDir.exists()) {
			dataDir.mkdir();
		}
		mParentPath = parentPath;

		String pathPrefix = mParentPath + "/" + DIR_PREFIX;
		DecimalFormat f = new DecimalFormat("00000");
		String path;

		mNextExperimentNumber = 0;

		do {
			mNextExperimentNumber++;
			path = pathPrefix + f.format(mNextExperimentNumber);
			dataDir = new File(path);
		} while (dataDir.exists());
	}

	@Override
	public ExperimentStoreCompartment addExperimentStoreCompartment() {
		DecimalFormat f = new DecimalFormat("00000");
		String path = mParentPath + "/" + DIR_PREFIX
				+ f.format(mNextExperimentNumber);
		try {
			ExperimentStoreCompartment compartment = new SimpleFileExperimentStoreCompartment(
					path);
			return compartment;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void setType(ExperimentStoreType type) {
		mType = ExperimentStoreType.SIMPLE_FILE_EXPERIMENT_STORE;
	}

	@Override
	public ExperimentStoreCompartment nextExperimentStoreCompartment() {
		SimpleFileExperimentStoreCompartment compartment;
		mCompartmentCursor++;
		DecimalFormat f = new DecimalFormat("00000");
		String path = mParentPath + "/" + DIR_PREFIX + f.format(mCompartmentCursor);
		try {
			compartment = new SimpleFileExperimentStoreCompartment(path,
					ExperimentStoreCompartment.SCF_EXIST);
			return compartment;
		} catch (Exception e) {
			return null;
		}
	}
}
