
package sysnetlab.android.sdc.ui.fragments;

import java.lang.reflect.Field;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.SensorDataCollectorActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ExperimentRunFragment extends Fragment{
    private View mView;
    private OnFragmentClickListener mCallback;
    private ExperimentHandler mHandler;

    private ExperimentRunTaggingFragment mExperimenRunTaggingFragment;

    public interface OnFragmentClickListener {
        public void onBtnDoneClicked_ExperimentRunFragment();
    }

    public interface ExperimentHandler {
        public void runExperiment_ExperimentRunFragment(View v);

        public void stopExperiment_ExperimentRunFragment(View v);
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
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mCallback == null) {
            Log.e("SensorDataCollector", "ExperimentRunFragment.mCallback should not be null");
        }

        if (this.mView == null) {
            Log.e("SensorDataCollector", "ExperimentRunFragment.mView should not be null");
        }
        mHandler.stopExperiment_ExperimentRunFragment(mView);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCallback == null) {
            Log.e("SensorDataCollector", "ExperimentRunFragment.mCallback should not be null");
        }

        if (this.mView == null) {
            Log.e("SensorDataCollector", "ExperimentRunFragment.mView should not be null");
        }
        mHandler.runExperiment_ExperimentRunFragment(mView);
    }    
    
    public boolean isFragmentUIActive() {
        return isAdded() && !isDetached() && !isRemoving();
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {      
      inflater.inflate(R.menu.action_bar, menu);      
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
        
        return mView;
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
}
