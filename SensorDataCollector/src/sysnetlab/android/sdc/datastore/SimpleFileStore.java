/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package sysnetlab.android.sdc.datastore;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import sysnetlab.android.sdc.datacollector.DeviceInformation;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.datacollector.TaggingAction;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Assumptions The application allows a single experiment being run. No two
 * experiments are allowed to run concurrently.
 */
public class SimpleFileStore extends AbstractStore {
    protected final String DIR_PREFIX = "exp";
    protected final String DEFAULT_DATAFILE_PREFIX = "sdc";
    protected String mParentPath;
    protected String mNewExperimentPath;

    protected int mNextExperimentNumber;
    private int mNextChannelNumber;

    public SimpleFileStore() throws RuntimeException {

        Log.d("SensorDataCollector", "Entered SimpleFileStore.constructor().");

        String parentPath = Environment.getExternalStorageDirectory().getPath();
        parentPath = parentPath + "/SensorData";
        File dataDir = new File(parentPath);
        if (!dataDir.exists() && !dataDir.mkdir()) {
            throw new RuntimeException(
                    "SimpleFileStore::SimpleFileStore(): failed to create directory "
                            + parentPath);
        }
        mParentPath = parentPath;

        String pathPrefix = mParentPath + "/" + DIR_PREFIX;
        DecimalFormat f = new DecimalFormat("00000");
        String path;

        mNextExperimentNumber = 0;

        do {
            mNextExperimentNumber++;
            path = pathPrefix + f.format(mNextExperimentNumber);
            dataDir = new File(path);
        } while (dataDir.exists());
    }

    @Override
    public void setupNewExperimentStorage(Experiment experiment) throws RuntimeException {
        DecimalFormat f = new DecimalFormat("00000");
        mNewExperimentPath = mParentPath + "/" + DIR_PREFIX
                + f.format(mNextExperimentNumber);

        Log.d("SensorDataCollector", "SimpleFileStore::setupNewExperimentStorage(): " + mNewExperimentPath);
        
        File dir = new File(mNewExperimentPath);
        if (!dir.mkdir()) {
            throw new RuntimeException(
                    "SimpleFileStore::addExperiment(): failed to create directory "
                            + mNewExperimentPath);
        }
        mNextExperimentNumber++;
        mNextChannelNumber = 1;
        mChannels = new ArrayList<Channel>();
    }

    @Override
    public List<Experiment> listStoredExperiments() {
        List<Experiment> listExperiments = new ArrayList<Experiment>();

        DecimalFormat f = new DecimalFormat("00000");
        for (int i = 1; i < mNextExperimentNumber; i++) {
            String dirName = DIR_PREFIX + f.format(i);
            String pathPrefix = mParentPath + "/" + dirName;

            Experiment experiment = loadExperiment(dirName, pathPrefix);
            if (experiment != null)
                listExperiments.add(experiment);
        }
        return listExperiments;
    }

    @Override
    public void writeExperimentMetaData(Experiment experiment) {
        String configFilePath = mNewExperimentPath + "/.experiment";
        PrintStream out;
        try {
            out = new PrintStream(new BufferedOutputStream(
                    new FileOutputStream(configFilePath)));
            
            out.println(experiment.getName());
            out.println(experiment.getDateTimeCreatedAsString());
            out.println(experiment.getDateTimeDoneAsString());

            out.println(experiment.getTags().size());
            for (Tag tag : experiment.getTags()) {
            	out.println(tag.getTagId());
                out.println(tag.getName());
                out.println(tag.getShortDescription());
                out.println(tag.getLongDescription());
            }

            out.println(experiment.getNotes().size());
            for (Note note : experiment.getNotes()) {
                out.println(note.getDateCreatedAsString());
                out.println(note.getNote());
            }

            out.println(experiment.getDeviceInformation().getManufacturer());
            out.println(experiment.getDeviceInformation().getModel());

            out.println(experiment.getSensors().size());
            for (AbstractSensor sensor : experiment.getSensors()) {
                out.println(sensor.getName());
                out.println(sensor.getMajorType());
                out.println(sensor.getMinorType());
                switch(sensor.getMajorType()) {
                    case AbstractSensor.ANDROID_SENSOR:
                        out.println(((AndroidSensor) sensor).getListener().getChannel().describe());
                        break;
                }
            }
            
            out.println(experiment.getTaggingActions().size());
            for (TaggingAction taggingAction : experiment.getTaggingActions()) {
                out.println(taggingAction.toString());
            }

            out.close();
        } catch (FileNotFoundException e) {
            Log.e("SensorDataCollector",
                    "SimpleFileStore::writeExperimentMetaData(): failed to write to " +
                            configFilePath);
            e.printStackTrace();
        }
    }

