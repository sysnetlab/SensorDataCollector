
package sysnetlab.android.sdc.datacollector;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public class DeviceInformation implements Parcelable {
    private String mManufacturer;
    private String mModel;

    public DeviceInformation() {
        setDeviceInformation(Build.MANUFACTURER, Build.MODEL);
    }

    public void setDeviceInformation(String manufacturer, String model) {
        mManufacturer = manufacturer;
        if (model.startsWith(mManufacturer)) {
            mModel = model.replaceFirst(mManufacturer + "(\\ *)", "");
        } else {
            mModel = model;
        }
        char first = mModel.charAt(0);
        if (Character.isLowerCase(first)) {
            mModel = Character.toUpperCase(first) + mModel.substring(1);
        }
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String mModel) {
        this.mModel = mModel;
    }

    public String getManufacturer() {
        return mManufacturer;
    }

    public void setManufacturer(String mManufacturer) {
        this.mManufacturer = mManufacturer;
    }

    public String toString() {
        return mManufacturer + " " + mModel;
    }

    public static final Parcelable.Creator<DeviceInformation> CREATOR = new Parcelable.Creator<DeviceInformation>() {
        @Override
        public DeviceInformation createFromParcel(Parcel inParcel) {
            return new DeviceInformation(inParcel);
        }

        @Override
        public DeviceInformation[] newArray(int size) {
            return new DeviceInformation[size];
        }
    };
    
    public DeviceInformation(Parcel inParcel) {
        mManufacturer = inParcel.readString();
        mModel = inParcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeString(mManufacturer);
        outParcel.writeString(mModel);
    }
}
