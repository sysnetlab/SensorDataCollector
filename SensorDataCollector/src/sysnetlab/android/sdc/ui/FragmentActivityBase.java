
package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class FragmentActivityBase extends ActionBarActivity {

    protected TaskLoadingSpinner mLoadingTask;
    protected ProgressBar mProgressBar;

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
            mProgressBar = (ProgressBar) findViewById(R.id.progresswheel_task_in_progress);
            if (layoutProgress != null)
                layoutProgress.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void result) {
            RelativeLayout layoutProgress = (RelativeLayout) findViewById(R.id.layout_progressbar_loading);
            if (layoutProgress != null)
                layoutProgress.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sensordatacolletor_menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.action_about:
			showAboutDialog();
			break;

		default:
			break;
		}
    	return super.onOptionsItemSelected(item);
    }

	private void showAboutDialog() {
		String textTitle = getResources().getString(R.string.text_title_about);
		String textDescription = getResources().getString(R.string.text_about_description);
		//String textAuthors = getResources().getString(R.string.text_about_authors);
		//String textVersion = getResources().getString(R.string.text_about_version);
				
		try {
			
			textDescription = String.format(textDescription,
						getPackageManager()
						.getPackageInfo(getPackageName(), 0)
						.versionName);
						
		} catch (NameNotFoundException e) {
			Log.e("about", "Failure when trying to get the app version: "+e.getMessage());
			//textVersion="Not Available";
		}
		
		TextView textViewAuthors=new TextView(this);
		textViewAuthors.setText(textDescription);
		textViewAuthors.setGravity(Gravity.CENTER_HORIZONTAL);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {                        
                    	
                    }
                });	        
        builder.setNegativeButton(R.string.text_about_webpage,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	Intent viewIntent =
						new Intent("android.intent.action.VIEW",
						Uri.parse("https://github.com/sysnetlab/SensorDataCollector"));
                		startActivity(viewIntent);
                    }
                });

        builder.setTitle(textTitle)
        		.setView(textViewAuthors)
        		.setCancelable(true)
        		.create();
        
        builder.create().show();
	}
}
