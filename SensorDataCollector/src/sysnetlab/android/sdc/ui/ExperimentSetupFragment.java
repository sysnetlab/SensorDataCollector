package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class ExperimentSetupFragment extends Fragment {
	private View mView;
	private ExperimentTagsFragment mExperimentTagsFragment;
	private ExperimentSensorSelectionFragment mExperimentSensorSelectionFragment;	
	private OnFragmentClickListener mCallback;
	private ExperimentHandler mHandler;
	
	public interface OnFragmentClickListener {
    	public void onImvTagsClicked_ExperimentSetupFragment(ImageView v);
    	public void onImvNotesClicked_ExperimentSetupFragment(ImageView v);
    	public void onImvSensorsClicked_ExperimentSetupFragment(ImageView v);    	
    	public void onBtnRunClicked_ExperimentSetupFragment(View v);
    	public void onBtnBackClicked_ExperimentSetupFragment();
    }	
	
	public interface ExperimentHandler {
		public void initViewWithExperiment_ExperimentSetupFragment(View v);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		((ImageView)mView.findViewById(R.id.imv_sensors_plusminus))
		.setOnClickListener(new ImageView.OnClickListener() {
			public void onClick(View v) {
				mCallback.onImvSensorsClicked_ExperimentSetupFragment((ImageView)v);
			}
		});
		
		((Button)mView.findViewById(R.id.button_experiment_run))
		.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.onBtnRunClicked_ExperimentSetupFragment(v);
			}
			
		});
		
		((Button)mView.findViewById(R.id.button_experiment_back))
		.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.onBtnBackClicked_ExperimentSetupFragment();
			}
		});
		
		mHandler.initViewWithExperiment_ExperimentSetupFragment(mView);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
        mView = inflater.inflate(R.layout.fragment_experiment_setup, container, false);
        
		// use nested fragment
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();


		if (mExperimentTagsFragment == null) {
			mExperimentTagsFragment = new ExperimentTagsFragment();;
			transaction.add(R.id.layout_tags, mExperimentTagsFragment);
		}
		
		if (mExperimentSensorSelectionFragment == null) {
			mExperimentSensorSelectionFragment = new ExperimentSensorSelectionFragment();
			/*
			Button button = new Button(getActivity());
			button.setText("Clear");
			mExperimentSensorSelectionFragment.addViewToHeader(button);
			*/
			transaction.add(R.id.layout_sensor_list,  mExperimentSensorSelectionFragment);
		}
		transaction.commit();
		
        return mView;		
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
        try {
            mCallback = (OnFragmentClickListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExperimentSetupFragment.OnFragmentClickListener");
        }
        
        try {
        	mHandler = (ExperimentHandler)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExperimentSetupFragment.ExperimentHandler");

        }
	
	}

	public View getView() {
		return mView;
	}
}
