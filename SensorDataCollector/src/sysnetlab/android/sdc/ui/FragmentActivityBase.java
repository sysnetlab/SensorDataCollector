package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.RelativeLayout;

public abstract class FragmentActivityBase extends ActionBarActivity {
	
	protected TaskLoadingSpinner mLoadingTask;
	
	protected abstract void loadTask();
	
	protected class TaskLoadingSpinner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... v) {
            loadTask();
            return null;
        }

        @Override
        protected void onPreExecute() {
        	RelativeLayout layoutProgress = (RelativeLayout) findViewById(R.id.layout_progressbar_loading);
            if (layoutProgress != null)
                layoutProgress.setVisibility(View.VISIBLE);
        }
        
        @Override
        protected void onPostExecute(Void result) {
        	RelativeLayout layoutProgress = (RelativeLayout) findViewById(R.id.layout_progressbar_loading);
          if (layoutProgress != null)
              layoutProgress.setVisibility(View.GONE);
        }
	}
}
