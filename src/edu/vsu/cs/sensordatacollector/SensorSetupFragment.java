/* $Id$ */
package edu.vsu.cs.sensordatacollector;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;

public class SensorSetupFragment extends Fragment {
	private OnClickListener mCallback;
    private View mView;
    private VirtualSensor mSensor;

    public interface OnClickListener {
    	public void onBtnConfirmClicked(View v, VirtualSensor sensor);
    	public void onBtnCancelClicked();
    }    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

    	//TODO: handle configuration changes 
    	
        mView = inflater.inflate(R.layout.fragment_sensor_setup, container, false);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mSensor != null) {
        	updateSensorSetupView(mSensor);
        } 
    }    

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    	//TODO: handle configuration changes 
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnClickListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnClickedListener");
        }
    }
    
    
    public void setSensor(VirtualSensor sensor) {
    	mSensor = sensor;
    }
    
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);

    	((Button)mView.findViewById(R.id.btn_sensor_setup_confirm))
    	.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			mCallback.onBtnConfirmClicked(v, mSensor);
    		}
    	});

    	((Button)mView.findViewById(R.id.btn_sensor_setup_cancel))
    	.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			mCallback.onBtnCancelClicked();
    		}
    	});
    }
   
    @SuppressLint("NewApi")
	private void updateSensorSetupView(VirtualSensor sensor) {
    	TextView tv = (TextView)mView.findViewById(R.id.tv_sensor_setup_header);
    	tv.setText(sensor.getName());
    	switch(sensor.getMajorType()) {
    	case VirtualSensor.ANDROID_SENSOR: 
    		Log.i("SensorDataCollector", "Android sensor.");
    		
			TextView tvSensingType = (TextView)mView.findViewById(R.id.tv_sensing_type);
			EditText etSamplingRate = (EditText)mView.findViewById(R.id.edittext_sampling_rate);
			TextView tvSamplingRate = (TextView)mView.findViewById(R.id.tv_sampling_rate);			
    		if (((AndroidSensor)sensor).isStreamingSensor()) {
				tvSensingType.setText(getActivity().getResources().getString(R.string.text_sensing_type_streaming));
    			etSamplingRate.setText(String.valueOf(1000000./((AndroidSensor)sensor).getSamplingInterval()));
    			Log.i("SensorDataCollector", "Streaming sensor.");		
    		} else {
				tvSensingType.setText(getActivity().getResources().getString(R.string.text_sensing_type_onchange));    			
    			etSamplingRate.setText(getActivity().getResources().getString(R.string.text_sensing_rate_na));
    			etSamplingRate.setEnabled(false);
    			tvSamplingRate.setEnabled(false);
    			Log.i("SensorDataCollector", "Non-streaming (onchange) sensor."); 
    		}
    		break;
    	case VirtualSensor.AUDIO_SENSOR:
    		// TODO: audio sensor
    		Log.i("SensorDataCollector", "ToDo: Audio sensor.");
    		break;
    	case VirtualSensor.CAMERA_SENSOR:
    		// TODO: camera sensor
    		Log.i("SensorDataCollector", "ToDo: Camera sensor.");
    		break;
    	case VirtualSensor.WIFI_SENSOR:
    		// TODO: WiFi sensor
    		Log.i("SensorDataCollector", "Todo: WiFi RSSI sensor.");
    		break;
    	case VirtualSensor.BLUETOOTH_SENSOR:
    		// TODO: Bluetooth sensor
    		Log.i("SensorDataCollector", "Todo: Bluetooth RSSI sensor.");
    		break;
    	default:
    		Log.e("SensorDataCollector", "Invalid major sensor type = " + sensor.getMajorType());
    		break;
    	}
    }
    
}
