
package sysnetlab.android.sdc.ui;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datastore.AbstractStore.Channel;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ExperimentViewSensorDataFragment extends Fragment {

    private View mView;
    private OnFragmentClickListener mCallback;
    private int mSensorNo;

    private int MAXIMUM_LINES_OF_DATA_TO_READ = 100;

    public interface OnFragmentClickListener {
        public void onBtnBackClicked_ExperimentViewSensorDataFragment();
    }

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

        ((Button) mView.findViewById(R.id.button_fragment_experiment_view_sensor_data_back))
                .setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mCallback.onBtnBackClicked_ExperimentViewSensorDataFragment();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_view_sensor_data, container, false);

        Experiment experiment = ExperimentManagerSingleton.getInstance().getActiveExperiment();

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_sensor_data_experiment_name))
                .setText(experiment.getName());

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_sensor_data_experiment_time_created))
                .setText(experiment.getDateTimeCreated());

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_sensor_data_experiment_time_done))
                .setText(experiment.getDateTimeDone());

        if (experiment.getSensors().size() > 0) {
            updateSensorDataView(experiment.getSensors(), mSensorNo);
        } else {
            ((TextView) mView
                    .findViewById(R.id.textview_fragment_experiment_view_sensor_data_sensor_description))
                    .setText(getResources().getString(R.string.text_unknown_sensor));

            ((TextView) mView
                    .findViewById(R.id.textview_fragment_experiment_view_sensor_data_text))
                    .setText(getResources().getString(
                            R.string.text_sensor_has_not_recorded_any_data));
        }
        return mView;
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

    private void updateSensorDataView(ArrayList<AbstractSensor> lstSensors, int sensorNo) {
        AbstractSensor sensor = lstSensors.get(sensorNo);
        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_sensor_data_sensor_description))
                .setText(
                sensor.toString()
                );

        String sensorData = getSensorData(sensor, MAXIMUM_LINES_OF_DATA_TO_READ);

        if (sensorData.trim().equals("")) {
            ((TextView) mView
                    .findViewById(R.id.textview_fragment_experiment_view_sensor_data_text))
                    .setText(getResources().getString(
                            R.string.text_sensor_has_not_recorded_any_data));
        } else {
            TextView textView = (TextView) mView
                    .findViewById(R.id.textview_fragment_experiment_view_sensor_data_text);
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
