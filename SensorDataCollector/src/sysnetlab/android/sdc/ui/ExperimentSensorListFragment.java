
package sysnetlab.android.sdc.ui;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.ui.adaptors.SensorListAdaptor;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ExperimentSensorListFragment extends Fragment {
    private OnFragmentClickListener mCallback;
    private ListView mListView;

    public interface OnFragmentClickListener {
        public void onListItemClicked_ExperimentSensorListFragment(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_experiment_sensor_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listview_experiment_view_sensor_list);
        TextView textView = (TextView) view.findViewById(R.id.textview_experiment_view_no_sensors);

        ArrayList<AbstractSensor> lstSensors = ExperimentManagerSingleton.getInstance()
                .getActiveExperiment()
                .getSensors();

        if (lstSensors.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);

            SensorListAdaptor sensorListAdaptor = new SensorListAdaptor(getActivity(), lstSensors,
                    View.GONE);
            listView.setAdapter(sensorListAdaptor);

            listView.setVisibility(View.VISIBLE);
        }
        mListView = listView;

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mListView != null) {
            mListView.setOnItemClickListener(new ListView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                    Log.i("SensorDataCollector", "Sensor List item clicked at postion " + position);
                    mCallback
                            .onListItemClicked_ExperimentSensorListFragment(position);
                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSwipeListener");
        }
    }
    
    
}
