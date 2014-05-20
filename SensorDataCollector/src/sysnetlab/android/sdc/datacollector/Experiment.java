
package sysnetlab.android.sdc.datacollector;

import java.util.ArrayList;
import java.util.Calendar;
import java.io.FileNotFoundException;
import java.text.DateFormat;

import sysnetlab.android.sdc.datastore.AbstractStore;
import sysnetlab.android.sdc.datastore.SimpleFileStore;
import sysnetlab.android.sdc.datastore.SimpleFileStoreSingleton;
import sysnetlab.android.sdc.datastore.StoreClassUtil;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.SensorUtilSingleton;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Experiment implements Parcelable {
    private DeviceInformation mDeviceInfo; // on what device?
    private String mName;
    private String mDateTimeCreated;
    private String mDateTimeDone;
    private ArrayList<Tag> mTags;
    private ArrayList<Note> mNotes;
    private ArrayList<TaggingAction> mTaggingActions;

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
        mTaggingActions = new ArrayList<TaggingAction>();
        mStore = store;
    }

    public Experiment(AbstractStore store) {
        this("Unnamed Experiment", DateFormat.getDateTimeInstance().format(
                Calendar.getInstance().getTime()), store);
    }

    public Experiment clone() {
        Experiment experiment;
        if (mStore instanceof SimpleFileStore) {
            experiment = new Experiment(SimpleFileStoreSingleton.getInstance());
        } else {
            experiment = new Experiment(SimpleFileStoreSingleton.getInstance());
            Log.i("SensorDataCollector", "Experiment class needs support on data stype "
                    + mStore.getClass().getName());
        }
        
        experiment.setDeviceInformation(new DeviceInformation(mDeviceInfo));
        experiment.setName(new String(mName));
        experiment.setTags(new ArrayList<Tag>(mTags));
        experiment.setNotes(new ArrayList<Note>(mNotes));
        experiment.setSensors(new ArrayList<AbstractSensor>(mSensors));

        return experiment;
    }

    public ArrayList<Tag> getTags() {
        return mTags;
    }

    public void setTags(ArrayList<Tag> mTags) {
        this.mTags = mTags;
    }

    public void addTag(String strTag, String strDescription) {
        if (strTag != null)
            strTag = strTag.trim();
        else
            return;
        if (strTag.equals("")) {
            return;
        }
        Tag t = new Tag(strTag, strDescription);
        if (!mTags.contains(t)) {
            mTags.add(t);
        }
    }

    public ArrayList<Note> getNotes() {
        return mNotes;
    }

    public void setNotes(ArrayList<Note> mNotes) {
        if (mNotes != null)
            this.mNotes = mNotes;
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
    
    public ArrayList<TaggingAction> getTaggingActions() {
        return mTaggingActions;
    }
    
    public void setTaggingActions(ArrayList<TaggingAction> taggingActions) {
        mTaggingActions = taggingActions;
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

        outParcel.writeString(mStore.getClass().getName());

        outParcel.writeInt(mSensors.size());
        for (AbstractSensor sensor : mSensors) {
            outParcel.writeString(sensor.getName());
            outParcel.writeInt(sensor.getMajorType());
            outParcel.writeInt(sensor.getMinorType());
            outParcel.writeString(sensor.getListener().getChannel().describe());
        }
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

        String storeClassName = inParcel.readString();
        AbstractStore store = StoreClassUtil.getStoreInstanceFromClassName(storeClassName);
        mStore = store;

        mSensors = new ArrayList<AbstractSensor>();
        int numSensors = inParcel.readInt();
        for (int i = 0; i < numSensors; i++) {
            String sensorName = inParcel.readString();
            int sensorMajorType = inParcel.readInt();
            int sensorMinorType = inParcel.readInt();
            String channelDescriptor = inParcel.readString();
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
            AbstractSensor sensor = SensorUtilSingleton.getInstance().getSensor(sensorName,
                    sensorMajorType,
                    sensorMinorType, channel);
            mSensors.add(sensor);
        }
    }

    public Parcelable.Creator<Experiment> getCreator() {
        return CREATOR;
    }

}
