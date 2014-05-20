
package sysnetlab.android.sdc.ui.fragments;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ExperimentViewFragment extends Fragment {
    private View mView;
    private ExperimentViewTagsFragment mExperimentViewTagsFragment;
    private ExperimentViewNotesFragment mExperimentViewNotesFragment;
    private ExperimentSensorListFragment mExperimentSensorListFragment;
    private OnFragmentClickListener mCallback;

    public interface OnFragmentClickListener {
        public void onBtnBackClicked_ExperimentViewFragment();
        public void onBtnCloneClicked_ExperimentViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_view, container,
                false);

        // use nested fragment
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (mExperimentViewTagsFragment == null) {
            mExperimentViewTagsFragment = new ExperimentViewTagsFragment();
            // mExperimentViewTagsFragment.setArguments(getArguments());
            transaction.add(R.id.layout_experiment_view_tags, mExperimentViewTagsFragment);
        }

        if (mExperimentViewNotesFragment == null) {
            mExperimentViewNotesFragment = new ExperimentViewNotesFragment();
            // mExperimentViewNotesFragment.setArguments(getArguments());
            transaction.add(R.id.layout_experiment_view_notes, mExperimentViewNotesFragment);
        }

        if (mExperimentSensorListFragment == null) {
            mExperimentSensorListFragment = new ExperimentSensorListFragment();
            transaction.add(R.id.layout_experiment_view_sensor_list, mExperimentSensorListFragment);
        }
        transaction.commit();

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((EditText) mView.findViewById(R.id.et_experiment_view_name))
                .setText(((ViewExperimentActivity) getActivity()).getExperiment().getName());

        ((Button) mView.findViewById(R.id.button_experiment_view_clone))
                .setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mCallback.onBtnCloneClicked_ExperimentViewFragment();
                    }
                });

        ((Button) mView.findViewById(R.id.button_experiment_view_back))
                .setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mCallback.onBtnBackClicked_ExperimentViewFragment();
                    }
                });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentClickListener");
        }     
    }
}
