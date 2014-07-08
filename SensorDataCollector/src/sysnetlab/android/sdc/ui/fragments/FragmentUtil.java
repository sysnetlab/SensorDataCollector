
package sysnetlab.android.sdc.ui.fragments;

import sysnetlab.android.sdc.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtil {
	
	public final static int FRAGMENT_SWITCH_ADD_TO_BACKSTACK=1;
	public final static int FRAGMENT_SWITCH_NO_BACKSTACK=2;
	
	public static void switchFragment(FragmentActivity activity, Fragment fragment, String name,int backStackOption) {
		FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);	
		transaction.replace(R.id.fragment_container, fragment);
		if(backStackOption == FragmentUtil.FRAGMENT_SWITCH_ADD_TO_BACKSTACK)
			transaction.addToBackStack(name);
		transaction.commit();			
	}	
	
    public static void addFragment(FragmentActivity activity, Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
