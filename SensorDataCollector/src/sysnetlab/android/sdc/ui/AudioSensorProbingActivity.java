package sysnetlab.android.sdc.ui;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.sensor.audio.AudioRecordParameter;
import sysnetlab.android.sdc.sensor.audio.AudioRecordSettingDataSource;
import android.app.Activity;
import android.content.Context;
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
        
        AudioSensorProbingTask task = new AudioSensorProbingTask(this);
        task.execute();        
    }

    private class AudioSensorProbingTask extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        
        AudioSensorProbingTask(Context context) {
            mContext = context;
        }
        
        @Override
        protected Void doInBackground(Void... v) {
            AudioRecordSettingDataSource.initializeInstance(mContext);
            AudioRecordSettingDataSource dbSource = AudioRecordSettingDataSource.getInstance();
            dbSource.open();
            if (!dbSource.isDataSourceReady()) {
                dbSource.prepareDataSource();
            } else {
                List<AudioRecordParameter> listParams = dbSource.getAllAudioRecordParameters();
                if (listParams == null || listParams.isEmpty()) {
                    dbSource.prepareDataSource();
                }
            }
            
            dbSource.close();
            return null;  
        }

        @Override
        protected void onPreExecute() {
            LinearLayout layoutProgress = (LinearLayout) findViewById(R.id.layout_progressbar_loading);
            if (layoutProgress != null)
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
