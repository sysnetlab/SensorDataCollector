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
	private SensorListFragment mSensorListFragment;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
	}

	private OnFragmentClickListener mCallback;
	
	public interface OnFragmentClickListener {
    	public void onImvTagsClicked_ExperimentSetupFragment(ImageView v);
    	public void onImvNotesClicked_ExperimentSetupFragment(ImageView v);
    	public void onImvSensorsClicked_ExperimentSetupFragment(ImageView v);    	
    	public void onBtnRunClicked_ExperimentSetupFragment(View v);
    	public void onBtnBackClicked_ExperimentSetupFragment();
    }	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		
        mView = inflater.inflate(R.layout.fragment_experiment_setup_layout, container, false);
        
		// use nested fragment
		if (mSensorListFragment == null) {
			mSensorListFragment = new SensorListFragment();
			FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
			transaction.add(R.id.layout_sensor_list, mSensorListFragment).commit();   
		}
		
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
	}

	public View getView() {
		return mView;
	}
}
