
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

import sysnetlab.android.sdc.datacollector.Experiment;
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

    private Experiment loadExperiment(String dirName, String parentDir) {
        String configFilePath = parentDir + "/.experiment";
        String name, dt;
        BufferedReader reader;
        File file;

        try {
            file = new File(configFilePath);
            if (file.exists()) {
                reader = new BufferedReader(new FileReader(configFilePath));
                name = reader.readLine();
                dt = reader.readLine();
                reader.close();
            } else {
                file = new File(parentDir);
                dt = SimpleDateFormat.getDateTimeInstance().format(
                        (new Date(file.lastModified())));
                name = dirName;
            }
            return new Experiment(name, dt);
        } catch (IOException e) {
            Log.e("SensorDataCollector",
                    "SimpleFileStore::loadExperiment: failed to load experiment.");
            e.printStackTrace();
            return null;
        }
    }

    public String getNewExperimentPath() {
        return mNewExperimentPath;
    }
}
