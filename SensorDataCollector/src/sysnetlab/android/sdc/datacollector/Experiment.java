package sysnetlab.android.sdc.datacollector;

import java.util.ArrayList;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;

import sysnetlab.android.sdc.datasink.DataSinkSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import android.os.Parcel;
import android.os.Parcelable;


public class Experiment implements Parcelable {
	private DeviceInformation mDeviceInfo;			// on what device?
	private String mName;
	private String mDateTimeCreated;
	private String mDateTimeDone;
	private ArrayList<Tag> mTags;
	private ArrayList<Note> mNotes;
	private ArrayList<AbstractSensor> mSensors;	
	
	public ArrayList<Tag> getmTags() {
		return mTags;
	}

	public void setTags(ArrayList<Tag> mTags) {
		this.mTags = mTags;
	}

	public ArrayList<Note> getmNotes() {
		return mNotes;
	}

	public void setNotes(ArrayList<Note> mNotes) {
		this.mNotes = mNotes;
	}

	public static final Parcelable.Creator<Experiment> CREATOR
		= new Parcelable.Creator<Experiment>() {
			@Override
			public Experiment createFromParcel(Parcel inParcel) {
				return new Experiment(inParcel);
			}

			@Override
			public Experiment[] newArray(int size) {
				return new Experiment[size];
			}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel outParcel, int flags) {
		outParcel.writeString(mName);
		outParcel.writeString(mDateTimeCreated);  
	}
	
	public Experiment(Parcel inParcel) 
	{
		mName = inParcel.readString();
		mDateTimeCreated = inParcel.readString();
	}
	
	public Experiment() 
	{
		mDeviceInfo = new DeviceInformation();
		DateFormat df = DateFormat.getDateTimeInstance();
		mDateTimeCreated = df.format(Calendar.getInstance().getTime());
		mName = "Unnamed Experiment (" + mDateTimeCreated + ")";	
	}
	
	public Experiment(int metaPort) {
		readConfiguratione(metaPort);
	}
	
	public Experiment(String configFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(configFile));
		mName = reader.readLine();
		mDateTimeCreated = reader.readLine();
		reader.close();
	}
	
	public Experiment(String n, String dC)
	{
		mName = n;
		mDateTimeCreated = dC;
	}
	
	public void writeConfiguration(int metaPort)
	{
		String configMsg = getName() + "\n" + getDateTimeCreated() + "\n";
		DataSinkSingleton.getInstance().write(metaPort,  configMsg);
	}
	
	private void readConfiguratione(int metaPort)
	{
		mName = DataSinkSingleton.getInstance().readLine(metaPort);
		mDateTimeCreated = DataSinkSingleton.getInstance().readLine(metaPort);
	}	
	
	/*
	private void writeExperimentConfiguration(Experiment exp, String experimentDir)
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
	

	
	
	private Experiment readExperimentConfiguratione(String experimentDir)
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
	*/
	
	public String getName()
	{
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getDateTimeCreated()
	{
		return mDateTimeCreated;
	}
	
	public ArrayList<AbstractSensor> getSensors() {
		return mSensors;
	}

	public void setSensors(ArrayList<AbstractSensor> sensors) {
		this.mSensors = sensors;
	}

	public DeviceInformation getDeviceInformation() {
		return mDeviceInfo;
	}

	public void setDeviceInformation(DeviceInformation deviceInfo) {
		mDeviceInfo = deviceInfo;
	}

	public String getDateTimeDone() {
		return mDateTimeDone;
	}

	public void setDateTimeDone(String mDateTimeDone) {
		this.mDateTimeDone = mDateTimeDone;
	}
}
