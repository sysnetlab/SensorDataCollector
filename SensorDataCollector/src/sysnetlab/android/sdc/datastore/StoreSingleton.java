
package sysnetlab.android.sdc.datastore;

import com.dropbox.sync.android.DbxAccountManager;

public class StoreSingleton {
	
    private static AbstractStore instance = null;
    // TODO: Not sure if this is the best spot for this, but will do for now.
    private static DbxAccountManager mDbxAccountManager;
    
    
    protected StoreSingleton() {
        // prevent from instantiating
    }

    public static AbstractStore getInstance() {
        if (instance == null) {
        	if (mDbxAccountManager.hasLinkedAccount())
        		instance = new DbxSimpleFileStore();
        	else {
        		instance = new SimpleXmlFileStore();
        	}
        }
        return instance;
    }
    
    public static void setDbxAccountManager(DbxAccountManager m) {
    	mDbxAccountManager = m;
    }
    
    public static DbxAccountManager getDbxAccountManager() {
    	return mDbxAccountManager;
    }
    
}
