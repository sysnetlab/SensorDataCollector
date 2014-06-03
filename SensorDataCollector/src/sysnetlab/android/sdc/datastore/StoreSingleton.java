
package sysnetlab.android.sdc.datastore;

public class StoreSingleton {
	
    private static AbstractStore instance = null;
    private static SimpleXMLFileStore simpleXmlFileStoreInstance = null; 
    private static SimpleFileStore simpleFileStoreInstance = null; 

    protected StoreSingleton() {
        // prevent from instantiating
    }

    public static AbstractStore getInstance() {
        if (instance == null) {
            instance = new SimpleXMLFileStore();
        }
        return instance;
    }
    
    public static void setStoreInstance(AbstractStore store) {
        instance = store; 
    }
    
    public static AbstractStore getSimpleFileStoreInstance() {
        if (simpleFileStoreInstance == null) {
            simpleFileStoreInstance = new SimpleFileStore();
        }
        return simpleFileStoreInstance;
        
    }
    public static AbstractStore getSimpleXmlFileStoreInstance() {
        if (simpleXmlFileStoreInstance == null) {
            simpleXmlFileStoreInstance = new SimpleXMLFileStore();
        }
        return simpleXmlFileStoreInstance;
    }
    
}
