
package sysnetlab.android.sdc.sensor;

import android.text.TextUtils;

public class SensorProperty {
    private String mName;
    private String mValue;

    public SensorProperty() {
        this("", "");
    }

    public SensorProperty(String name, String value) {
        mName = name;
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(final String value) {
        mValue = value;
    }
    
    public boolean equals(Object rhs) {
        if (this == rhs) return true;
        
        if (!(rhs instanceof SensorProperty)) return false;
        
        SensorProperty property = (SensorProperty) rhs;
        
        if (!TextUtils.equals(mName, property.mName)) return false;
        
        if (!TextUtils.equals(mValue, property.mValue)) return false;
        
        return true;
    }
}
