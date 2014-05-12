package sysnetlab.android.sdc.datasink;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import sysnetlab.android.sdc.datacollector.Experiment;
import android.os.Environment;
import android.util.Log;

public class SimpleFileSink implements DataSink {
	String mParentPath; 
	String mPath;
	int mNumPorts;
	int mMetaPort;
	ArrayList<PrintStream> mPrintStreamList;
	
	public SimpleFileSink() {
		Log.i("SensorDataCollector", "entering SimpleFileSink.constructor() ...");
		String parentPath = Environment.getExternalStorageDirectory().getPath();
		parentPath = parentPath + "/SensorData";
		File dataDir = new File(parentPath);
		if (!dataDir.exists()) {
			dataDir.mkdir();
		} 
		mParentPath = parentPath;		
		mPrintStreamList = new ArrayList<PrintStream>();
		mNumPorts = 0;
	}
	
	@Override
	public boolean open() {
		String pathPrefix = mParentPath + "/exp"; 
		DecimalFormat f = new DecimalFormat("00000");
		String path;
		File dataDir;
		int i = 0;
		
		do {
			i = i + 1;
			path = pathPrefix + f.format(i);
			dataDir = new File(path);
		} while (dataDir.exists());
		
		mPath = path;
		return dataDir.mkdir();
	}
	
	@Override
	public void close() {
		for (PrintStream out : mPrintStreamList) {
			out.close();
		}
		mPrintStreamList = null;
	}	
	
	@Override
	public int openDataPort(String sensorName) {
		String filename = sensorName.replace(' ', '_') + ".txt";
		String path = mPath + "/" + filename;
		try {
			PrintStream out = null;
			out = new PrintStream(new BufferedOutputStream(new FileOutputStream(path)));
			mPrintStreamList.add(out);
			mMetaPort = mNumPorts;
			mNumPorts ++;
		} catch (FileNotFoundException e) {
			Log.e("SensorDataCollector", "Calling open:", e);
			mMetaPort = -1;
		}
		
		return mMetaPort;		
	}



	@Override
	public void closeDataPort(int port) {
		PrintStream out = (PrintStream)mPrintStreamList.get(port);
		if (out != null) out.close();
	}	



	@Override
	public void write(int port, String s) {
		PrintStream out = mPrintStreamList.get(port);
		out.print(s);
	}

	@Override
	public int openMetaPort() {
		String path = mPath + "/.experiment";
		int portNum = mNumPorts;		
		try {
			PrintStream out = new PrintStream(new FileOutputStream(path));
			mPrintStreamList.add(out);
			mNumPorts ++;	
			return portNum;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		}		
	}

	@Override
	public void closeMetaPort(int metaPort) {
		PrintStream out = (PrintStream)mPrintStreamList.get(metaPort);
		if (out != null) out.close();
		
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
		if (experimentDirNames == null) {
			return listExp;
		}
		for (String dirName : experimentDirNames) {
			String configFile = mParentPath + "/" + dirName + "./experiment";
			try {
				Experiment exp = new Experiment(configFile);
				listExp.add(exp);
			} catch (IOException e) {
				File file = new File(mParentPath + "/" + dirName);
				String lastModDate = SimpleDateFormat.getDateTimeInstance().format((new Date(file.lastModified()))); 				
				listExp.add(new Experiment(dirName, lastModDate));
			}
		}

		return listExp;
	}

	@Override
	public String readLine(int port) {
		// TODO Auto-generated method stub
		Log.e("SensorDataCollector", "SimpleFileSink.readLine has not been implemented.");
		return null;
	}	
}
