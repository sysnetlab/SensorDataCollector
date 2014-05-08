package sysnetlab.android.sdc.datasink;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import sysnetlab.android.sdc.datacollector.Experiment;
import android.os.Environment;
import android.util.Log;

public class SimpleFileSink implements DataSink {
	String mParentPath; 
	String mPath;
	LinkedList<PrintStream> mPrintStreamList;
	
	public SimpleFileSink() {
		Log.i("SensorDataCollector", "entering SimpleFileSink.constructor() ...");
		String parentPath = Environment.getExternalStorageDirectory().getPath();
		parentPath = parentPath + "/SensorData";
		File dataDir = new File(parentPath);
		if (!dataDir.exists()) {
			dataDir.mkdir();
		} 
		mParentPath = parentPath;		
		mPrintStreamList = new LinkedList<PrintStream>();
	}
	
	@Override
	public PrintStream open(String filename) {
		PrintStream out = null;
		String path = mPath + "/" + filename;
		try {
			out = new PrintStream(new BufferedOutputStream(new FileOutputStream(path)));
			mPrintStreamList.add(out);
		} catch (FileNotFoundException ex) {
			Log.e("SensorDataCollector", "Calling open:", ex);
		}
		return out;
	}

	@Override
	public void close() {
		while (!mPrintStreamList.isEmpty()) {
			PrintStream out = mPrintStreamList.remove();
			out.close();
		}
	}

	@Override
	public Experiment createExperiment() {
		String pathPrefix = mParentPath + "/exp"; 
		DecimalFormat f = new DecimalFormat("00000");
		String timeCreated = SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()); 
		String path;
		File dataDir;
		int i = 0;
		
		do {
			i = i + 1;
			path = pathPrefix + f.format(i);
			dataDir = new File(path);
		} while (dataDir.exists());
		dataDir.mkdir();
		mPath = path;
		
		Experiment exp = new Experiment("exp" + f.format(i), timeCreated);
		writeExperimentConfigurationFile(exp, path);
		
		Log.i("SensorDataCollector", "path " + path + " does not exist and is created.");
		return exp;
	}

	@Override
	public List<Experiment> listExperiments() {
		List<Experiment> listExp = new ArrayList<Experiment>();
		
		File parentDir = new File(mParentPath);
		String[] experimentDirNames = parentDir.list(new FilenameFilter() {
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		
		for (String dirName : experimentDirNames)
		{
			Experiment exp = readExperimentConfigurationFile(mParentPath + "/" + dirName);
			if(exp != null)
			{
				listExp.add(exp);
			}
			else
			{
				listExp.add(new Experiment(dirName, "unknown"));
			}
		}
		
		return listExp;
	}
	
	private void writeExperimentConfigurationFile(Experiment exp, String experimentDir)
	{
		try {
			PrintStream out = new PrintStream(new FileOutputStream(experimentDir + "/.experiment"));
			out.println(exp.getName());
			out.println(exp.getDateCreated());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Experiment readExperimentConfigurationFile(String experimentDir)
	{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(experimentDir + "/.experiment"));
			String name = reader.readLine();
			String dateCreated = reader.readLine();
			reader.close();
			return new Experiment(name, dateCreated);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
