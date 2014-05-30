
package sysnetlab.android.sdc.ui.fragments;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import sysnetlab.android.sdc.ui.adaptors.OperationAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ExperimentViewFragment extends Fragment {
    private ExperimentSensorListFragment mExperimentSensorListFragment;
    private OnFragmentClickListener mCallback;

    public interface OnFragmentClickListener {
        public void onBtnCloneClicked_ExperimentViewFragment();

        public void onTagsClicked_ExperimentViewFragment();

        public void onNotesClicked_ExperimentViewFragment();

        public void onSensorsClicked_ExperimentViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_experiment_view, container,
                false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewExperimentActivity activity = (ViewExperimentActivity) getActivity();

        ((TextView) activity.findViewById(R.id.textview_experiment_view_name_value))
                .setText(activity.getExperiment().getName());

        ListView listOperations = (ListView) activity
                .findViewById(R.id.listview_experiment_view_operations);
        OperationAdapter operationAdapter = new OperationAdapter(activity,
                activity.getExperiment(),
                OperationAdapter.VIEW_EXPERIMENT);
        listOperations.setAdapter(operationAdapter);

        listOperations.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == OperationAdapter.OP_TAGS) {
                    mCallback.onTagsClicked_ExperimentViewFragment();

                }
                else if (position == OperationAdapter.OP_NOTES) {
                    mCallback.onNotesClicked_ExperimentViewFragment();
                }
                else if (position == OperationAdapter.OP_SENSORS) {
                    mCallback.onSensorsClicked_ExperimentViewFragment();
                }

            }
        });

        // use nested fragment
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (mExperimentSensorListFragment == null) {
            mExperimentSensorListFragment = new ExperimentSensorListFragment();
            transaction.add(R.id.layout_experiment_view_sensor_list, mExperimentSensorListFragment);
        }
        transaction.commit();

        ((Button) getActivity().findViewById(R.id.button_experiment_view_clone))
                .setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mCallback.onBtnCloneClicked_ExperimentViewFragment();
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
    
    public ExperimentSensorListFragment getExperimentSensorListFragment(){
    	return mExperimentSensorListFragment;
    }
    
}
