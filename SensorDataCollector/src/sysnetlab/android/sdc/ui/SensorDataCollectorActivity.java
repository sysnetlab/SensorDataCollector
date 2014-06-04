
package sysnetlab.android.sdc.ui;

import com.dropbox.sync.android.DbxAccountManager;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.sensor.SensorUtilsSingleton;
import sysnetlab.android.sdc.ui.fragments.ExperimentListFragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class SensorDataCollectorActivity extends FragmentActivity implements
        ExperimentListFragment.OnFragmentClickListener {
    
    public final static String APP_OPERATION_KEY = "operation";
    public final static int APP_OPERATION_CREATE_NEW_EXPERIMENT = 1;
    public final static int APP_OPERATION_CLONE_EXPERIMENT = 2;
    
    // For Dropbox Sync API 
    private static final String DBX_APP_KEY = "t02om332sh6xycp";
    private static final String DBX_APP_SECRET = "ynuhmqusy8b4gt8";
    private static final int REQUEST_LINK_TO_DBX = 123;
    private DbxAccountManager mDbxAcctMgr;
    
    private ExperimentListFragment mExperimentListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        ExperimentManagerSingleton.getInstance().addExperimentStore(
                StoreSingleton.getInstance());

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            mExperimentListFragment = new ExperimentListFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.add(R.id.fragment_container, mExperimentListFragment);
            transaction.commit();
        }

        SensorUtilsSingleton.getInstance().setContext(getBaseContext());
        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), DBX_APP_KEY, DBX_APP_SECRET);
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
    	super.onResume();
    }
    
    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                showToast("Successfully linked to Dropbox.");
            } else {
                showToast("Link to Dropbox failed or was cancelled.");
            }
        	if (android.os.Build.VERSION.SDK_INT >= 11) {
            	invalidateOptionsMenu();
        	}
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sensordatacollector_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_dropbox);
    	if (mDbxAcctMgr.hasLinkedAccount()) {
    		item.setTitle(getString(R.string.text_unlink_from_dropbox));
    	} else {
    		item.setTitle(getString(R.string.text_link_to_dropbox));
    	}
    	return super.onPrepareOptionsMenu(menu);	
    }
    
    @Override
    @SuppressLint("NewApi")
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_dropbox:
            	if (mDbxAcctMgr.hasLinkedAccount()) {
            		mDbxAcctMgr.unlink();
            		showToast("Successfully unlinked from Dropbox.");
                	if (android.os.Build.VERSION.SDK_INT >= 11) {
                    	invalidateOptionsMenu();
                	}
            	} else {
            		mDbxAcctMgr.startLink((Activity)this, REQUEST_LINK_TO_DBX);
            	}
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onExperimentClicked_ExperimentListFragment(Experiment experiment) {
        Log.i("SensorDataCollector", "Creating ViewExperimentActivity with experiment = "
                + experiment);

        Intent intent = new Intent(this, ViewExperimentActivity.class);

        // intent.putExtra("experiment", experiment);

        ExperimentManagerSingleton.getInstance().setActiveExperiment(experiment);

        startActivity(intent);
    }

    @Override
    public void onCreateExperimentButtonClicked_ExperimentListFragment(Button b) {
        Log.i("SensorDataCollector", "Creating CreateExperimentActivity ...");
        
        Intent intent = new Intent(this, CreateExperimentActivity.class);
        intent.putExtra(SensorDataCollectorActivity.APP_OPERATION_KEY,
                SensorDataCollectorActivity.APP_OPERATION_CREATE_NEW_EXPERIMENT);
        startActivity(intent);
    }
    

    public ExperimentListFragment getExperimentListFragment() {
        return mExperimentListFragment;
    }
    
    @Override
	public void onBackPressed(){
    	moveTaskToBack(true);
	}
 
    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }
}
