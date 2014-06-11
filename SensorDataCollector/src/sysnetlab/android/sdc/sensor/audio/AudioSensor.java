
package sysnetlab.android.sdc.sensor.audio;

import java.util.Date;

import android.media.AudioRecord;
import android.text.TextUtils;
import sysnetlab.android.sdc.datacollector.DeviceInformation;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.sensor.AbstractSensor;

public class AudioSensor extends AbstractSensor {
    public final static int AUDIOSENSOR_MICROPHONE = 1;

    private static AudioSensor instance = null;       
    
    private String mName = "Audio Sensor (Microphone)";
    
    // formulate in a producer/consumer paradigm
    private AudioRecord mAudioRecord;
    private AudioRecordParameter mAudioRecordParameter;
    
    private boolean mIsRecording;
    private Thread mRecordingThread;
    private Channel mDataStoreChannel;
    private short[] mShortBuffer;
    
    private Date mTimeStart;
    private Date mTimeEnd;

    public static AudioSensor getInstance() {
        if (instance == null) {
            instance = new AudioSensor();
        }

        return instance;
    }
    
    protected AudioSensor() {
        super.setMajorType(AbstractSensor.AUDIO_SENSOR);   
        super.setMinorType(AUDIOSENSOR_MICROPHONE);
    }

    public AudioSensor(AudioRecordParameter params) {
        super.setMajorType(AbstractSensor.AUDIO_SENSOR);   
        super.setMinorType(AUDIOSENSOR_MICROPHONE);
        mAudioRecordParameter = params;
    } 
    
    public void startRecording() {
        if (mAudioRecord == null) {
            mAudioRecord = new AudioRecord(mAudioRecordParameter.getSource().getSourceId(), mAudioRecordParameter.getSamplingRate(),
                    mAudioRecordParameter.getChannel().getChannelId(), mAudioRecordParameter.getEncoding().getEncodingId(),
                    mAudioRecordParameter.getBufferSize());            
        }
        
        mRecordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioData();
            }
        }, "SDCAudioRecord");

        mAudioRecord.startRecording();
        mIsRecording = true;
        
        mRecordingThread.start();
    }


    public void stopRecording() {
        if (mAudioRecord == null) {
            return;
        }
        mIsRecording = false;
        mAudioRecord.stop();
        mAudioRecord.release();
        mAudioRecord = null;
        mRecordingThread = null;
    }
    
    public void writeAudioData() {
        while (mIsRecording) {
            mAudioRecord.read(mShortBuffer, 0, mAudioRecordParameter.getBufferSize());

            System.out.println("Short writing to file" + mShortBuffer.toString());
            // ToDo
            byte mByteBuffer[] = short2byte(mShortBuffer);
            mDataStoreChannel.write("Hello, Data Coming");
            // mDataStoreChannel.write(mByteBuffer, 0, mBufferSize * 2);
        }
    }
    
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;
    }
    
    @Override
    public int getMajorType() {
        return super.getMajorType();
    }
    
    @Override
    public int getMinorType() {
        return super.getMinorType();
    }


    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getVendor() {
        return (new DeviceInformation()).getManufacturer();
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public boolean isSameSensor(AbstractSensor sensor) {
        // same physical sensor, may have different settings
        return TextUtils.equals(mName,  sensor.getName()); 
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs) return true;
        
        if (!(rhs instanceof AudioSensor)) return false;
        
        AudioSensor sensor = (AudioSensor) rhs;
        
        if (mAudioRecordParameter == null) { 
            if (sensor.mAudioRecordParameter != null) {
                return false;
            } else {
                return true;
            }
        } else {
            if (!mAudioRecordParameter.equals(sensor.mAudioRecordParameter)) {
                return false;
            } else {
                return true;
            }
        }
    }
    
    public AudioRecordParameter getAudioRecordParameter() {
        return mAudioRecordParameter;
    }
    
    public void setAudioRecordParameter(AudioRecordParameter param) {
        mAudioRecordParameter = param;
    }
    
    public Date getTimeStart() {
        return mTimeStart;
    }
    
    public void setTimeStart(Date d) {
        mTimeStart = d;
    }
    
    public Date getTimeEnd() {
        return mTimeEnd;
    }
    
    public void setTimeEnd(Date d) {
        mTimeEnd = d;
    }
}
