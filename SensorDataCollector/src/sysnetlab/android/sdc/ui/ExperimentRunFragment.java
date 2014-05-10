package sysnetlab.android.sdc.ui;

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

public class ExperimentRunFragment extends Fragment {
	private View mView;
	private OnFragmentClickListener mCallback;   
	private ExperimentRunTaggingFragment mExperimenRunTaggingFragment;

	
	public interface OnFragmentClickListener {
        public void onBtnDoneClicked_ExperimentRunFragment();
        public void runExperiment(View v);
        public void stopExperiment(View v);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		((Button)mView.findViewById(R.id.button_experiment_done))
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
            mCallback = (OnFragmentClickListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExperimentRunFragment.OnFragmentClickedListener");
        }		
	}


	@Override
	public void onPause() {
		super.onPause();
		
		if (mCallback == null) {
			Log.e("SensorDataCollector", "ExperimentRunFragment.mCallback should not be null");
		}
		
		if (this.mView == null) {
			Log.e("SensorDataCollector", "ExperimentRunFragment.mCallback should not be null");
		}
		mCallback.stopExperiment(mView);				
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if (mCallback == null) {
			Log.e("SensorDataCollector", "ExperimentRunFragment.mCallback should not be null");
		}
		
		if (this.mView == null) {
			Log.e("SensorDataCollector", "ExperimentRunFragment.mCallback should not be null");
		}
		mCallback.runExperiment(mView);		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = super.onCreateView(inflater, container, savedInstanceState);
		
        mView = inflater.inflate(R.layout.fragment_experiment_run, container, false);
        
        
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		if (mExperimenRunTaggingFragment == null) {
			mExperimenRunTaggingFragment = new ExperimentRunTaggingFragment();
		}
		transaction.add(R.id.layout_experiment_run_tags, mExperimenRunTaggingFragment).commit();    

		
		return mView;
	}


}
