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

import java.lang.reflect.Field;

import sysnetlab.android.sdc.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ExperimentRunFragment extends Fragment{
    private View mView;
    private OnFragmentClickListener mCallback;
    private ExperimentHandler mHandler;
    private boolean mIsUserTrigger;

    private ExperimentRunTaggingFragment mExperimenRunTaggingFragment;

    public interface OnFragmentClickListener {
        public void onBtnDoneClicked_ExperimentRunFragment();
    }

    public interface ExperimentHandler {
        public void runExperiment_ExperimentRunFragment();
        public void stopExperiment_ExperimentRunFragment();
        public void notifyInBackground_ExperimentRunFragment();
        public void removeInBackgroundNotification_ExperimentRunFragment();
        public void runTimer_ExperimentRunFragment();        
    }       

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Button) mView.findViewById(R.id.button_experiment_done))
                .setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onBtnDoneClicked_ExperimentRunFragment();
                    }
                }
                );
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExperimentRunFragment.OnFragmentClickedListener");
        }
        try {
            mHandler = (ExperimentHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExperimentRunFragment.ExperimentHandler");
        }
        
        if (mCallback == null) {
            Log.e("SensorDataCollector", "ExperimentRunFragment.mCallback should not be null");
        }

        mHandler.runExperiment_ExperimentRunFragment();
    }

    @Override
    public void onStop(){
	    if(!mIsUserTrigger){
	    	mHandler.notifyInBackground_ExperimentRunFragment();    		
	    }
        super.onStop();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        mIsUserTrigger = false;
        mHandler.removeInBackgroundNotification_ExperimentRunFragment();
    }    
    
    public boolean isFragmentUIActive() {
        return isAdded() && !isDetached() && !isRemoving();
    }
       
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_run, container, false);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (mExperimenRunTaggingFragment == null) {
            mExperimenRunTaggingFragment = new ExperimentRunTaggingFragment();
        }
        transaction.replace(R.id.layout_experiment_run_tags, mExperimenRunTaggingFragment).commit();
        setHasOptionsMenu(true);
        
        mHandler.runTimer_ExperimentRunFragment();
        
        return mView;
    }
    
    @Override
    public void onPause() {
    	if(mIsUserTrigger){
	    	if (mCallback == null) {
	            Log.e("SensorDataCollector", "ExperimentRunFragment.mCallback should not be null");
	        }
	
	        if (this.mView == null) {
	            Log.e("SensorDataCollector", "ExperimentRunFragment.mView should not be null");
	        }
	        mHandler.stopExperiment_ExperimentRunFragment();
    	}    	
    	super.onPause();
    }
    
    @Override
    public void onDetach() {
    	super.onDetach();
    	
        /*
         * it appears that there is a bug in the support library that dealing
         * with nested fragments as discussed in
         * http://stackoverflow.com/questions
         * /15207305/getting-the-error-java-lang
         * -illegalstateexception-activity-has-been-destroyed In this
         * application, the symptom of the bug is that when you click the back
         * button to return to the experiment setup screen from the experiment
         * run screen. The activity crashed with an error of
         * "java.lang.IllegalStateException: Activity has been destroyed". The
         * following code snippet, as suggested in the discussion, appears to
         * fix the problem.
         */
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void onDestroyView(){
    	super.onDestroyView();
    	mHandler.removeInBackgroundNotification_ExperimentRunFragment();
    }
    
    public void setIsUserTrigger(boolean isUserTrigger){
    	mIsUserTrigger=isUserTrigger;
    }
    
    public boolean getIsUserTrigger(){
    	return mIsUserTrigger;
    }
    
    public View getView(){
    	return mView;
    }
}
