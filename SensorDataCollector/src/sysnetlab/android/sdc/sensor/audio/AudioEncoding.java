package sysnetlab.android.sdc.sensor.audio;

public class AudioEncoding {
    private int mEncodingId;
    private int mEncodingNameResId;

    AudioEncoding(int id, int resId) {
        mEncodingId = id;
        mEncodingNameResId = resId;
    }

    public int getEncodingId() {
        return mEncodingId;
    }
    
    public int getEncodingNameResId() {
        return mEncodingNameResId;
    }
}
