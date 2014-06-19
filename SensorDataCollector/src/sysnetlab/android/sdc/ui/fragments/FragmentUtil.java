
package sysnetlab.android.sdc.ui.fragments;

import sysnetlab.android.sdc.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtil {
	public static void switchToFragment(FragmentActivity activity, Fragment fragment, String name) {
		FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);	
		transaction.replace(R.id.fragment_container, fragment);
		transaction.addToBackStack(name);
		transaction.commit();			
	}	
	
    public static void addFragment(FragmentActivity activity, Fragment fragment, String name) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(name);
        transaction.commit();
    }
    
    public static void addFragment(FragmentActivity activity, Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);
        transaction.add(R.id.fragment_container, fragment);
        transaction.commit();
    }

}
