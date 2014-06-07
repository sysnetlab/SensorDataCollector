package sysnetlab.android.sdc.datacollector;

import java.io.File;
import java.io.FileInputStream;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

public class DropboxHelper {

	// Singleton Object
	private static DropboxHelper mHelper;

	final static private String APP_KEY = "t02om332sh6xycp";
	final static private String APP_SECRET = "ynuhmqusy8b4gt8";
	final static private String ACCESS_KEY_NAME = "DROPBOX_ACCESS_KEY";
	final static private String ACCESS_SECRET_NAME = "DROPBOX_ACCESS_SECRET";
	private DropboxAPI<AndroidAuthSession> mDropboxApi;
	private Context mContext;

	public static DropboxHelper getInstance(Context context) {
		if (mHelper == null) {
			mHelper = new DropboxHelper(context);
		}
		return mHelper;
	}

	public static DropboxHelper getInstance() throws RuntimeException {
		if (mHelper == null)
			throw new RuntimeException("DropboxHelper not yet instantiated");

		return mHelper;
	}

	protected DropboxHelper(Context context) {
		super();
		mContext = context;

		checkAppKeySetup();

		// create session
		AndroidAuthSession session = buildSession();
		mDropboxApi = new DropboxAPI<AndroidAuthSession>(session);
	}

	protected void checkAppKeySetup() {
		// Check to make sure that we have a valid app key
		if (APP_KEY.startsWith("CHANGE") || APP_SECRET.startsWith("CHANGE")) {
			showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
			return;
		}

		// Check if the app has set up its manifest properly.
		Intent testIntent = new Intent(Intent.ACTION_VIEW);
		String scheme = "db-" + APP_KEY;
		String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
		testIntent.setData(Uri.parse(uri));
		PackageManager pm = mContext.getPackageManager();
		if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
			showToast("URL scheme in your app's "
					+ "manifest is not set up correctly. You should have a "
					+ "com.dropbox.client2.android.AuthActivity with the "
					+ "scheme: " + scheme);
		}
	}

	protected AndroidAuthSession buildSession() {
		AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);

		AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
		loadAuth(session);
		return session;
	}

	protected void loadAuth(AndroidAuthSession session) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		String key = prefs.getString(ACCESS_KEY_NAME, null);
		String secret = prefs.getString(ACCESS_SECRET_NAME, null);

		if (key == null || secret == null || key.length() == 0
				|| secret.length() == 0)
			return;

		if (key.equals("oauth2:")) {
			// If the key is set to "oauth2:", then we can assume the token is
			// for OAuth 2.
			session.setOAuth2AccessToken(secret);
		} else {
			// Still support using old OAuth 1 tokens.
			session.setAccessTokenPair(new AccessTokenPair(key, secret));
		}
	}

	// Clear token
	protected void clearKeys() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		Editor edit = prefs.edit();
		edit.clear();
		edit.commit();
	}

	protected void storeKeys(AndroidAuthSession session) {
		// Store the OAuth 2 access token, if there is one.
		String oauth2AccessToken = session.getOAuth2AccessToken();
		if (oauth2AccessToken != null) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(mContext);
			Editor edit = prefs.edit();
			edit.putString(ACCESS_KEY_NAME, "oauth2:");
			edit.putString(ACCESS_SECRET_NAME, oauth2AccessToken);
			edit.commit();
			return;
		}
		// Store the OAuth 1 access token, if there is one. This is only
		// necessary if
		// you're still using OAuth 1.
		AccessTokenPair oauth1AccessToken = session.getAccessTokenPair();
		if (oauth1AccessToken != null) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(mContext);
			Editor edit = prefs.edit();
			edit.putString(ACCESS_KEY_NAME, oauth1AccessToken.key);
			edit.putString(ACCESS_SECRET_NAME, oauth1AccessToken.secret);
			edit.commit();
			return;
		}
	}

	// Complete authentication
	public void completeAuthentication() {
		if (isLinked())
			return;
		AndroidAuthSession session = mDropboxApi.getSession();

		if (session.authenticationSuccessful()) {
			try {
				// Mandatory call to complete the auth
				session.finishAuthentication();

				// Store it locally in our app for later use
				storeKeys(session);
				showToast("Successfully linked to Dropbox.");
			} catch (IllegalStateException e) {
				showToast("Couldn't authenticate with Dropbox:"
						+ e.getLocalizedMessage());
				Log.i("DropboxHelper", "Error authenticating", e);
			}
		}
	}

	// Returns true if you are connected to Dropbox
	public boolean isLinked() {
		return mDropboxApi.getSession().isLinked();
	}

	public void link() {
		mDropboxApi.getSession().startOAuth2Authentication(mContext);
	}

	public void unlink() {
		mDropboxApi.getSession().unlink();
		// Clear our stored keys
		clearKeys();
		showToast("Successfully unlinked from Dropbox.");
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
		error.show();
	}

	
	public void writeAllFilesInDirToDropbox(String path) {
		if (!isLinked()) {
			showToast("Dropbox has not been linked.");
			return;
		}
		
		AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {
            
            @Override
            protected String doInBackground(String... path) {
        		Log.i("DropboxHelper", "Sending path " + path[0] + " to Dropbox.");
        		File root = new File(path[0]);
        		if (!root.exists()) {
        			return "Unsupported Experiment Datastore";
        		}
            	File[] allFiles = root.listFiles();
        		for (File f : allFiles) {
        			 Log.i("DropboxHelper", "Sending " + f.getAbsolutePath() + " to Dropbox.");
        			 FileInputStream inputStream = null;
        			 try {
        			     File file = f;
        			     inputStream = new FileInputStream(f);
        			     String dbPath = f.getAbsolutePath();
        			     String storagePath = Environment.getExternalStorageDirectory().getPath();
        			     if (dbPath.startsWith(storagePath)) {
        			    	 dbPath = dbPath.substring(storagePath.length());
        			     }
        			     @SuppressWarnings("unused")
        			     Entry newEntry = mDropboxApi.putFile(dbPath, inputStream,
        			             file.length(), null, null);
        			 } catch (Exception e) {
        			     Log.e("DropboxHelper", "Something went wrong: " + e);
           				 return "Failed to send experiment to Dropbox.";
        			 } 
        		}
            	return "Experiment successfully sent to Dropbox.";
            }
            
            @Override 
            protected void onPostExecute (String result) {
            	showToast(result);
            }

	    };
	    asyncTask.execute(path);	
	}
}
