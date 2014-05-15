
package sysnetlab.android.sdc.datacollector;

import android.os.Build;

public class DeviceInformation {
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
}
