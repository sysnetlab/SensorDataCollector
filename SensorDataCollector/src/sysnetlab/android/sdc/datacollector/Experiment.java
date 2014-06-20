
package sysnetlab.android.sdc.datacollector;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import sysnetlab.android.sdc.datastore.AbstractStore;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.datastore.SimpleFileStore;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

public class Experiment implements Parcelable {
    private DeviceInformation mDeviceInfo; // on what device?
    private String mName;
    private Date mDateTimeCreated;
    private Date mDateTimeDone;
    private List<Tag> mTags;
    private List<Note> mNotes;
    private List<TaggingAction> mTaggingActions;
    private String mPath;
    private boolean mHasChanges = false;
    private static String mDefaultName = "Unnamed Experiment";

    private List<AbstractSensor> mSensors;
    //private AbstractStore mStore;

    public Experiment(String name, Date dateCreated) {
        mDeviceInfo = new DeviceInformation();
        mName = name != null ? name : "";
        mDateTimeCreated = dateCreated != null ? dateCreated : Calendar.getInstance().getTime();
        mDateTimeDone = dateCreated != null ? dateCreated : Calendar.getInstance().getTime();
        mTags = new ArrayList<Tag>();
        mNotes = new ArrayList<Note>();
        mSensors = new ArrayList<AbstractSensor>();
        mTaggingActions = new ArrayList<TaggingAction>();
        mPath = null;
    }

    public Experiment(String name) {
        this(name, Calendar.getInstance().getTime());
    }
    
    public Experiment() {
        this(mDefaultName, Calendar.getInstance().getTime());
    }

    public Experiment clone() {
        Experiment experiment = new Experiment();
        experiment.setDeviceInformation(new DeviceInformation(mDeviceInfo));
        experiment.setName(new String(mName));
        experiment.setTags(new ArrayList<Tag>(mTags));
        experiment.setNotes(new ArrayList<Note>(mNotes));
        experiment.setSensors(new ArrayList<AbstractSensor>(mSensors));        
        return experiment;
    }

    public List<Tag> getTags() {
        return mTags;
    }

    public void setTags(List<Tag> tags) {
    	if(tags!=null){
    		this.mTags = tags;
    		this.mHasChanges = true;
    	}
    }

    public boolean addTag(String strTag, String strDescription) {
        if (strTag != null)
            strTag = strTag.trim();
        else
            return false;
        
        if (strTag.equals("")) {
            return false;
        }
        
        Tag t = new Tag(strTag, strDescription);
        
        boolean tagExists = false;
        for (Tag tagInList : mTags) {
            if (TextUtils.equals(t.getName(), tagInList.getName())) {
                tagExists = true;
                break;
            }
        }
        
        if (!tagExists) {
        	this.mHasChanges = true;
            return mTags.add(t);
        } else {
            return false;
        } 
    }

    public List<Note> getNotes() {
        return mNotes;
    }

    public void addNote(Note note){
    	mNotes.add(note);
    	this.mHasChanges = true;
    }
    
