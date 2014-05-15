
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sysnetlab.android.sdc.datacollector.DeviceInformation;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.datacollector.Note;
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
    public void addExperiment() throws RuntimeException {
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
    public List<Experiment> listExperiments() {
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
    public boolean writeExperimentMetaData(Experiment experiment) {
        String configFilePath = mNewExperimentPath + "/.experiment";
        PrintStream out;
        try {
            out = new PrintStream(new BufferedOutputStream(
                    new FileOutputStream(configFilePath)));

            out.println(experiment.getName());
            out.println(experiment.getDateTimeCreated());
            out.println(experiment.getDateTimeDone());

            out.println(experiment.getTags().size());
            for (Tag tag : experiment.getTags()) {
                out.println(tag.getName());
                out.println(tag.getShortDescription());
                out.println(tag.getLongDescription());
            }

            out.println(experiment.getNotes().size());
            for (Note note : experiment.getNotes()) {
                out.println(note.getDateTime());
                out.println(note.getNote());
            }

            out.println(experiment.getDeviceInformation().getManufacturer());
            out.println(experiment.getDeviceInformation().getModel());

            out.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("SensorDataCollector",
                    "SimpleFileStore::writeExperimentMetaData(): failed to write to " +
                            configFilePath);
            e.printStackTrace();
            return false;
        }
    }

    private Experiment loadExperiment(String dirName, String parentDir) {
        String configFilePath = parentDir + "/.experiment";
        String name = null, dateTimeCreated = null;

        try {
            BufferedReader in;
            File file;

            file = new File(configFilePath);

            if (file.exists()) {
                String dateTimeDone;

                in = new BufferedReader(new FileReader(configFilePath));

                name = in.readLine();
                dateTimeCreated = in.readLine();

                Experiment experiment = new Experiment(name, dateTimeCreated);

                dateTimeDone = in.readLine();
                experiment.setDateTimeDone(dateTimeDone);

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
                }

                n = Integer.parseInt(in.readLine());
                if (n > 0) {
                    ArrayList<Note> notes = new ArrayList<Note>();

                    for (int i = 0; i < n; i++) {
                        String dateTime = in.readLine();
                        String noteText = in.readLine();

                        Note note = new Note(noteText, dateTime);
                        notes.add(note);
                    }
                }

                String make = in.readLine();
                String model = in.readLine();
                DeviceInformation deviceInfo = new DeviceInformation(make, model);

                experiment.setDeviceInformation(deviceInfo);

                in.close();

                return experiment;
            } else {
                file = new File(parentDir);

                dateTimeCreated = SimpleDateFormat.getDateTimeInstance().format(
                        (new Date(file.lastModified())));
                name = dirName;

                Log.w("SensorDataCollector",
                        "SimpleFileStore::loadExperiment(): no configuraiton file is found. treated it as an old experiment.");

                return new Experiment(name, dateTimeCreated);
            }
        } catch (NumberFormatException e) {
            if (name != null && dateTimeCreated != null) {
                Log.w("SensorDataCollector",
                        "SimpleFileStore::loadExperiment(): Found an old configuration file.");
                return new Experiment(name, dateTimeCreated);
            }

            Log.e("SensorDataCollector", "SimpleFileStore::loadExperiment(): " +
                    "Failed to load the configuration file.");
            e.printStackTrace();

            return null;     
            
        } catch (IOException e) {

            if (name != null && dateTimeCreated != null) {
                Log.w("SensorDataCollector",
                        "SimpleFileStore::loadExperiment(): Found an old configuration file.");
                return new Experiment(name, dateTimeCreated);
            }

            Log.e("SensorDataCollector", "SimpleFileStore::loadExperiment(): " +
                    "Failed to load the configuration file.");
            e.printStackTrace();

            return null;
        }
    }

    public class SimpleFileChannel extends AbstractStore.Channel {
        PrintStream mOut;

        // BufferedReader mIn;

        protected SimpleFileChannel() {
            // prohibit from creating SimpleFileChannel object with an argument
        }

        public SimpleFileChannel(String path) throws FileNotFoundException {
            mOut = new PrintStream(new BufferedOutputStream(
                    new FileOutputStream(path)));
        }

        public void close() {
            mOut.close();
        }

        @Override
        public void write(String s) {
            mOut.print(s);
        }

        @Override
        public void open() {
        }

    }

    @Override
    public Channel getChannel(String tag) {

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
    public Channel getChannel() {
        return getChannel(null);
    }

    @Override
    public void closeAllChannels() {
        for (Channel channel : mChannels)
            channel.close();
        // create new channel list and garbage-collect the old channel list
        mChannels = new ArrayList<Channel>();
    }

    public String getNewExperimentPath() {
        return mNewExperimentPath;
    }
}
