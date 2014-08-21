/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package sysnetlab.android.sdc.datacollector;

import java.lang.reflect.Field;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class DeviceInformation implements Parcelable {
    private String mManufacturer;
    private String mModel;
    private int mSdkInt;
    private String mSdkCodeName;
    private String mSdkRelease;

    public DeviceInformation(String manufacturer, String model, int sdkInt, String sdkCodeName,
            String sdkRelease) {
        setDeviceInformation(manufacturer, model);
        mSdkInt = sdkInt;
        mSdkCodeName = sdkCodeName;
        mSdkRelease = sdkRelease;
    }

    public DeviceInformation() {
        setDeviceInformation(Build.MANUFACTURER, Build.MODEL);
        mSdkInt = Build.VERSION.SDK_INT;
        mSdkCodeName = getCodeName();
        mSdkRelease = Build.VERSION.RELEASE;
    }

    public DeviceInformation(String manufacturer, String model) {
        setDeviceInformation(manufacturer, model);
        mSdkInt = Build.VERSION.SDK_INT;
        mSdkCodeName = getCodeName();
        mSdkRelease = Build.VERSION.RELEASE;
    }

    public DeviceInformation(DeviceInformation deviceInfo) {
        mManufacturer = deviceInfo.mManufacturer;
        mModel = deviceInfo.mModel;
        mSdkInt = deviceInfo.mSdkInt;
        mSdkCodeName = deviceInfo.mSdkCodeName;
        mSdkRelease = deviceInfo.mSdkRelease;
    }

    public void setDeviceInformation(String manufacturer, String model) {
        mManufacturer = manufacturer != null ? manufacturer : "";
        model = model != null ? model : "";
        if (model.startsWith(mManufacturer)) {
            mModel = model.replaceFirst(mManufacturer + "(\\ *)", "");
        } else {
            mModel = model;
        }
        char first = mModel.charAt(0);
        if (Character.isLowerCase(first)) {
            mModel = Character.toUpperCase(first) + mModel.substring(1);
        }

        mSdkInt = -1;
        mSdkCodeName = "unknown";
        mSdkRelease = "unknown";
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

    public int getSdkInt() {
        return mSdkInt;
    }

    public void setSdkInt(int sdkint) {
        mSdkInt = sdkint;
    }

    public String getSdkCodeName() {
        return mSdkCodeName;
    }

    public void setSdkCodeName(String codeName) {
        mSdkCodeName = codeName;
    }

    public String getSdkRelease() {
        return mSdkRelease;
    }

    public void setSdkRelease(String sdkRelease) {
        mSdkRelease = sdkRelease;
    }

    public String toString() {
        return mManufacturer + " " + mModel;
    }

    public boolean equals(Object rhs) {
        if (rhs == this) {
            return true;
        }

        if (!(rhs instanceof DeviceInformation)) {
            return false;
        }

        DeviceInformation deviceInfo = (DeviceInformation) rhs;

        if (!TextUtils.equals(mManufacturer, deviceInfo.mManufacturer)) {
            return false;
        }

        if (!TextUtils.equals(mModel, deviceInfo.mModel)) {
            return false;
        }

        if (!TextUtils.equals(mSdkRelease, deviceInfo.mSdkRelease)) {
            return false;
        }

        if (mSdkInt != deviceInfo.mSdkInt) {
            return false;
        }

        if (!TextUtils.equals(mSdkCodeName, deviceInfo.mSdkCodeName)) {
            return false;
        }

        return true;
    }

    private String getCodeName() {
        if (!Build.VERSION.CODENAME.equals("REL"))
            return Build.VERSION.CODENAME;

        String codeName = Build.VERSION.CODENAME;

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field f : fields) {
            try {
                if (f.getInt(new Object()) == Build.VERSION.SDK_INT) {
                    codeName = f.getName();
                    break;
                }
            } catch (IllegalAccessException e) {
                // use whatever in code name
            } catch (IllegalArgumentException e) {
                // use whatever in code name
            }
        }

        return codeName;
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
        mSdkInt = inParcel.readInt();
        mSdkCodeName = inParcel.readString();
        mSdkRelease = inParcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeString(mManufacturer);
        outParcel.writeString(mModel);
        outParcel.writeInt(mSdkInt);
        outParcel.writeString(mSdkCodeName);
        outParcel.writeString(mSdkRelease);
    }
}
