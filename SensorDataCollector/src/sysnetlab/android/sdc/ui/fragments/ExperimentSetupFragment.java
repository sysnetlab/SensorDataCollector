package sysnetlab.android.sdc.ui.fragments;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.adaptors.OperationAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;	
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ExperimentSetupFragment extends Fragment {
    private View mView;
    private OnFragmentClickListener mCallback;

    public interface OnFragmentClickListener {
        public void onTagsClicked_ExperimentSetupFragment();

        public void onNotesClicked_ExperimentSetupFragment();

        public void onSensorsClicked_ExperimentSetupFragment();

        public void onBtnRunClicked_ExperimentSetupFragment(View v);

        public void onBtnBackClicked_ExperimentSetupFragment();
    }

    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((EditText) mView.findViewById(R.id.et_experiment_setup_name))
        .setText(((CreateExperimentActivity) getActivity()).getExperiment().getName());

		CreateExperimentActivity a = (CreateExperimentActivity) getActivity();
		ListView operationMenu = (ListView) mView.findViewById(R.id.lv_operations);
		OperationAdapter operationAdapter = new OperationAdapter(a, a.getExperiment());
		operationMenu.setAdapter(operationAdapter);
		operationMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == OperationAdapter.OP_TAGS) {
					mCallback.onTagsClicked_ExperimentSetupFragment();
					
				}
				else if (position == OperationAdapter.OP_NOTES) {
					mCallback.onNotesClicked_ExperimentSetupFragment();
				}
				else if (position == OperationAdapter.OP_SENSORS) {
					mCallback.onSensorsClicked_ExperimentSetupFragment();
				}
				
			}
			
		});


        ((Button) mView.findViewById(R.id.button_experiment_run))
                .setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onBtnRunClicked_ExperimentSetupFragment(mView);
                    }
                });

        ((Button) mView.findViewById(R.id.button_experiment_back))
                .setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onBtnBackClicked_ExperimentSetupFragment();
                    }
                });
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_setup, container, false);


        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExperimentSetupFragment.OnFragmentClickListener");
        }
    }

    public View getView() {
        return mView;
    }
}