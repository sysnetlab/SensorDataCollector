package sysnetlab.android.sdc.appdata;

import sysnetlab.android.sdc.datacollector.Experiment;

public class AppDataSingleton {
	private static AppDataSingleton instance = null;
	private Experiment experiment;
	
	protected AppDataSingleton()
	{
	}

	public static AppDataSingleton getInstance()
	{
		if(instance == null)
		{
			instance = new AppDataSingleton();
		}
		return instance;
	}
	
	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}	
}
