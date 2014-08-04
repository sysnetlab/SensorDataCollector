/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

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
