
package sysnetlab.android.sdc.sensor.audio;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.media.AudioRecord;
import android.text.TextUtils;
import android.util.Log;
import sysnetlab.android.sdc.datacollector.DeviceInformation;
import sysnetlab.android.sdc.datacollector.ExperimentTime;
import sysnetlab.android.sdc.datastore.AbstractStore;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.sensor.AbstractSensor;

public class AudioSensor extends AbstractSensor {
    public final static int AUDIOSENSOR_MICROPHONE = 1;

    private static AudioSensor instance = null;       
    
    private String mName = "Audio Sensor (Microphone)";
    
    private AbstractStore.Channel mChannel;
    
    private AudioRecord mAudioRecord;
    private AudioRecordParameter mAudioRecordParameter;
    
    private Date mDateStart;
    private Date mDateEnd;
    private ExperimentTime mExperimentTimeStart;
    private ExperimentTime mExperimentTimeEnd;
    
    // formulate in a producer/consumer paradigm
    private boolean mHavingDataLoss;
    private boolean mIsRecording;
    private Thread mDataCollectionThread;
    private Thread mDataProcessingThread;
    
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

    public AudioSensor(AudioRecordParameter params, AbstractStore.Channel channel) {
        super.setMajorType(AbstractSensor.AUDIO_SENSOR);   
        super.setMinorType(AUDIOSENSOR_MICROPHONE);
        mAudioRecordParameter = params;
        mChannel = channel;
    } 
    
    public void start() {
        if (mIsRecording) {
            return;
        }

        mAudioRecord = new AudioRecord(mAudioRecordParameter.getSource().getSourceId(),
                mAudioRecordParameter.getSamplingRate(),
                mAudioRecordParameter.getChannel().getChannelId(), mAudioRecordParameter
                        .getEncoding().getEncodingId(),
                mAudioRecordParameter.getBufferSize());
        
//        if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
//            Log.e("SensorDataCollector", "AudioSensor::start(): wrong state");
//            return;
//        }
//        Log.d("SensorDataCollector", "AudioSensor::start(): audio record initialized");

        mDateStart = Calendar.getInstance().getTime();
        mExperimentTimeStart = new ExperimentTime();

        Log.d("SensorDataCollector", "AudioSensor::start(): start audio record");        
        mAudioRecord.startRecording();

        BlockingQueue<ByteBuffer> audioDataQueue = new LinkedBlockingQueue<ByteBuffer>();
        Producer p = new Producer(audioDataQueue);
        Consumer c = new Consumer(audioDataQueue);
        mDataCollectionThread = new Thread(p);
        mDataProcessingThread = new Thread(c);

        mIsRecording = true;
        
        mDataCollectionThread.start();
        mDataProcessingThread.start();

        Log.d("SensorDataCollector", "AudioSensor::start(): audio record started");        
    }


