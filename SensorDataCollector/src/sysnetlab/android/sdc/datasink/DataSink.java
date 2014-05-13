package sysnetlab.android.sdc.datasink;

public interface DataSink {
	/*
	 *  It is expected that a data sink provides a light-weight buffering
	 *  mechanism. 
	 *  
	 *  The model:
	 *  Since each sensor works asynchronously with other sensors. The data sink
	 *  should provide an input/output port for each sensor. 
	 *  
	 *  To handle the difference of the data generated from different sensors,
	 *  we may introduce the concept of port adapter that deal with the 
	 *  difference in the future
	 *  
	 *  experiment                             data sink 
	 *  (meta data)<--------> [port adapter 0] (port 0)
	 *  (sensor 1) <--------> [port adapter 1] (port 1)
	 *  (sensor 2) <--------> [port adapter 2] (port 2)
	 *               ......
	 *  (sensor n) <--------> [port adapter n] (port n)
	 *  
	 *  
	 *  */
	public boolean open();	
	public int openDataPort(String attr);
	public void closeDataPort(int port);
	public int openMetaPort();
	public void closeMetaPort(int port);
	// TODO prevent write sensor data to meta port, and vice versus 
	public void write(int port, String s);
	public String readLine(int port);
	public void close();
	
	// public List<Experiment> listExperiments();
}