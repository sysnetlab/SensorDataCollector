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

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.adapters.SensorListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ExperimentSensorSelectionFragment extends Fragment {
    private boolean mHavingHeader;

    private View mView;
    private ListView mListView;
    private ArrayAdapter<AbstractSensor> mSensorListAdaptor;

    private OnFragmentClickListener mCallback;

    public interface OnFragmentClickListener {
        public void onBtnClearClicked_ExperimentSensorSelectionFragment(boolean checked);

        public void onSensorClicked_ExperimentSensorSelectionFragment(AbstractSensor sensor);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SensorDataCollector",
                "entered ExperimentSensorSelectionFragment::onCreate(). ");
        mHavingHeader = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d("SensorDataCollector",
                "ExperimentSensorSelectionFragment::onCreateView(): " +
                        "entered with mHavingHeader = " + mHavingHeader);

        mView = inflater.inflate(R.layout.fragment_sensor_selection, container, false);

        mHavingHeader = getActivity().getIntent().getBooleanExtra("havingheader", false);

        if (mHavingHeader) {
            RelativeLayout layout = (RelativeLayout) mView
                    .findViewById(R.id.layout_sensor_selection_header);
            layout.setVisibility(View.VISIBLE);
        }

        // find the container for the list
        RelativeLayout layout = (RelativeLayout) mView
                .findViewById(R.id.layout_sensor_selection_list);

        mListView = new ListView(getActivity());
        if (!SensorDiscoverer.isInitialized())
            SensorDiscoverer.initialize(getActivity().getApplicationContext());
        List<AbstractSensor> lstSensors = SensorDiscoverer.discoverSensorList();

        Activity activity = getActivity();
        if (activity instanceof CreateExperimentActivity) {
            ((CreateExperimentActivity) activity).selectSensors(lstSensors);
        } else {
            Log.d("SensorDataCollector",
                    "ExperimentSensorSelectionFragment::onCreate(): "
                            + "not a CreateExperimentActivity.");
        }

        mSensorListAdaptor = new SensorListAdapter(getActivity(), lstSensors);

        mListView.setAdapter(mSensorListAdaptor);
        layout.addView(mListView);

        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            Log.e("SensorDataCollector",
                    "ExperimentSensorSelectionFragment::onAttach(): failed to cast Activity to OnFragmentClickListener.");
            e.printStackTrace();
            throw new RuntimeException("Failed to cast Activity to OnFragmentClickListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("SensorDataCollector",
                "ExperimentSensorSelectionFragment::onActivityCreated() called.");

        final CheckBox checkAll = ((CheckBox) mView.findViewById(R.id.checkbox_check_all));
        checkAll.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean checked = checkAll.isChecked();
                mCallback.onBtnClearClicked_ExperimentSensorSelectionFragment(checked);
            }
        });

        // ((ListView) mView.findViewById(R.id.layout_sensor_list))
        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                mCallback.onSensorClicked_ExperimentSensorSelectionFragment(
                        (AbstractSensor) listView.getItemAtPosition(position));
            }
        });
    }

    public ArrayAdapter<AbstractSensor> getSensorListAdapter() {
        return mSensorListAdaptor;
    }

    public ListView getSensorListView() {
        return mListView;
    }
}
