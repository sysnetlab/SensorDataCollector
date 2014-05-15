
package sysnetlab.android.sdc.datacollector;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.DateFormat;

import sysnetlab.android.sdc.datastore.AbstractStore;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import android.os.Parcel;
import android.os.Parcelable;

public class Experiment implements Parcelable {
    private DeviceInformation mDeviceInfo; // on what device?
    private String mName;
    private String mDateTimeCreated;
    private String mDateTimeDone;
    private ArrayList<Tag> mTags;
    private ArrayList<Note> mNotes;

    private ArrayList<AbstractSensor> mSensors;
    private AbstractStore mStore;
    
    public Experiment(String n, String dt, AbstractStore store) {
        mDeviceInfo = new DeviceInformation();
        mName = n;
        mDateTimeCreated = dt;
        mDateTimeDone = dt;
        mTags = new ArrayList<Tag>();
        mNotes = new ArrayList<Note>();
        mSensors = new ArrayList<AbstractSensor>();
        mStore = store;
    }


    public Experiment() {
        this("Unnamed Experiment", DateFormat.getDateTimeInstance().format(
                Calendar.getInstance().getTime()), null);
    }

    public Experiment(String n, String dt) {
        this(n, dt, null);
    }

    public Experiment(AbstractStore store) {
        this("Unnamed Experiment", DateFormat.getDateTimeInstance().format(
                Calendar.getInstance().getTime()), store);
    }
    
    public ArrayList<Tag> getTags() {
        return mTags;
    }

    public void setTags(ArrayList<Tag> mTags) {
        this.mTags = mTags;
    }

    public void addTag(String strTag) {
    	if(strTag!=null)
    		strTag = strTag.trim();
    	else
    		return;
        if (strTag.equals("")) {
            return;
        }
        Tag t = new Tag(strTag);
        if (!mTags.contains(t)) {
            mTags.add(new Tag(strTag));
        }
    }

    public ArrayList<Note> getNotes() {
        return mNotes;
    }

    public void setNotes(ArrayList<Note> mNotes) {
    	if(mNotes!=null)
    		this.mNotes = mNotes;
    }

    public Experiment() {
        this("Unnamed Experiment", DateFormat.getDateTimeInstance().format(
                Calendar.getInstance().getTime()), null);
    }

    public Experiment(String n, String dt) {
        this(n, dt, null);
    }

    public Experiment(AbstractStore store) {
        this("Unnamed Experiment", DateFormat.getDateTimeInstance().format(
                Calendar.getInstance().getTime()), store);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDateTimeCreated() {
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

    public void setDateTimeDone(String dateTimeDone) {
        mDateTimeDone = dateTimeDone;
    }

    public AbstractStore getStore() {
        return mStore;
    }

    public void setStore(AbstractStore store) {
        mStore = store;
    }

    public String toString() {
        return mName + " " + mDateTimeCreated;
    }
    
    public static final Parcelable.Creator<Experiment> CREATOR = new Parcelable.Creator<Experiment>() {
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
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeString(mName);
        outParcel.writeString(mDateTimeCreated);
        outParcel.writeString(mDateTimeDone);
        outParcel.writeTypedList(mTags);
        outParcel.writeTypedList(mNotes);
        outParcel.writeParcelable(mDeviceInfo, flags);
        // ? sensors ? // can be recovered from the hardware
        // ? store? // let us not worry about it now
    }

    public Experiment(Parcel inParcel) {
        mName = inParcel.readString();
        mDateTimeCreated = inParcel.readString();
        mDateTimeDone = inParcel.readString();
        mTags = new ArrayList<Tag>();
        inParcel.readTypedList(mTags, Tag.CREATOR);
        mNotes = new ArrayList<Note>();
        inParcel.readTypedList(mNotes, Note.CREATOR);
        mDeviceInfo = inParcel.readParcelable(DeviceInformation.class.getClassLoader());
    }

	public Parcelable.Creator<Experiment> getCreator() {
		return CREATOR;
	}
}
