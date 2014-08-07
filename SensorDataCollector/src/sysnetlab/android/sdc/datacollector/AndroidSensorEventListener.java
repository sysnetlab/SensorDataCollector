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

package sysnetlab.android.sdc.datacollector;

import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

public class AndroidSensorEventListener implements SensorEventListener {
    private Channel mChannel;
    private Experiment mExperiment;

    public AndroidSensorEventListener(Channel channel) {
        mChannel = channel;
        mExperiment = ExperimentManagerSingleton.getInstance().getActiveExperiment();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @SuppressLint("NewApi")
    public void onSensorChanged(SensorEvent event) {
        Log.i("SensorDataCollector", "onSensorChanged(): get a data point from a sensor.");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mChannel.write(
                    SystemClock.currentThreadTimeMillis() + ", "
                            + SystemClock.elapsedRealtime() + ", "
                            + SystemClock.elapsedRealtimeNanos());
        } else {
            mChannel.write(SystemClock.currentThreadTimeMillis()
                    + ", " + SystemClock.elapsedRealtime());
        }
        for (int i = 0; i < event.values.length; i++) {
            mChannel.write(", " + event.values[i]);
        }
        
        TaggingAction lastTaggingAction = mExperiment.getLastTagging();
        if(lastTaggingAction != null && lastTaggingAction.getTagState()==TaggingState.TAG_ON){
        	mChannel.write(Integer.toString(lastTaggingAction.getTag().getTagId()));
        }else{
        	mChannel.write("0");
        }
        mChannel.write("\n");
    }
    
    public Channel getChannel() {
        return mChannel;
    }
        
}
