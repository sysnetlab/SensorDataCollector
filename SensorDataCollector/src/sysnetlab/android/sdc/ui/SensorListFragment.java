/* $Id$ */
package sysnetlab.android.sdc.ui;


import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.DataSensorFactory;
import sysnetlab.android.sdc.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SensorListFragment extends ListFragment {
	
	private OnClickListener mCallback;
    private View mHeaderView;
    private View mFooterView;
    private ArrayAdapter<AndroidSensor> mSensorList;

    public interface OnClickListener {
        public void onSensorClicked(AndroidSensor sensor);
    	public void onBtnRunClicked(View v);
    	public void onBtnClearClicked();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	//TODO: handle configuration changes 
		LayoutInflater inflator = getLayoutInflater(savedInstanceState);
	    mFooterView = inflator.inflate(R.layout.sensor_list_footer, null);
	    mHeaderView = inflator.inflate(R.layout.sensor_list_header, null);	
        
	    mSensorList = new SensorListAdaptor(getActivity(), DataSensorFactory.getSensorList(getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getFragmentManager().findFragmentById(R.id.fragment_sensor_setup) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
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

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
    	if (DataCollectionState.getState() == DataCollectionState.DATA_COLLECTION_STOPPED)
    		mCallback.onSensorClicked((AndroidSensor)lv.getItemAtPosition(position));
    	else
    		Toast.makeText(getActivity(), "Data Collection In Progress!", Toast.LENGTH_SHORT).show();
    }
    
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);

    	//TODO: handle configuration changes 
    	getListView().addHeaderView(mHeaderView);
    	getListView().addFooterView(mFooterView);
    	setListAdapter(mSensorList);


    	((Button)mFooterView.findViewById(R.id.btnDataSensorsRun))
    	.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			mCallback.onBtnRunClicked(v);
    		}
    	});

    	((Button)mFooterView.findViewById(R.id.btnDataSensorsClear))
    	.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			mCallback.onBtnClearClicked();
    		}
    	});
    }   
    
    public SensorListAdaptor getSensorList()
    {
    	return (SensorListAdaptor) mSensorList;
    }
   
}
