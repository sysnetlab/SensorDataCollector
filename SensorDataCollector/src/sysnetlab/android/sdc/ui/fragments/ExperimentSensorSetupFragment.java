
package sysnetlab.android.sdc.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.SensorProperty;
import sysnetlab.android.sdc.sensor.audio.AudioChannelIn;
import sysnetlab.android.sdc.sensor.audio.AudioRecordParameter;
import sysnetlab.android.sdc.sensor.audio.AudioRecordSettingDataSource;
import sysnetlab.android.sdc.sensor.audio.AudioSensor;
import sysnetlab.android.sdc.sensor.audio.AudioSensorSetupDiaglogFragment;
import sysnetlab.android.sdc.sensor.audio.AudioSource;
import sysnetlab.android.sdc.ui.UserInterfaceUtil;
import sysnetlab.android.sdc.R;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ExperimentSensorSetupFragment extends Fragment {
    private OnFragmentClickListener mCallback;
    private View mView;
    private AbstractSensor mSensor;
    private AudioRecordSettingDataSource mAudioRecordSettingDataSource;
    private List<AudioRecordParameter> mAudioRecordParameters;
    
    private ListView mAudioSensorListView;
    private int mIndexAudioChannelIn  = 0;
    private int mIndexAudioSource = 0;

    public interface OnFragmentClickListener {
        public void onBtnSetSamplingRateClicked_SensorSetupFragment(View v, AbstractSensor sensor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO: handle configuration changes
        mAudioRecordSettingDataSource = new AudioRecordSettingDataSource(getActivity());
        mAudioRecordSettingDataSource.open();
        
        switch (mSensor.getMajorType()) {
            case AbstractSensor.ANDROID_SENSOR:
                mView = inflater.inflate(R.layout.fragment_sensor_setup, container, false);

                if (mSensor != null) {
                    updateAndroidSensorSetupView(mSensor);
                }
                break;

            case AbstractSensor.AUDIO_SENSOR:
                mView = inflater.inflate(R.layout.fragment_audio_sensor_setup, container, false);

                if (mSensor != null) {
                    updateAudioSensorSetupView(mSensor);
                }

                break; 

            case AbstractSensor.CAMERA_SENSOR:
                // TODO: camera sensor
                Log.i("SensorDataCollector", "ToDo: Camera sensor.");
                mView = inflater.inflate(R.layout.fragment_sensor_setup, container, false);
                break;
            case AbstractSensor.WIFI_SENSOR:
                // TODO: WiFi sensor
                Log.i("SensorDataCollector", "Todo: WiFi RSSI sensor.");
                mView = inflater.inflate(R.layout.fragment_sensor_setup, container, false);
                break;
            case AbstractSensor.BLUETOOTH_SENSOR:
                // TODO: Bluetooth sensor
                Log.i("SensorDataCollector", "Todo: Bluetooth RSSI sensor.");
                mView = inflater.inflate(R.layout.fragment_sensor_setup, container, false);
                break;
            default:
                Log.e("SensorDataCollector", "Invalid major sensor type = " + mSensor.getMajorType());

                mView = inflater.inflate(R.layout.fragment_sensor_setup, container, false);
                
                String strWarnMsgFormatter = mView.getResources().getString(
                        R.string.text_sensor_type_x_not_handled);
                String strWargMsg = String.format(strWarnMsgFormatter, mSensor.getMajorType());
                
                Toast.makeText(getActivity(), strWargMsg,  Toast.LENGTH_SHORT).show();
                break;
        }
        
        return mView;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        
        mAudioRecordSettingDataSource.close();        
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: handle configuration changes
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnClickedListener");
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Button) mView.findViewById(R.id.button_sensor_setup_set_sampling_rate))
                .setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        mCallback.onBtnSetSamplingRateClicked_SensorSetupFragment(v, mSensor);
                    }
                });
    }
    
    public void setSensor(AbstractSensor sensor) {
        mSensor = sensor;
    }

    private void updateAndroidSensorSetupView(AbstractSensor sensor) {
        TextView tv = (TextView) mView.findViewById(R.id.textview_sensor_setup_sensor_name);
        tv.setText(sensor.getName());
        
        Log.i("SensorDataCollector", "Android sensor.");

        UserInterfaceUtil
                .fillSensorProperties(getActivity(), (ListView) mView
                        .findViewById(R.id.listview_sensor_setup_sensor_properties), sensor);

        EditText etSamplingRate = (EditText) mView
                .findViewById(R.id.edittext_sensor_steup_sampling_rate);
        Button btnSetSamplingRate = (Button) mView
                .findViewById(R.id.button_sensor_setup_set_sampling_rate);
        TextView tvSamplingRate = (TextView) mView
                .findViewById(R.id.textview_sensor_setup_sampling_rate);

        if (((AndroidSensor) sensor).isStreamingSensor()) {
            etSamplingRate.setText(String.valueOf(1000000. / ((AndroidSensor) sensor)
                    .getSamplingInterval()));
            etSamplingRate.setEnabled(true);
            tvSamplingRate.setEnabled(true);
            etSamplingRate.setVisibility(View.VISIBLE);
            tvSamplingRate.setVisibility(View.VISIBLE);
            btnSetSamplingRate.setVisibility(View.VISIBLE);
            Log.i("SensorDataCollector", "Streaming sensor.");
        } else {
            etSamplingRate.setText(getActivity().getResources().getString(
                    R.string.text_not_applicable));
            etSamplingRate.setEnabled(false);
            tvSamplingRate.setEnabled(false);
            etSamplingRate.setVisibility(View.GONE);
            tvSamplingRate.setVisibility(View.GONE);
            btnSetSamplingRate.setVisibility(View.GONE);
            Log.i("SensorDataCollector", "Non-streaming (onchange) sensor.");
        }
    }
    
    private void updateAudioSensorSetupView(AbstractSensor sensor) {
        if (!mAudioRecordSettingDataSource.isDataSourceReady()) {
            // add progress wheel
            // if false, disable audio recording
            mAudioRecordSettingDataSource.prepareDataSource();
        }
        mAudioRecordParameters = mAudioRecordSettingDataSource.getAllAudioRecordParameters();
        ((AudioSensor) mSensor).setAudioRecordParameter(mAudioRecordParameters.get(0));

        TextView tv = (TextView) mView.findViewById(R.id.textview_sensor_setup_sensor_name);
        tv.setText(sensor.getName());

        ListView listView = (ListView) mView.findViewById(R.id.listview_audio_sensor_setup);
        
        UserInterfaceUtil.fillSensorProperties(getActivity(), listView, sensor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                SensorProperty property = (SensorProperty) listView.getItemAtPosition(position);
                
                if (property.getName().equals(getActivity().getResources().getString(R.string.text_audio_source))) {
                    showAudioSensorDialog(mAudioRecordSettingDataSource, mAudioRecordParameters, mAudioRecordParameters.get(0), AudioSensorSetupDiaglogFragment.SELECT_SOURCE);
                } else if (property.getName().equals(getActivity().getResources().getString(R.string.text_audio_channel_in))) {
                    // TODO    
                } else if (property.getName().equals(getActivity().getResources().getString(R.string.text_audio_encoding))) {
                    // TODO    
                } else if (property.getName().equals(getActivity().getResources().getString(R.string.text_audio_sampling_rate))) {
                    // TODO    
                } else if (property.getName().equals(getActivity().getResources().getString(R.string.text_audio_min_buffer_size))) {
                    // TODO    
                }
            }
        });
    }
    
    private void showAudioSensorDialog(AudioRecordSettingDataSource dbSource,
            List<AudioRecordParameter> allParams, AudioRecordParameter param, int operation) {
        DialogFragment newFragment = AudioSensorSetupDiaglogFragment.newInstance(dbSource,
                allParams, param, operation);
        
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        newFragment.show(fm, "dialog");   
    }
}
