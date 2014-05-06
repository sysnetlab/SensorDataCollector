package sysnetlab.android.sdc.datacollector;

public class Experiment {
	private String name;
	private String dateCreated;
	
	public Experiment(String n, String dC)
	{
		name = n;
		dateCreated = dC;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDateCreated()
	{
		return dateCreated;
	}
	
}
