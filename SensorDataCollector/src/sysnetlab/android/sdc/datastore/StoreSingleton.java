
package sysnetlab.android.sdc.datastore;

public class StoreSingleton {
	
    private static AbstractStore instance = null;

    protected StoreSingleton() {
        // prevent from instantiating
    }

    public static AbstractStore getInstance() {
        if (instance == null) {
            instance = new SimpleXmlFileStore();
        }
        return instance;
    }
}
