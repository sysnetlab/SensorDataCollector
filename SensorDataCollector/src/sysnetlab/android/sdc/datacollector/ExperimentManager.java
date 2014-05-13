package sysnetlab.android.sdc.datacollector;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import sysnetlab.android.sdc.datasink.ExperimentStore;
import sysnetlab.android.sdc.datasink.ExperimentStoreCompartment;

public class ExperimentManager {
	private List<ExperimentStore> mStores;

	public ExperimentManager() {
		mStores = new ArrayList<ExperimentStore>();
	}

	public void addExperimentStore(ExperimentStore store) {
		mStores.add(store);
	}

	public List<Experiment> listExperiments() {
		List<Experiment> listExp = new ArrayList<Experiment>();
		
		for (ExperimentStore store : mStores) {
			listExperiments(store, listExp);
		}
		
		return listExp;
	}

	private void listExperiments(ExperimentStore store, List<Experiment> listExp) {
		ExperimentStoreCompartment compartment;

		while ((compartment = store.nextExperimentStoreCompartment()) != null) {
			try {
				Experiment experiment = new Experiment(compartment);
				listExp.add(experiment);
			} catch (Exception e) {
				Log.e("SensorDataCollector",
						"ExperimentManager failed to reconstruct experiment");
				e.printStackTrace();
			}
		}
	}
}
