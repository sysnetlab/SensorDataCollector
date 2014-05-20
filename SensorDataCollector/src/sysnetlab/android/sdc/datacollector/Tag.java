
package sysnetlab.android.sdc.datacollector;

import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {
    private String mName;
    private String mShortDescription;
    private String mLongDescription;
    
    public Tag(String name) {
        this(name, "", "");
    }

    public Tag(String name, String shortDesc) {
        this(name, shortDesc, "");
    }
 
    public Tag(String name, String shortDesc, String longDesc) {
        mName = name;
        mShortDescription = shortDesc;
        mLongDescription = longDesc;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public String getLongDescription() {
        return mLongDescription;
    }

    public String toString() {
        return mName;
    }

    public void setLongDescription(String mLongDescription) {
        this.mLongDescription = mLongDescription;
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof Tag)
        {
            sameSame = this.mName.equals(((Tag) object).mName);
        }

        return sameSame;
    }
    
    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {

        @Override
        public Tag createFromParcel(Parcel inParcel) {
            return new Tag(inParcel);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
        
    };
    
    public Tag(Parcel inParcel) {
        mName = inParcel.readString();
        mShortDescription = inParcel.readString();
        mLongDescription = inParcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeString(mName);
        outParcel.writeString(mShortDescription);
        outParcel.writeString(mLongDescription);       
    }
    
    public Parcelable.Creator<Tag> getCreator() {
		return CREATOR;
	}
}
