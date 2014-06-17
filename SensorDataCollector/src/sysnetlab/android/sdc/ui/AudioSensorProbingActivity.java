package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AudioSensorProbingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        AudioSensorProbingTask task = new AudioSensorProbingTask();
        task.execute();        
    }

    private class AudioSensorProbingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... v) {
            for (AbstractSensor sensor : SensorDiscoverer.discoverSensorList(AudioSensorProbingActivity.this)) {
                sensor.setSelected(false);
            }
            return null;  
        }

        @Override
        protected void onPreExecute() {
            LinearLayout layoutProgress = (LinearLayout) findViewById(R.id.layout_progressbar_loading);
            // if (layoutProgress != null)
                layoutProgress.setVisibility(View.VISIBLE);            
        }

        @Override
        protected void onPostExecute(Void result) {
          LinearLayout layoutProgress = (LinearLayout) findViewById(R.id.layout_progressbar_loading);
          if (layoutProgress != null)
              layoutProgress.setVisibility(View.GONE);
          
          Intent intent = new Intent(AudioSensorProbingActivity.this, SensorDataCollectorActivity.class);
          startActivity(intent);          
        }
    }     

}
