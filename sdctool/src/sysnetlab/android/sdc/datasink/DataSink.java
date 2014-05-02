package sysnetlab.android.sdc.datasink;

import java.io.PrintStream;

public abstract class DataSink {
	public abstract PrintStream open(String tag);
	public abstract void createExperiment();
	public abstract void close();	
}
