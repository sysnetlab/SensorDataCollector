package sysnetlab.android.sdc.datasink;

public class DataSinkSingleton {

	private static DataSink instance = null;
	private static DataSink compartmentSinkInstance = null;

	protected DataSinkSingleton() {
		// Exists only to defeat instantiation
	}

	public static DataSink getInstance() {
		if (instance == null) {
			instance = new SimpleFileSink();
		}
		return instance;
	}

	public static DataSink getCompartmentInstance(
			ExperimentStoreCompartment comparment) {
		if (compartmentSinkInstance == null) {
			compartmentSinkInstance = new SimpleFileSink(
					(SimpleFileExperimentStoreCompartment) comparment);
		}
		return compartmentSinkInstance;
	}

}
