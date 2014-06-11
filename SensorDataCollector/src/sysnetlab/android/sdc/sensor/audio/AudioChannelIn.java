package sysnetlab.android.sdc.sensor.audio;

public class AudioChannelIn {
    private int mChannelId;
    private int mChannelNameResId;

    public AudioChannelIn(int id, int resId) {
        mChannelId = id;
        mChannelNameResId = resId;
    }

    public int getChannelId() {
        return mChannelId;
    }
    
    public int getChannelNameResId() {
        return mChannelNameResId;
    }
    
    public boolean equals(Object rhs) {
        if (this == rhs) return true;
        
        if (!(rhs instanceof AudioSource)) return false;
        
        AudioChannelIn c = (AudioChannelIn) rhs;
        
        if (mChannelId != c.mChannelId) return false;
        
        return true;
    }    
}
