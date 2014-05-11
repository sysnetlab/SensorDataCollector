package sysnetlab.android.sdc.datasink;

import java.io.PrintStream;
import java.util.List;

import sysnetlab.android.sdc.datacollector.Experiment;

public interface DataSink {
	/*
	 *  It is expected that a data sink provides a light-weight buffering
	 *  mechanism. 
	 */
	public Experiment open();
	public List<Experiment> listExperiments();
	public PrintStream open(String tag);
	public void close();	
}