    public List<Note> getNotesSortedByDate() {
        List<Note> allNotes = getNotes();
        Collections.sort(allNotes, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return -(n1.getDateCreated().compareTo(n2.getDateCreated()));
            }
        });

        return allNotes;
    }
    
    public void setNotes(List<Note> mNotes) {
        if (mNotes != null){
            this.mNotes = mNotes;
            this.mHasChanges = true;
        }
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name != null ? name : "";
        this.mHasChanges = true;
    }
    
    public void setHasChanges(boolean hasChanges){
    	this.mHasChanges = hasChanges;
    }
    
    public void setHasChangesFromInt(int hasChanges){
    	if(hasChanges==0)
    		this.mHasChanges = false;
    	else 
    		this.mHasChanges = true;
    }
    
    public boolean hasChanges(){
    	return mHasChanges;
    }
    
    public String getDateTimeCreatedAsStringUTC() {
        // use XML dateTimeType format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd'T'HH:mm:ss.SSSZ", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(mDateTimeCreated);
    }

    public Date getDateTimeCreated() {
        return mDateTimeCreated;
    }
    
    public String getDefaultName(){
    	return mDefaultName;
    }
    
    public void setDateTimeCreatedFromStringUTC(String dateCreated) {
        try {
            // use XML dateTimeType format
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd'T'HH:mm:ss.SSSZ", Locale.US);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            mDateTimeCreated = formatter.parse(dateCreated);                
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }    

    public String getDateTimeCreatedAsString() {
        return SimpleDateFormat.getDateTimeInstance().format(mDateTimeCreated);
    }

    
    public void setDateTimeCreatedFromString(String dateCreated) {
        try {
			mDateTimeCreated = SimpleDateFormat.getDateTimeInstance().parse(dateCreated);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public long getDateTimeCreatedAsLong(){
    	return mDateTimeCreated.getTime();
    }
    
    public void setDateTimeCreatedFromLong(long dateCreated){    	
    		mDateTimeCreated = new Date(dateCreated);    	
    }
    
    public long getDateTimeDoneAsLong(){
    	return mDateTimeDone.getTime();
    }
    
    public void setDateTimeDoneFromLong(long dateDone){    	
    		mDateTimeDone = new Date(dateDone);    	
    }
    
    public List<AbstractSensor> getSensors() {

        return mSensors;
    }

    public void setSensors(List<AbstractSensor> sensors) {
    	if(sensors!=null){
    		this.mSensors = sensors;
    		this.mHasChanges = true;
    	}
    }

    public DeviceInformation getDeviceInformation() {
        return mDeviceInfo;
    }

    public void setDeviceInformation(DeviceInformation deviceInfo) {
        mDeviceInfo = deviceInfo;
    }

    public String getDateTimeDoneAsString() {
        return SimpleDateFormat.getDateTimeInstance().format(mDateTimeDone);
    }
    
    public String getDateTimeDoneAsStringUTC() {
        // use XML dateTimeType format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd'T'HH:mm:ss.SSSZ", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(mDateTimeDone);
    }


    public Date getDateTimeDone() {
        return mDateTimeDone;
    }
    
    
    public void setDateTimeDoneFromStringUTC(String dateDone) {
        try {
            // use XML dateTimeType format
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd'T'HH:mm:ss.SSSZ", Locale.US);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            mDateTimeDone = formatter.parse(dateDone);                
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }
    
    public void setDateTimeDoneFromString(String dateDone) {
    	try {
 			mDateTimeDone = SimpleDateFormat.getDateTimeInstance().parse(dateDone);
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
    }
    
    public void setDateTimeDone(Date dateDone) {
		mDateTimeDone = dateDone;
    }
    
    public List<TaggingAction> getTaggingActions() {
        return mTaggingActions;
    }
    
    public void setTaggingActions(List<TaggingAction> taggingActions) {
        mTaggingActions = taggingActions;
    }

    public String toString() {
        return mName + " " + mDateTimeCreated;
    }
    
    public boolean equals(Object rhs) {
        if (rhs == this) {
            return true;
        }
        
        Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): checkpoint #1");
        
        if (!(rhs instanceof Experiment)) {
            return false;
        }

        Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): checkpoint #2");        
        
        Experiment e = (Experiment) rhs;
        
        if (!TextUtils.equals(mName, e.mName)) {
            return false;
        } 
        
        Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): checkpoint #3");
        
        // Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): " +
        //        mDeviceInfo.toString() + " - " + e.mDeviceInfo.toString());
        
        if (mDeviceInfo != null && e.mDeviceInfo == null) {
            return false;
        } else if (!mDeviceInfo.equals(e.mDeviceInfo)) {
            return false;
        }
        
        Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): checkpoint #4"); 

        if (mDateTimeCreated != null && e.mDateTimeCreated == null) {
            return false;
        } else if (!mDateTimeCreated.equals(e.mDateTimeCreated)) {
            return false;
        }        
        
        Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): checkpoint #5");
        
        if (mDateTimeDone != null && e.mDateTimeDone == null) {
            return false;
        } else if (!mDateTimeDone.equals(e.mDateTimeDone)) {
            return false;
        }
        
        Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): checkpoint #6");        
        if (mTags != null && e.mTags == null) {
            return false;
        } else if (!mTags.equals(e.mTags)) {
            return false;
        }
        
        Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): checkpoint #7");        
        
        if (mNotes != null && e.mNotes == null) {
            return false;
        } else if (!mNotes.equals(e.mNotes)){
            return false;
        }
        
        Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): checkpoint #8");        

        if (mTaggingActions != null && e.mTaggingActions == null) {
            return false;
        } else if (!mTaggingActions.equals(e.mTaggingActions)){
            return false;
        }
        
        Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): checkpoint #9");        

        if (mSensors != null && e.mSensors == null) {
            return false;
        } else if (!mSensors.equals(e.mSensors)){
            Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): checkpoint #10 - " + mSensors.size() + "|" + e.mSensors.size());            
            return false;
        }

        Log.d("SensorDataCollotr.UnitTest", "Experiment::equals(): checkpoint #11");        
        
        return true;
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

        outParcel.writeLong(getDateTimeCreatedAsLong());
        outParcel.writeLong(getDateTimeDoneAsLong());

        outParcel.writeTypedList(mTags);

        outParcel.writeTypedList(mNotes);

        outParcel.writeParcelable(mDeviceInfo, flags);

        outParcel.writeInt(mSensors.size());
        for (AbstractSensor sensor : mSensors) {
            outParcel.writeString(sensor.getName());
            outParcel.writeInt(sensor.getMajorType());
            outParcel.writeInt(sensor.getMinorType());
            switch (sensor.getMajorType()) {
                case AbstractSensor.ANDROID_SENSOR:
                    outParcel.writeString(((AndroidSensor)sensor).getListener().getChannel().describe());
                    break;
            }
        }        
        if(mHasChanges)        	
        	outParcel.writeInt(1);
        else
        	outParcel.writeInt(0);
        // TODO write the tagging action properly to parcel.
    }

    public Experiment(Parcel inParcel) {
        Log.i("SensorDataCollector", "Experiment(Parcel) called.");
        //TODO read it properly from parcel.
        mTaggingActions = new ArrayList<TaggingAction>();
        
        mName = inParcel.readString();

        setDateTimeCreatedFromLong(inParcel.readLong());
        setDateTimeDoneFromLong(inParcel.readLong());

        mTags = new ArrayList<Tag>();
        inParcel.readTypedList(mTags, Tag.CREATOR);

        mNotes = new ArrayList<Note>();
        inParcel.readTypedList(mNotes, Note.CREATOR);

        mDeviceInfo = inParcel.readParcelable(DeviceInformation.class.getClassLoader());

        AbstractStore store = StoreSingleton.getInstance();
        
        mSensors = new ArrayList<AbstractSensor>();
        int numSensors = inParcel.readInt();
        for (int i = 0; i < numSensors; i++) {
            String sensorName = inParcel.readString();
            int sensorMajorType = inParcel.readInt();
            int sensorMinorType = inParcel.readInt();
            String channelDescriptor = "";
            switch (sensorMajorType) {
                case AbstractSensor.ANDROID_SENSOR:
                    channelDescriptor = inParcel.readString();
                    break;
            }
            // TODO make sure channel is read-only or write-only or
            // bidirectional
            Channel channel = null;
            if (channelDescriptor != null && !channelDescriptor.trim().equals("")) {
                if (store instanceof SimpleFileStore) {
                    try {
                        channel = ((SimpleFileStore) store).new SimpleFileChannel(channelDescriptor);
                    } catch (FileNotFoundException e) {
                        Log.i("SensordataCollector",
                                "Experiment::Expriment(Parcel): calling new SimpleFileChannel");
                    }
                } else {
                    Log.i("SensorDataCollector",
                            "Experiment::Expriment(Parcel): need to deal with " +
                                    store.getClass().getName());
                }
            }
            AbstractSensor sensor = SensorDiscoverer.constructSensorObject(sensorName,
                    sensorMajorType,
                    sensorMinorType, channel, null);
            mSensors.add(sensor);
        }
        setHasChangesFromInt(inParcel.readInt());
    }
    
    public Parcelable.Creator<Experiment> getCreator() {
        return CREATOR;
    }
    
    public void setPath(String p) {
    	mPath = p;
    }
    
    public String getPath() {
    	return mPath;
    }

}
