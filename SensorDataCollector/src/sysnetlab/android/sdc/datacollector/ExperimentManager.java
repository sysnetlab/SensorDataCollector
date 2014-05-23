
package sysnetlab.android.sdc.datacollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sysnetlab.android.sdc.datastore.AbstractStore;

public class ExperimentManager {
    private List<AbstractStore> mStores;    
    private Experiment mActiveExperiment;

    public ExperimentManager() {
        mStores = new ArrayList<AbstractStore>();
    }

    public void addExperimentStore(AbstractStore store) {
    	if(!mStores.contains(store))
    		mStores.add(store);
    }
    
    public List<Experiment> getExperiments() {
        List<Experiment> allExperiments = new ArrayList<Experiment>();
        for (AbstractStore store : mStores) {
            List<Experiment> experiments = store.listStoredExperiments();
            allExperiments.addAll(experiments);
        }
        return allExperiments;
    }       
    
    public List<Experiment> getExperimentsSortedByDate(){
    	List<Experiment> allExperiments = getExperiments();    	
	    	Collections.sort(allExperiments, new Comparator<Experiment>(){    		
	    		public int compare(Experiment e1, Experiment e2){    			
	    			return -1*e1.getDateTimeCreated().compareTo(e2.getDateTimeCreated());
	    		}
	    	});    	
    		
    	return allExperiments;
    }
    
    public void setActiveExperiment(Experiment experiment) {
        mActiveExperiment = experiment; 
    }
    
    public Experiment getActiveExperiment() {
        return mActiveExperiment;
    }
}
