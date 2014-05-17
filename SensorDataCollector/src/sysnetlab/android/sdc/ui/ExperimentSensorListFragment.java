
package sysnetlab.android.sdc.ui;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.ui.adaptors.SensorListAdaptor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ExperimentSensorListFragment extends Fragment {

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
            
            SensorListAdaptor sensorListAdaptor = new SensorListAdaptor(getActivity(), lstSensors, View.GONE);
            listView.setAdapter(sensorListAdaptor);
            
            listView.setVisibility(View.VISIBLE);
        }

        return view;
    }

}