    protected Experiment loadExperiment(String dirName, String parentDir) {
        String configFilePath = parentDir + "/.experiment";
        String name = null, dateTimeCreated = null;
        Experiment experiment = null;

        try {
            BufferedReader in;
            File file;

            file = new File(configFilePath);

            if (file.exists()) {
                String dateTimeDone;

                in = new BufferedReader(new FileReader(configFilePath));

                name = in.readLine();
                dateTimeCreated = in.readLine();
                dateTimeDone = in.readLine();
                
                experiment = new Experiment();
                experiment.setPath(parentDir);
                experiment.setName(name);
                experiment.setDateTimeCreatedFromString(dateTimeCreated);
                experiment.setDateTimeDoneFromString(dateTimeDone);

                int n;
                n = Integer.parseInt(in.readLine());
                if (n > 0) {
                    ArrayList<Tag> tags = new ArrayList<Tag>();

                    for (int i = 0; i < n; i++) {
                    	String tagId = in.readLine();
                        String tagName = in.readLine();
                        String tagShortDesc = in.readLine();
                        String tagLongDesc = in.readLine();
                        Tag tag = new Tag(tagName, tagShortDesc, tagLongDesc,Integer.parseInt(tagId));
                        tags.add(tag);
                    }
                    experiment.setTags(tags);
                }


                n = Integer.parseInt(in.readLine());
                if (n > 0) {
                    ArrayList<Note> notes = new ArrayList<Note>();

                    for (int i = 0; i < n; i++) {
                        String dateTime = in.readLine();
                        String noteText = in.readLine();

                        Note note = new Note(noteText);
                        note.setDateCreatedFromString(dateTime);
                        notes.add(note);
                    }
                    
                    experiment.setNotes(notes);
                }

                String make = in.readLine();
                String model = in.readLine();
                DeviceInformation deviceInfo = new DeviceInformation(make, model);

                experiment.setDeviceInformation(deviceInfo);

                ArrayList<AbstractSensor> lstSensors = new ArrayList<AbstractSensor>();
                int numSensors = Integer.parseInt(in.readLine());
                for (int i = 0; i < numSensors; i++) {
                    String sensorName = in.readLine();
                    int sensorMajorType = Integer.parseInt(in.readLine());
                    int sensorMinorType = Integer.parseInt(in.readLine());
                    String channelDescriptor = "";
                    switch (sensorMajorType) {
                        case AbstractSensor.ANDROID_SENSOR:
                            channelDescriptor = in.readLine();

                            Channel channel = new SimpleFileChannel(channelDescriptor,
                                    Channel.READ_ONLY);
                            AbstractSensor sensor = SensorDiscoverer.constructSensorObject(
                                    sensorName,
                                    sensorMajorType,
                                    sensorMinorType, channel, null);
                            lstSensors.add(sensor);
                            break;
                        default:
                            Log.w("SensorDataCollector",
                                    "SimpleFileStore::loadExperiment(): do not handle sensor major type "
                                            + sensorMajorType);
                    }
                }
                experiment.setSensors(lstSensors);
                
                in.close();
            } else {
                file = new File(parentDir);

                Date dateCreated = new Date(file.lastModified());
                name = dirName;

                Log.w("SensorDataCollector",
                        "SimpleFileStore::loadExperiment(): no configuraiton file is found for " + name);
                experiment = new Experiment(name, dateCreated);
                experiment.setPath(parentDir);
            }
        } catch (NumberFormatException e) {
            Log.w("SensorDataCollector",
                    "SimpleFileStore::loadExperiment(): failed to parse an old configuration file for "
                            + name + " with error " + e.toString());
            experiment = null;
        } catch (IOException e) {
            Log.w("SensorDataCollector", "SimpleFileStore::loadExperiment(): " +
                    "failed to read an old configuration file with error message " + e.toString());
            experiment = null;
        } catch (RuntimeException e) {
            Log.w("SensorDataCollector",
                    "SimpleFileStore::loadExperiment(): found an old configuration file "
                            + experiment.getName() + ", " + experiment.getDateTimeCreatedAsString());
        }

        Log.d("SensorDataCollector", "SimpleFileStore::loadExperiment(): loaded experiment ["
                + (experiment != null ? experiment.toString() : "null") + "]");
        return experiment;
    }

