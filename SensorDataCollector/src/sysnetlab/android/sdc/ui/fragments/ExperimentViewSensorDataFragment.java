
package sysnetlab.android.sdc.ui.fragments;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.ui.GestureEventListener;
import sysnetlab.android.sdc.ui.UserInterfaceUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ExperimentViewSensorDataFragment extends Fragment {

    private View mView;
    private int mSensorNo;

    private int MAXIMUM_LINES_OF_DATA_TO_READ = 100;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mView.setOnTouchListener(new GestureEventListener(getActivity()) {

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

                Experiment experiment = ExperimentManagerSingleton.getInstance()
                        .getActiveExperiment();
                if (mSensorNo > 0) {
                    mSensorNo--;
                    updateSensorDataView(experiment.getSensors(), mSensorNo);
                }
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                Experiment experiment = ExperimentManagerSingleton.getInstance()
                        .getActiveExperiment();
                if (mSensorNo < experiment.getSensors().size() - 1) {
                    mSensorNo++;
                    updateSensorDataView(experiment.getSensors(), mSensorNo);
                }
            }

            @Override
            public void onSwipeTop() {
                // TODO Auto-generated method stub
                super.onSwipeTop();
            }

            @Override
            public void onSwipeBottom() {
                // TODO Auto-generated method stub
                super.onSwipeBottom();
            }

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_view_sensor_data, container, false);

        Experiment experiment = ExperimentManagerSingleton.getInstance().getActiveExperiment();

        String strHeadingSubTextFormatter = getResources().getString(
                R.string.text_for_experiment_name_x);
        String strHeadingSubText = String.format(strHeadingSubTextFormatter, experiment.getName());
        ((TextView) mView.findViewById(R.id.textview_experiment_sensor_viewing_subtext))
                .setText(strHeadingSubText);

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_sensors_experiment_time_created))
                .setText(experiment.getDateTimeCreatedAsString());

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_sensors_experiment_time_done))
                .setText(experiment.getDateTimeDoneAsString());

        updateSensorDataView(experiment.getSensors(), mSensorNo);

        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorNo = 0;
    }

    private void updateSensorDataView(ArrayList<AbstractSensor> lstSensors, int sensorNo) {
        if (lstSensors == null || lstSensors.isEmpty()) {
            ((TextView) mView.findViewById(R.id.textview_fragment_experiment_view_notes_note_text))
                    .setText(mView.getResources()
                            .getString(R.string.text_experiment_has_no_sensors));
            return;
        }

        AbstractSensor sensor = lstSensors.get(sensorNo);
        ListView listView = (ListView) mView
                .findViewById(R.id.listview_fragment_experiment_view_sensor_data_sensor_properties);

        UserInterfaceUtil.fillSensorProperties(getActivity(), listView, sensor, true);

        String sensorData = getSensorData(sensor, MAXIMUM_LINES_OF_DATA_TO_READ);

        if (sensorData.trim().equals("")) {
            ((TextView) mView
                    .findViewById(R.id.textview_fragment_experiment_view_notes_note_text))
                    .setText(getResources().getString(
                            R.string.text_sensor_has_not_recorded_any_data));
        } else {
            TextView textView = (TextView) mView
                    .findViewById(R.id.textview_fragment_experiment_view_notes_note_text);
            textView.setText(sensorData);
            textView.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    public void setSensorNo(int sensorNo) {
        mSensorNo = sensorNo;
    }

    private String getSensorData(AbstractSensor sensor, int maximumLines) {
        Log.i("SensorDataCollector", "called ExperimentViewSensorDataFragment::getSensorData().");
        String data = "";

        Channel channel = sensor.getListener().getChannel();

        for (int i = 0; i < maximumLines; i++) {
            String line = channel.read();
            if (line == null)
                break;
            data = data + line + "\n";
        }

        channel.reset();

        return data;
    }

}
