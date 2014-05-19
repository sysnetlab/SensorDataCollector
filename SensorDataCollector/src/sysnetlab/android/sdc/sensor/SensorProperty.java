
package sysnetlab.android.sdc.sensor;

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
}