    public class SimpleFileChannel extends AbstractStore.Channel {
        private PrintStream mOut;
        private BufferedReader mIn;
        private RandomAccessFile mRandomAccessFile;
        private String mPath;
        private int mType;
        private int mFlags;

        protected SimpleFileChannel() {
            // prohibit from creating SimpleFileChannel object without an argument
        }

        public SimpleFileChannel(String path) throws FileNotFoundException {
            this(path, WRITE_ONLY, CHANNEL_TYPE_CSV);
        }
        
        public SimpleFileChannel(String path, int flags) throws FileNotFoundException {
            this(path, flags, CHANNEL_TYPE_CSV);
        }        
        
        public SimpleFileChannel(String path, int flags, int type) {
            mFlags = flags;
            mType = type;
            mPath = path;
            mBlockingQueue = null;
            mDeferredClosing = false;
        } 

        @Override
        public boolean open() {
            try {
                if (mFlags == READ_ONLY) {
                    mRandomAccessFile = null;
                    mOut = null;
                    File file = new File(mPath);
                    if (file.exists()) {
                        mIn = new BufferedReader(new FileReader(mPath));
                    } else {
                        Log.e("SensorDataCollector", "SimpleFileChannel::open(): "
                                + "error on open openning the channel: cannot find the file: " + mPath);
                        return false;
                    }
                } else if (mFlags == WRITE_ONLY) {
                    mIn = null;

                    if (mType == AbstractStore.Channel.CHANNEL_TYPE_WAV) {
                        mRandomAccessFile = new RandomAccessFile(mPath, "rw");
                        try {
                            mOut = new PrintStream(new BufferedOutputStream(new FileOutputStream(
                                    mRandomAccessFile.getFD())));
                        } catch (IOException e) {
                            mOut = null;
                            mRandomAccessFile = null;
                            Log.e("SensorDataCollector", "SimpleFileChannel::open(): "
                                    + "error on open openning the channel: " + e.toString());
                            return false;
                        }
                    } else {
                        mOut = new PrintStream(
                                new BufferedOutputStream(new FileOutputStream(mPath)));
                        mRandomAccessFile = null;
                    }
                } else {
                    Log.e("SensorDataCollector", "SimpleFileChannel::open(): "
                            + "encountered unsupported creation flag " + mFlags);
                    return false;
                }
            } catch (FileNotFoundException e) {
                Log.e("SensorDataCollector", "SimpleFileChannel::open(): "
                        + " error on opening the channel: "
                        + e.toString());
                return false;
            }
            
            return true;
        }

        public void close() {
            if (!mDeferredClosing) {
                closeImediately();
            } else {
                closeWhenReady();
            }
        }
        

        @Override
        public void setReadyToClose() {
            if (!mDeferredClosing) return;
            
            if (mDeferredClosing) {
                mBlockingQueue = new LinkedBlockingQueue<Integer>();
                try {
                    mBlockingQueue.put(1);
                } catch (InterruptedException e) {
                    Log.d("SensorDataCollector", "SimpleFileStore::Channel::setDeferredClosing() "
                            + e.toString());
                    mBlockingQueue = null;
                    mDeferredClosing = false;
                }
            }            
        }
        
        @Override
        public void setDeferredClosing(boolean deferredClosing) {
            mDeferredClosing = deferredClosing;
        }        

