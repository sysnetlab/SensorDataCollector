package sysnetlab.android.sdc.datacollector;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import sysnetlab.android.sdc.datasink.ExperimentStore;
import sysnetlab.android.sdc.datasink.ExperimentStoreCompartment;

public class ExperimentManager {
	
	public List<Experiment> listExperiment(ExperimentStore store) {
		ExperimentStoreCompartment compartment;
		List<Experiment> listExp = new ArrayList<Experiment>();
		
		while((compartment = store.nextExperimentStoreCompartment()) != null) {
			try { 
				Experiment experiment = new Experiment(compartment);
				listExp.add(experiment);				
			} catch (Exception e) {
				Log.e("SensorDataCollector", "ExperimentManager failed to reconstruct experiment");
				e.printStackTrace();
			}
		}
		
		return listExp;
	}
}
