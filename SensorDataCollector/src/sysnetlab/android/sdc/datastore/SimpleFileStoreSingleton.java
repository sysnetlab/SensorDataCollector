package sysnetlab.android.sdc.datastore;

public class SimpleFileStoreSingleton {
	private static SimpleFileStore instance;
	
	protected SimpleFileStoreSingleton() {
		// prevent from instantiating
	}
	
	public static SimpleFileStore getInstance() {
		if (instance == null )
			instance =  new SimpleFileStore();
		return instance;
	}
}
