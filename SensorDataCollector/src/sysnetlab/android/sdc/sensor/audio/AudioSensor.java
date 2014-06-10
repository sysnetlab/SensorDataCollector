
package sysnetlab.android.sdc.sensor.audio;

import android.media.AudioRecord;
import sysnetlab.android.sdc.datacollector.DeviceInformation;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.sensor.AbstractSensor;

public class AudioSensor extends AbstractSensor {
    public final static int AUDIOSENSOR_MICROPHONE = 1;

    private static AudioSensor instance = null;       
    
    private String mName = "Audio Sensor (Microphone)";
    
    // formulate in a producer/consumer paradigm
    private AudioRecord mAudioRecord;
    AudioRecordParameter mAudioRecordParameter;
    
    private boolean mIsRecording;
    private Thread mRecordingThread;
    private Channel mDataStoreChannel;
    private short[] mShortBuffer; 
    private int mBufferSize;

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
                    mAudioRecordParameter.getMinBufferSize());            
        }
        
        mRecordingThread = new Thread(new Runnable() {
            public void run() {
                // TODO
                // writeAudioData();
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
            mAudioRecord.read(mShortBuffer, 0, mBufferSize);

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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public boolean equals(Object rhs) {
        // TODO Auto-generated method stub
        return false;
    }
    
    public AudioRecordParameter getAudioRecordParameter() {
        return mAudioRecordParameter;
    }
    
    public void setAudioRecordParameter(AudioRecordParameter param) {
        mAudioRecordParameter = param;
    }
}
