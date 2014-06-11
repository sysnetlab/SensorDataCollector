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
    
    public boolean equals(Object rhs) {
        if (this == rhs) return true;
        
        if (!(rhs instanceof AudioSource)) return false;
        
        AudioSource s = (AudioSource) rhs;
        
        if (mSourceId != s.mSourceId) return false;
        
        return true;
    }
}
