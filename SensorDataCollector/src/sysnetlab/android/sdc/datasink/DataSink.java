package sysnetlab.android.sdc.datasink;

import java.io.PrintStream;
import java.util.List;

import sysnetlab.android.sdc.datacollector.Experiment;

public interface DataSink {
	public void createExperiment();
	public List<Experiment> listExperiments();
	public PrintStream open(String tag);
	public void close();	
}