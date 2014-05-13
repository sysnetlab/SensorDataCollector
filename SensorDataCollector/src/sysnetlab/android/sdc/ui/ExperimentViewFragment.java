package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ExperimentViewFragment extends Fragment {
	private View mView;
	private ExperimentTagsFragment mExperimentTagsFragment;
	private ExperimentSensorSelectionFragment mExperimentSensorSelectionFragment;
	private OnFragmentClickListener mCallback;
	
	public interface OnFragmentClickListener {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		mView = inflater.inflate(R.layout.fragment_experiment_view, container,
				false);
		
		// use nested fragment
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();


		if (mExperimentTagsFragment == null) {
			mExperimentTagsFragment = new ExperimentTagsFragment();;
			transaction.add(R.id.layout_experiment_view_tags, mExperimentTagsFragment);
		}
		
		if (mExperimentSensorSelectionFragment == null) {
			mExperimentSensorSelectionFragment = new ExperimentSensorSelectionFragment();
			transaction.add(R.id.layout_experiment_view_sensor_list,  mExperimentSensorSelectionFragment);
		}
		transaction.commit();		
		
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		((EditText)mView.findViewById(R.id.et_experiment_view_name))
		.setText(((ViewExperimentActivity)getActivity()).getExperiment().getName());

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			mCallback = (OnFragmentClickListener)activity;
		} catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExperimentViewFragment.ExperimentViewFragmentHandler");
        }
	}

	

}
