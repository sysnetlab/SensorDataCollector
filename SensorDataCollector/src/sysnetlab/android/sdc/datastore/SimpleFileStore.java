
package sysnetlab.android.sdc.datastore;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sysnetlab.android.sdc.datacollector.DeviceInformation;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.datacollector.TaggingAction;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.SensorUtilSingleton;
import android.os.Environment;
import android.util.Log;

/**
 * Assumptions The application allows a single experiment being run. No two
 * experiments are allowed to run concurrently.
 */
public class SimpleFileStore extends AbstractStore {
    private final String DIR_PREFIX = "exp";
    private final String DEFAULT_DATAFILE_PREFIX = "sdc";
    private String mParentPath;
    private String mNewExperimentPath;

    private int mNextExperimentNumber;
    private int mNextChannelNumber;

    public SimpleFileStore() throws RuntimeException {

        Log.i("SensorDataCollector",
                "entering SimpleFileStore.constructor() ...");

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
                out.println(sensor.getListener().getChannel().describe());
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

    private Experiment loadExperiment(String dirName, String parentDir) {
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
                experiment.setName(name);
                experiment.setDateTimeCreatedFromString(dateTimeCreated);
                experiment.setDateTimeDoneFromString(dateTimeDone);

                int n;
                n = Integer.parseInt(in.readLine());
                if (n > 0) {
                    ArrayList<Tag> tags = new ArrayList<Tag>();

                    for (int i = 0; i < n; i++) {
                        String tagName = in.readLine();
                        String tagShortDesc = in.readLine();
                        String tagLongDesc = in.readLine();

                        Tag tag = new Tag(tagName, tagShortDesc, tagLongDesc);
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
                    String channelDescriptor = in.readLine();
                    // TODO make sure channel is read-only
                    Channel channel = new SimpleFileChannel(channelDescriptor, Channel.READ_ONLY);
                    AbstractSensor sensor = SensorUtilSingleton.getInstance().getSensor(sensorName,
                            sensorMajorType,
                            sensorMinorType, channel);
                    lstSensors.add(sensor);
                }
                experiment.setSensors(lstSensors);
                
                in.close();

                Log.i("SensorDataCollector",
                        "SimpleFileStore::loadExperiment(): load experiment("
                                + experiment.getName() + ", " + experiment.getDateTimeCreatedAsString()
                                + ") successfully.");
                return experiment;
            } else {
                file = new File(parentDir);

                Date dateCreated = new Date(file.lastModified());
                name = dirName;

                Log.w("SensorDataCollector",
                        "SimpleFileStore::loadExperiment(): no configuraiton file is found for " + name);

                return new Experiment(name, dateCreated);
            }
        } catch (NumberFormatException e) {
            if (name != null && dateTimeCreated != null) {
                Log.w("SensorDataCollector",
                        "SimpleFileStore::loadExperiment(): Found an old configuration file for " + name);
                return new Experiment(name);
            }

            Log.e("SensorDataCollector", "SimpleFileStore::loadExperiment(): " +
                    "Failed to load the configuration file.");
            e.printStackTrace();

            return null;

        } catch (IOException e) {

            if (name != null && dateTimeCreated != null) {
                Log.w("SensorDataCollector",
                        "SimpleFileStore::loadExperiment(): Found an old configuration file for " + name);
                return new Experiment(name);
            }

            Log.e("SensorDataCollector", "SimpleFileStore::loadExperiment(): " +
                    "Failed to load the configuration file.");
            e.printStackTrace();

            return null;
        } catch (RuntimeException e) {
            Log.w("SensorDataCollector",
                    "SimpleFileStore::loadExperiment(): Found an old configuration file "
                            + experiment.getName() + ", " + experiment.getDateTimeCreatedAsString());
            return experiment;
        }
    }

    public class SimpleFileChannel extends AbstractStore.Channel {
        private PrintStream mOut;
        private BufferedReader mIn;
        private String mPath;

        protected SimpleFileChannel() {
            // prohibit from creating SimpleFileChannel object without an argument
        }

        public SimpleFileChannel(String path) throws FileNotFoundException {
            this(path, WRITE_ONLY);
        }
        
        public SimpleFileChannel(String path, int flags) throws FileNotFoundException {
            if (flags == READ_ONLY) {
                mOut = null;
                File file = new File(path);
                if (file.exists()) {
                    mIn = new BufferedReader(new FileReader(path));
                } else {
                    throw new RuntimeException("SimpleFileChannel: cannot open file " + path);
                }
                mPath = path;
            } else if (flags == WRITE_ONLY) {
                mOut = new PrintStream(new BufferedOutputStream(
                        new FileOutputStream(path)));
                mIn = null;
                mPath = path;
            } else {
                throw new RuntimeException(
                        "SimpleFileChannel: encountered unsupported creation flag " + flags);
            }
        }

        public void close() {
            if (mOut != null) mOut.close();
            if (mIn != null) {
                try {
                    mIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("SensorDataCollector", "close error in SimpleFileStore::close().");
                }
            }
        }

        @Override
        public void write(String s) {
            mOut.print(s);
        }
        
        @Override
        public String read() {
            if (mIn != null) {
                try {
                    return mIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("SensorDataCollector", "read error in SimpleFileStore::read().");
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
                    Log.i("SensorDataCollector", "reset error in SimpleFileStore::mark().");              
                }
            }
        }        

        @Override
        public void open() {
        }

        public String describe() {
            // return the value passed to the constructor
            return mPath;
        }
    }

    @Override
    public Channel createChannel(String tag) {

        String path;

        if (tag == null || tag.trim().length() == 0) {
            DecimalFormat f = new DecimalFormat("00000");
            path = mNewExperimentPath + "/" + DEFAULT_DATAFILE_PREFIX
                    + f.format(mNextChannelNumber) + ".txt";
            mNextChannelNumber++;
        } else {
            path = mNewExperimentPath + "/" + tag.replace(' ', '_') + ".txt";
        }

        try {
            Channel channel;
            channel = new SimpleFileChannel(path);
            mChannels.add(channel);

            return channel;
        } catch (FileNotFoundException e) {
            Log.e("SensorDataCollector",
                    "SimpleFileStore::getChannel: cannot open the channel file: "
                            + path);
            e.printStackTrace();

            return null;
        }
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

}
