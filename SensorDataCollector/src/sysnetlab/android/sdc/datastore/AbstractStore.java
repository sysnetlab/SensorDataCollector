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

import java.util.List;
import java.util.concurrent.BlockingQueue;

import android.widget.ProgressBar;
import sysnetlab.android.sdc.datacollector.Experiment;

public abstract class AbstractStore {
	
    protected List<Channel> mChannels;

    public abstract class Channel {
        public static final int READ_ONLY = 0x0001;
        public static final int WRITE_ONLY = 0x0002;  
        
        public static final int CHANNEL_TYPE_CSV = 0x0001;
        public static final int CHANNEL_TYPE_BIN = 0x0002;
        public static final int CHANNEL_TYPE_PCM = 0x0004;
        public static final int CHANNEL_TYPE_WAV = 0x0008;
        
        protected boolean mDeferredClosing;
        protected BlockingQueue<Integer> mBlockingQueue;
        
        public abstract boolean open();
        public abstract void write(String s);
        public abstract void write(byte[] buffer, int offset, int length);
        public abstract void write(byte[] buffer, int bufferOffset, int bufferLength, int fileOffset);
        public abstract String read();
        public abstract void reset();
        public abstract void close();
        
        public abstract int getType();

        /**
         * @return the channel description sufficiently to reconstruct a channel
         *         to read the data
         */
        public abstract String describe();
        public abstract void setDeferredClosing(boolean defferedClosing);
        public abstract void setReadyToClose();
    };

    public abstract void setupNewExperimentStorage(Experiment experiment);
    public abstract void writeExperimentMetaData(Experiment experiment);
    public abstract List<Experiment> listStoredExperiments();
    public abstract List<Experiment> listStoredExperiments(ProgressBar mProgressBar);
    public abstract int getCountExperiments();
    public abstract String getNewExperimentPath();
    
    public abstract Channel createChannel(String tag, int operationFlags, int channelType);
    public abstract void closeAllChannels();
	
}
