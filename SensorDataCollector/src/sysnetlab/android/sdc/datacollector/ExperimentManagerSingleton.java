
package sysnetlab.android.sdc.datacollector;

public class ExperimentManagerSingleton {
    private static ExperimentManager instance = null;

    protected ExperimentManagerSingleton() {
        // Exists only to defeat instantiation
    }

    public static ExperimentManager getInstance() {
        if (instance == null) {
            instance = new ExperimentManager();
        }
        return instance;
    }

}
