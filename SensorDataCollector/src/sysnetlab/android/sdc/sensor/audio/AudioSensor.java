
package sysnetlab.android.sdc.sensor.audio;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.media.AudioFormat;
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
        
        Log.d("SensorDataCollector", "getValidRecordingParameters(): " +
                "new AudioRecord(source, rate, channl, encoding, buffersize) = " +
                "new AudioRecord(" + mAudioRecordParameter.getSource().getSourceId() + ", " +
                mAudioRecordParameter.getSamplingRate() + ", " + 
                mAudioRecordParameter.getChannel().getChannelId() + ", " + 
                mAudioRecordParameter.getEncoding().getEncodingId() + ", " + 
                mAudioRecordParameter.getBufferSize() + ")");
        
        if (!passedSanitationCheck()) {
            throw new RuntimeException(
                    "AudioRecord in SensorDataCollector is not properly released or initialized.");
        }
        
        mDateStart = Calendar.getInstance().getTime();
        mExperimentTimeStart = new ExperimentTime();

        BlockingQueue<DataBuffer> audioDataQueue = new LinkedBlockingQueue<DataBuffer>();
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
    
    private boolean passedSanitationCheck() {
        if (mAudioRecord == null)
            return false;
        if (mAudioRecordParameter == null)
            return false;

        if (mAudioRecord.getState() != AudioRecord.STATE_INITIALIZED)
            return false;

        if (mAudioRecord.getAudioFormat() != mAudioRecordParameter.getEncoding().getEncodingId())
            return false;

        if (mAudioRecord.getAudioSource() != mAudioRecordParameter.getSource().getSourceId())
            return false;

        if (mAudioRecord.getSampleRate() != mAudioRecordParameter.getSamplingRate())
            return false;

        // how about source?
        return true;
    }
    
    private class DataBuffer {
        private boolean mPoisonPill;
        private ByteBuffer mByteBuffer;   
        
        DataBuffer(boolean pill, ByteBuffer buf) {
            mPoisonPill = pill;
            mByteBuffer = buf;
        }
        
        boolean isPoisonPill() {
            return mPoisonPill;
        }
        
        ByteBuffer getBuffer() {
            return mByteBuffer;
        }
    }
    
    private class Producer implements Runnable {
        private final BlockingQueue<DataBuffer> mQueue;
        
        Producer(BlockingQueue<DataBuffer> q) {
            mQueue = q; 
        }
        
        public void run() {
            initializeAudioData(mChannel, mAudioRecord);

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
                        mQueue.put(new DataBuffer(false, b));
                    }
                }
                
                mQueue.put(new DataBuffer(true, null));
            } catch (InterruptedException e) {
                Log.d("SensorDataCollector",
                        "AudioSensor::Producer::run(): interrupted: " + e.toString());
            }
        }
        
        ByteBuffer produce() {
            return recordToByteBuffer(mAudioRecordParameter.getMinBufferSize());
        }
    }
    
    private class Consumer implements Runnable {
        private final BlockingQueue<DataBuffer> mQueue;
        private int mNumberOfBytes;

        Consumer(BlockingQueue<DataBuffer> q) {
            mQueue = q;
            mNumberOfBytes = 0;
        }

        public void run() {
            try {
                while (mIsRecording) {
                    DataBuffer buf = mQueue.take();
                    if (!buf.isPoisonPill()) {
                        consume(mChannel, buf.getBuffer());
                    } else {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Log.i("SensorDataCollector", "AudioSensor::Consumer::run(): " + e.toString());
            }
            finalizeAudioData(mChannel, mAudioRecord, mNumberOfBytes);
        }

        void consume(AbstractStore.Channel c, ByteBuffer b) {
            int n = processBufferedAudioData(c, b);
            mNumberOfBytes += n;
        }
    }
    
    private ByteBuffer recordToByteBuffer(int capacity) {
        // Log.d("SensorDataCollector",
        //        "AudioSensor::recordToByteBuffer(): entered with capacity = " + capacity);

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
                Log.d("SensorDataCollector",
                        "AudioSensor::recordToByteBuffer(): AudioRecord.ERROR_INVALID_OPERATION");
                buf = null;
                break;
            } else if (numBytesRead == AudioRecord.ERROR_BAD_VALUE) {
                Log.d("SensorDataCollector",
                        "AudioSensor::recordToByteBuffer(): AudioRecord.ERROR_BAD_VALUE");
                buf = null;
                break;
            } else if (numBytesRead == AudioRecord.ERROR) {
                Log.d("SensorDataCollector", "AudioSensor::recordToByteBuffer(): AudioRecord.ERROR");
                buf = null;
                break;
            }
        }
        
        //Log.d("SensorDataCollector",
        //  "AudioSensor::recordToByteBuffer():buf = " + buf);        
        return buf;
    }
    
    private int processBufferedAudioData(AbstractStore.Channel c, ByteBuffer bb) {
        int sizeInBytes = bb.capacity();
        /*
        Log.d("SensorDataCollector",
                "AudioSensor::processBufferedAudioData():sizeInBytes = " + sizeInBytes);
                */
        
        byte[] b = new byte[sizeInBytes];
        bb.clear();
        bb.get(b);
        
        c.write(b, 0, sizeInBytes);
        
        return sizeInBytes;
    }
    
    private void initializeAudioData(AbstractStore.Channel c, AudioRecord r) {
        Log.d("SensorDataCollector", "AudioSensor::initializeAudioData(): start audio record");
        
        Log.d("SensorDataCollector", "initializeAudioData(): " +
                "new AudioRecord(source, rate, channl, encoding, buffersize) = " +
                "new AudioRecord(" + mAudioRecordParameter.getSource().getSourceId() + ", " +
                r.getSampleRate() + ", " + 
                "[ count = " + r.getChannelCount() + " config = " + r.getChannelConfiguration() + "], " + 
                "[ format = " + r.getAudioFormat() + " format = " +
                (r.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT ? "ENCODING_PCM_16BIT" : "ENCODING_PCM_8BIT") + "], " + 
                mAudioRecordParameter.getBufferSize() + ")");
        
        r.startRecording();
        
        if (c.getType() == Channel.CHANNEL_TYPE_WAV) {
            WaveHeader header = new WaveHeader((short)0, 0, (short)0, 0L);
            c.write(header.getHeader(), 0, header.getLength());
            c.setDeferredClosing(true);
        }
    }
    
    private void finalizeAudioData(AbstractStore.Channel c, AudioRecord r, int numberOfBytes) {
        Log.d("SensorDataCollector", "entering finalizeAudioData() ...");
        if (c.getType() == Channel.CHANNEL_TYPE_WAV) {
            short bitsPerSample;
            if (r.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT) {
                bitsPerSample = 16;
            } else if (r.getAudioFormat() == AudioFormat.ENCODING_PCM_8BIT) {
                bitsPerSample = 8;
            } else {
                bitsPerSample = 16;
            }
            long numberOfSamples;
            numberOfSamples = numberOfBytes / (bitsPerSample / 8); 
            WaveHeader header = new WaveHeader((short)r.getChannelCount(), r.getSampleRate(), bitsPerSample, numberOfSamples);
            c.write(header.getHeader(), 0, header.getLength(), 0);
            c.setReadyToClose();
        }
        r.stop();
        r.release();
        r = null; /*
                   * set null according to documentation at
                   * http://developer.android
                   * .com/reference/android/media/AudioRecord.html#release%28%29
                   */
    }
}
