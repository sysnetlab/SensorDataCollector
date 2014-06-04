
package sysnetlab.android.sdc.sensor.audio;

import android.media.AudioRecord;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.sensor.AbstractSensor;

public class AudioSensor extends AbstractSensor {
    // formulate in a producer/consumer paradigm
    private AudioRecord mAudioRecord;
    private boolean mIsRecording;
    private Thread mRecordingThread;
    private Channel mDataStoreChannel;
    private short[] mShortBuffer; 
    private int mBufferSize;

    public AudioSensor(AudioRecordParameter params) {
        mAudioRecord = new AudioRecord(params.getSource().getSourceId(), params.getSamplingRate(),
                params.getChannel().getChannelId(), params.getEncoding().getEncodingId(),
                params.getMinBufferSize());

    } 
    
    public void startRecording() {
        if (mAudioRecord == null) {
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
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getVendor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getSensor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isSameSensor(AbstractSensor sensor) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSensor(Object sensor) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean equals(Object rhs) {
        // TODO Auto-generated method stub
        return false;
    }

}