        private void closeImediately() {
            if (mOut != null) mOut.close();
            if (mIn != null) {
                try {
                    mIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("SensorDataCollector", "SimpleFileStore::close(): mIn.close() failed.");
                }
            }
        }
        
        private void closeWhenReady() {
            new Thread() {
                public void run() {
                    if (mBlockingQueue != null) {
                        try {
                            mBlockingQueue.take();
                        } catch (InterruptedException e) {
                            Log.d("SensorDataCollector",
                                    "SimpleFileStore::Channel::closeWhenReady(): " + e.toString());
                        }
                        closeImediately();
                    }
                }
            }.start();
        }

        @Override
        public void write(String s) {
            mOut.print(s);
        }
        
        @Override
        public void write(byte[] buffer, int offset, int length) {
            mOut.write(buffer, offset, length);
        }
        
        @Override
        public void write(byte[] buffer, int bufferOffset, int bufferLength, int fileOffset) {
            try {
                mRandomAccessFile.seek(0L);
                mRandomAccessFile.write(buffer, bufferOffset, bufferLength);
                mRandomAccessFile.seek(mRandomAccessFile.length());
            } catch (IOException e) {
                throw new RuntimeException("SimpleFileStore::Channel::write(): " + e.toString());
            }
            
        }
        
        @Override
        public String read() {
            if (mIn != null) {
                try {
                    return mIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("SensorDataCollector", "SimpleFileStore::read(): mIn.readLine() failed.");
                    return null;
                }
            } else {
                return null;
            }
        }


        @Override
        public void reset() {
            if (mIn != null) {
                try {
                    mIn.close();
                    mIn = new BufferedReader(new FileReader(mPath));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("SensorDataCollector", "SimpleFileStore::reset(): mIn.close() failed.");              
                }
            }
        }        

        @Override
        public int getType() {
            return mType;
        }

        public String describe() {
            // return the value passed to the constructor
            return mPath;
        }
    }

    @Override
    public Channel createChannel(String tag, int flags, int type) {

        String path, extension;
        
        if (type == Channel.CHANNEL_TYPE_CSV) {
            extension = ".csv";
        } else if (type == Channel.CHANNEL_TYPE_BIN) {
            extension = ".bin";
        } else if (type == Channel.CHANNEL_TYPE_PCM) {
            extension = ".pcm";
        } else if (type == Channel.CHANNEL_TYPE_WAV) {
            extension = ".wav";
        } else {
            extension = ".txt";
        }

        if (tag == null || tag.trim().length() == 0) {
            DecimalFormat f = new DecimalFormat("00000");
            path = mNewExperimentPath + "/" + DEFAULT_DATAFILE_PREFIX
                    + f.format(mNextChannelNumber) + extension;
            mNextChannelNumber++;
        } else {
            path = mNewExperimentPath + "/" + tag.replace(' ', '_') + extension;
        }

        Channel channel;
        channel = new SimpleFileChannel(path, flags, type);
        mChannels.add(channel);

        return channel;
    }

    @Override
    public void closeAllChannels() {
        for (Channel channel : mChannels) {
            channel.close();
        }
        // create new channel list and garbage-collect the old channel list
        mChannels = new ArrayList<Channel>();
    }

    public String getNewExperimentPath() {
        return mNewExperimentPath;
    }
    
    public int getNextExperimentNumber() {
        return mNextExperimentNumber;
    }
    
    public int getNextChannelNumber() {
        return mNextChannelNumber;
    }

	@Override
	public int getCountExperiments() {
		return mNextExperimentNumber;
	}

	@Override
	public List<Experiment> listStoredExperiments(ProgressBar mProgressBar) {
		List<Experiment> listExperiments = new ArrayList<Experiment>();

        DecimalFormat f = new DecimalFormat("00000");
        for (int i = 1; i < mNextExperimentNumber; i++) {
            String dirName = DIR_PREFIX + f.format(i);
            String pathPrefix = mParentPath + "/" + dirName;

            Experiment experiment = loadExperiment(dirName, pathPrefix);
            if (experiment != null)
                listExperiments.add(experiment);
            mProgressBar.setProgress(i);
        }
        return listExperiments;
	}
	
	public String getExperimentPath(){
		return mNewExperimentPath;
	}

}
