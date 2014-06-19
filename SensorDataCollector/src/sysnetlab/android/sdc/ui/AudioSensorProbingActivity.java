package sysnetlab.android.sdc.ui;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.sensor.audio.AudioRecordParameter;
import sysnetlab.android.sdc.sensor.audio.AudioRecordSettingDataSource;
import sysnetlab.android.sdc.sensor.audio.AudioSensorHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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

    public class AudioSensorProbingTask extends AsyncTask<Void, Integer, Void> {
        private Context mContext;

        private ProgressBar mProgressWheel;
        
        AudioSensorProbingTask(Context context) {
            mContext = context;
        }
        
        public void doPublishProgress(int progress) {
            Log.d("SensorDataCollector", "called publishProgress.");
            publishProgress(progress);
        }
        
        @Override
        protected Void doInBackground(Void... v) {
            AudioRecordSettingDataSource.initializeInstance(mContext);
            AudioRecordSettingDataSource dbSource = AudioRecordSettingDataSource.getInstance();
            dbSource.open();
            if (!dbSource.isDataSourceReady()) {
                dbSource.prepareDataSource(this);
            } else {
                List<AudioRecordParameter> listParams = dbSource.getAllAudioRecordParameters();
                if (listParams == null || listParams.isEmpty()) {
                    dbSource.prepareDataSource(this);
                }
            }
            
            dbSource.close();
            return null;  
        }

        @Override
        protected void onPreExecute() {
        	RelativeLayout layoutProgress = (RelativeLayout) findViewById(R.id.layout_progressbar_loading);
            if (layoutProgress != null) {
                mProgressWheel = (ProgressBar) layoutProgress.findViewById(R.id.progressbar_task_in_progress);
                mProgressWheel.setMax(AudioSensorHelper.estimateWorkLoadOnSensorProbing());
                mProgressWheel.setVisibility(View.VISIBLE);
                layoutProgress.setVisibility(View.VISIBLE);     
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("SensorDataCollector", "called setProgress(" + values[0] + ").");
            mProgressWheel.setProgress(values[0]);
        }        
        
        @Override
        protected void onPostExecute(Void result) {
        	RelativeLayout layoutProgress = (RelativeLayout) findViewById(R.id.layout_progressbar_loading);
          if (layoutProgress != null) {
              mProgressWheel.setVisibility(View.GONE);
              layoutProgress.setVisibility(View.GONE);
          }
          
          Intent intent = new Intent(AudioSensorProbingActivity.this, SensorDataCollectorActivity.class);
          startActivity(intent);          
        }
    }     

}
