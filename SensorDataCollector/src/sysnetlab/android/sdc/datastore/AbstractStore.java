
package sysnetlab.android.sdc.datastore;

import java.io.IOException;
import java.util.List;

import sysnetlab.android.sdc.datacollector.Experiment;

public abstract class AbstractStore {
    protected List<Channel> mChannels;

    public abstract class Channel {
        public final static int READ_ONLY = 0x0001;
        public final static int WRITE_ONLY = 0x0002;        
        
        public abstract void open();

        public abstract void write(String s);
        
        public abstract String read();
        
        public abstract void reset();

        public abstract void close();

        /**
         * @return the channel description sufficiently to reconstruct a channel
         *         to read the data
         */
        public abstract String describe();
    };

    public abstract void addExperiment();

    public abstract boolean writeExperimentMetaData(Experiment experiment);

    public abstract List<Experiment> listExperiments();

    public abstract Channel getChannel();

    public abstract Channel getChannel(String tag);

    public abstract void closeAllChannels() throws IOException;
}
