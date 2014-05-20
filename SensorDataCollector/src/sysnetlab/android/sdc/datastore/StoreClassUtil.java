
package sysnetlab.android.sdc.datastore;

public class StoreClassUtil {

    public static AbstractStore getStoreInstanceFromClassName(String className) {
        if (SimpleFileStore.class.getName().equals(className)) {
            return SimpleFileStoreSingleton.getInstance();
        } else {
            throw new RuntimeException("StoreClassUtil: Unexpected classname " + className);
        }
    }
}
