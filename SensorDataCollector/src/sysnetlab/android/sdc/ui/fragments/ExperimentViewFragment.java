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
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import sysnetlab.android.sdc.ui.adapters.OperationAdapter;
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

        public void onBtnDropboxClicked_ExperimentViewFragment();

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

        ((Button) getActivity().findViewById(R.id.button_experiment_view_dropbox))
                .setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mCallback.onBtnDropboxClicked_ExperimentViewFragment();
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

    public ExperimentSensorListFragment getExperimentSensorListFragment() {
        return mExperimentSensorListFragment;
    }

    public boolean isFragmentUIActive() {
        return isAdded() && !isDetached() && !isRemoving();
    }

}
