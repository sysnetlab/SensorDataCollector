package sysnetlab.android.sdc.datasink;

public abstract class ExperimentStore {
	protected ExperimentStoreType mType;

	public ExperimentStoreType getType() {
		return mType;
	}
	
	public abstract void setType(ExperimentStoreType type);
	
	public abstract ExperimentStoreCompartment addExperimentStoreCompartment();
	public abstract ExperimentStoreCompartment nextExperimentStoreCompartment();
}
