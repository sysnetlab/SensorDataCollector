package sysnetlab.android.sdc.sensor.audio;

public class AudioSource {
    private int mSourceId;
    private int mSourceNameResId;

    AudioSource(int id, int resId) {
        mSourceId = id;
        mSourceNameResId = resId;
    }

    public int getSourceId() {
        return mSourceId;
    }

    public int getSourceNameResId() {
        return mSourceNameResId;
    }
}