    public void stop() {
        if (!mIsRecording || mAudioRecord == null) {
            return;
        }
        
        mIsRecording = false;  
        mExperimentTimeEnd = new ExperimentTime();
        mDateEnd = Calendar.getInstance().getTime(); 
        
        mAudioRecord.stop();
        mAudioRecord.release();
        mAudioRecord = null;
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
    
    public AbstractStore.Channel getChannel() {
        return mChannel;
    }
    
    public AudioRecordParameter getAudioRecordParameter() {
        return mAudioRecordParameter;
    }
    

    public void setChannel(Channel channel) {
        mChannel = channel;
    }
    
    public void setAudioRecordParameter(AudioRecordParameter param) {
        mAudioRecordParameter = param;
    }
    
    public Date getTimeStart() {
        return mDateStart;
    }
    
    public void setTimeStart(Date d) {
        mDateStart = d;
    }
    
    public Date getTimeEnd() {
        return mDateEnd;
    }
    
    public void setTimeEnd(Date d) {
        mDateEnd = d;
    }

    public ExperimentTime getExperimentTimeStart() {
        return mExperimentTimeStart;
    }

    public void setExperimentTimeStart(ExperimentTime experimentTimeStart) {
        this.mExperimentTimeStart = experimentTimeStart;
    }

    public ExperimentTime getExperimentTimeEnd() {
        return mExperimentTimeEnd;
    }

    public void setExperimentTimeEnd(ExperimentTime experimentTimeEnd) {
        this.mExperimentTimeEnd = experimentTimeEnd;
    }
    
    public boolean havingDataLoss() {
        return mHavingDataLoss;
    }
    

    @Override
    public boolean isSameSensor(AbstractSensor sensor) {
        // same physical sensor, may have different settings
        return TextUtils.equals(mName,  sensor.getName()); 
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
    
    @Override
    public String toString() {
        return mName;
    }
    
    private class Producer implements Runnable {
        private final BlockingQueue<ByteBuffer> mQueue;
        
        Producer(BlockingQueue<ByteBuffer> q) {
            mQueue = q; 
        }
        
        public void run() {
          try {
            while (mIsRecording) {
                /* produce() may return null, I think it might be the
                 * result of a race condition.
                 *  
                 * when the AudioRecord is stopped, produce() may still
                 * be running and the thread may attempt to
                 * read a buffer, which is an exception and produce()
                 * returns null.
                 * 
                 * alternatively, we can AudioRecord::stop() after this
                 * while loop. however, it makes the code less readable.
                 * 
                 * a 3rd option, is to deliver an event when it is an
                 * opportune time to AudioRecord::stop(). The event 
                 * handler invokes AudioRecord:;stop()
                 */
                ByteBuffer b = produce();
                if (b != null) {
                    mQueue.put(b); 
                }
            }
          } catch (InterruptedException e) {
              Log.d("SensorDataCollector", "AudioSensor::Producer::run(): interrupted: " + e.toString());
          }
        }
        
        ByteBuffer produce() {
            return recordToByteBuffer(mAudioRecordParameter.getMinBufferSize());
        }
    }
    
    private class Consumer implements Runnable {
        private final BlockingQueue<ByteBuffer> mQueue;
        
        Consumer(BlockingQueue<ByteBuffer> q) { 
            mQueue = q; 
        }
        
        public void run() {
          try {
            while (mIsRecording) { 
                consume(mChannel, mQueue.take()); 
            }
          } catch (InterruptedException e) {
              Log.i("SensorDataCollector", "AudioSensor::Consumer::run(): " + e.toString());
          }
        }
        void consume(AbstractStore.Channel c, ByteBuffer b) {
            processBufferedAudioData(c, b);
        }
      }
    
    private ByteBuffer recordToByteBuffer(int capacity) {
//        Log.d("SensorDataCollector",
//                "AudioSensor::recordToByteBuffer(): entered with capacity = " + capacity); 
        
        ByteBuffer buf = ByteBuffer.allocateDirect(capacity);
        
        int totalBytesRead = 0;
        int numBytesToRead = capacity;
        while (numBytesToRead > 0) {
            int numBytesRead = mAudioRecord.read(buf, numBytesToRead);
            
            if (numBytesRead > 0) {
                totalBytesRead += numBytesRead;
                numBytesToRead = capacity - totalBytesRead;
                buf.position(totalBytesRead);
            } else if (numBytesRead == AudioRecord.ERROR_INVALID_OPERATION) {
                Log.e("SensorDataCollector",
                        "AudioSensor::recordToByteBuffer(): AudioRecord.ERROR_INVALID_OPERATION");
                buf = null;
                break;
            } else if (numBytesRead == AudioRecord.ERROR_BAD_VALUE) {
                Log.e("SensorDataCollector",
                        "AudioSensor::recordToByteBuffer(): AudioRecord.ERROR_BAD_VALUE");
                buf = null;
                break;
            } else if (numBytesRead == AudioRecord.ERROR) {
                Log.e("SensorDataCollector", "AudioSensor::recordToByteBuffer(): AudioRecord.ERROR");
                buf = null;
                break;
            }
        }
        
//        Log.d("SensorDataCollector",
//                "AudioSensor::recordToByteBuffer():buf = " + buf);        
        return buf;
    }
    
    private void processBufferedAudioData(AbstractStore.Channel c, ByteBuffer bb) {
        int sizeInBytes = bb.capacity();
        Log.d("SensorDataCollector",
                "AudioSensor::processBufferedAudioData():sizeInBytes = " + sizeInBytes);
        
        byte[] b = new byte[sizeInBytes];
        bb.clear();
        bb.get(b);
        
        c.write(b, 0, sizeInBytes);
    }
}
