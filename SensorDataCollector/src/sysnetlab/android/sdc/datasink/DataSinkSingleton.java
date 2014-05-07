package sysnetlab.android.sdc.datasink;

public class DataSinkSingleton {

	private static DataSink instance = null;
	
	protected DataSinkSingleton()
	{
		//Exists only to defeat instantiation
	}
	
	public static DataSink getInstance()
	{
		if(instance == null)
		{
			instance = new SimpleFileSink();
		}
		return instance;
	}
	
}
