
package sysnetlab.android.sdc.ui;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datastore.StoreSingleton;
import sysnetlab.android.sdc.sensor.SensorUtilsSingleton;
import sysnetlab.android.sdc.ui.fragments.ExperimentListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
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
    
    // Dropbox 
    final static private String DROPBOX_APP_KEY = "t02om332sh6xycp";
    final static private String DROPBOX_APP_SECRET = "ynuhmqusy8b4gt8";
    final static private String DROPBOX_ACCOUNT_PREFS_NAME = "dropbox_prefs";
    final static private String DROPBOX_ACCESS_KEY_NAME = "DROPBOX_ACCESS_KEY";
    final static private String DROPBOX_ACCESS_SECRET_NAME = "DROPBOX_ACCESS_SECRET";
    private DropboxAPI<AndroidAuthSession> mDropboxApi;
    private boolean mDropboxLoggedIn = false;
    // End Dropbox
    
    private Menu mMenu;
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
        
      // Dropbox
      dropboxCheckAppKeySetup();
      AppKeyPair appKeys = new AppKeyPair(DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
      AndroidAuthSession session = new AndroidAuthSession(appKeys);
      mDropboxApi = new DropboxAPI<AndroidAuthSession>(session);
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
    	super.onResume();
    	
        // Complete the Dropbox Authorization
        AndroidAuthSession session = mDropboxApi.getSession();

        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                dropboxStoreAuth(session);
                dropboxSetLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                Log.i("CreateExperimentActivity", "Error authenticating", e);
            }
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sensordatacollector_activity_menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_dropbox:
	          // This logs you out if you're logged in, or vice versa
	          if (mDropboxLoggedIn) {
	              // Remove credentials from the session
	              mDropboxApi.getSession().unlink();
	              // Clear our stored keys
	              dropboxClearKeys();
	              // Change UI state to display logged out version
	              dropboxSetLoggedIn(false);
	          } else {
	              mDropboxApi.getSession().startOAuth2Authentication(SensorDataCollectorActivity.this);
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
    
    /**
     * Convenience function to change UI state based on being logged in
     */
    private void dropboxSetLoggedIn(boolean loggedIn) {
    	mDropboxLoggedIn = loggedIn;
        MenuItem item = mMenu.findItem(R.id.action_dropbox);
    	if (mDropboxLoggedIn) {
    		item.setTitle(getString(R.string.text_unlink_from_dropbox));
    		showToast("Successfulling linked to Dropbox.");
    	} else {
    		item.setTitle(getString(R.string.text_link_to_dropbox));
    		showToast("Successfully unlinked from Dropbox.");
    	}
    }
    
    private void dropboxCheckAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (DROPBOX_APP_KEY.startsWith("CHANGE") ||
                DROPBOX_APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            finish();
            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + DROPBOX_APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            showToast("URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);
            finish();
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }
    
    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void dropboxStoreAuth(AndroidAuthSession session) {
        // Store the OAuth 2 access token, if there is one.
        String oauth2AccessToken = session.getOAuth2AccessToken();
        if (oauth2AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(DROPBOX_ACCOUNT_PREFS_NAME, 0);
            Editor edit = prefs.edit();
            edit.putString(DROPBOX_ACCESS_KEY_NAME, "oauth2:");
            edit.putString(DROPBOX_ACCESS_SECRET_NAME, oauth2AccessToken);
            edit.commit();
            return;
        }
        // Store the OAuth 1 access token, if there is one.  This is only necessary if
        // you're still using OAuth 1.
        AccessTokenPair oauth1AccessToken = session.getAccessTokenPair();
        if (oauth1AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(DROPBOX_ACCOUNT_PREFS_NAME, 0);
            Editor edit = prefs.edit();
            edit.putString(DROPBOX_ACCESS_KEY_NAME, oauth1AccessToken.key);
            edit.putString(DROPBOX_ACCESS_SECRET_NAME, oauth1AccessToken.secret);
            edit.commit();
            return;
        }
    }

    private void dropboxClearKeys() {
        SharedPreferences prefs = getSharedPreferences(DROPBOX_ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

}
