package sysnetlab.android.sdc.sensor.audio;

public class AudioRecordParameter {
    private int mSamplingRate;
    private AudioChannelIn mChannel;
    private AudioEncoding mEncoding;
    private AudioSource mSource;
    private int mBufferSize;
    private int mMinBufferSize;

    public AudioRecordParameter(int rate, AudioChannelIn channel, AudioEncoding encoding,
            AudioSource source, int bufferSize, int minBufferSize) {
        mSamplingRate = rate;
        mChannel = channel;
        mEncoding = encoding;
        mSource = source;
        mBufferSize = bufferSize;
        mMinBufferSize = minBufferSize;
    }

    public AudioRecordParameter() {
    }

    public int getSamplingRate() {
        return mSamplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        mSamplingRate = samplingRate;
    }

    public AudioChannelIn getChannel() {
        return mChannel;
    }

    public void setChannel(AudioChannelIn channel) {
        mChannel = channel;
    }

    public AudioEncoding getEncoding() {
        return mEncoding;
    }

    public void setEncoding(AudioEncoding encoding) {
        mEncoding = encoding;
    }

    public AudioSource getSource() {
        return mSource;
    }

    public void setSource(AudioSource source) {
        mSource = source;
    }

    public int getBufferSize() {
        return mBufferSize;
    }

    public void setBufferSize(int bufferSize) {
        mBufferSize = bufferSize;
    }  
    
    public int getMinBufferSize() {
        return mBufferSize;
    }

    public void setMinBufferSize(int minBufferSize) {
        mBufferSize = minBufferSize;
    }  
    
    public boolean equals(Object rhs) {
        if (this == rhs) return true;
        
        if (!(rhs instanceof AudioRecordParameter)) return false;
        
        AudioRecordParameter p = (AudioRecordParameter) rhs;
        
        if (mSamplingRate != p.mSamplingRate) return false;
        
        if (mBufferSize != p.mBufferSize) return false;
        
        if (mMinBufferSize != p.mMinBufferSize) return false;
        
        if (mChannel == null) {
            if (p.mChannel != null) return false;
        } else {
            if (!mChannel.equals(p.mChannel)) return false;
        }
        
        if (mEncoding == null) {
            if (p.mEncoding != null) return false;
        } else {
            if (!mEncoding.equals(p.mEncoding)) return false;            
        }
        
        if (mSource == null) {
            if (p.mSource != null) return false;
        } else {
            if (!mSource.equals(p.mSource)) return false;
        }
        
        return true;
    }
}