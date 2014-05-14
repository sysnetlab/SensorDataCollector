package sysnetlab.android.sdc.datacollector;

import java.util.ArrayList;
import java.util.List;

import sysnetlab.android.sdc.datastore.AbstractStore;

public class ExperimentManager {
	private List<AbstractStore> mStores;

	public ExperimentManager() {
		mStores = new ArrayList<AbstractStore>();
	}

	public void addExperimentStore(AbstractStore store) {
		mStores.add(store);
	}

	public List<Experiment> getExperiments() {
		List<Experiment> allExperiments = new ArrayList<Experiment>();
		for (AbstractStore store : mStores) {
			List<Experiment> experiments = store.listExperiments();
			allExperiments.addAll(experiments);
		}
		return allExperiments;
	}
}
