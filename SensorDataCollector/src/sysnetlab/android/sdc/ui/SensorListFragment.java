/* $Id$ */
package sysnetlab.android.sdc.ui;


import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.ui.adaptors.SensorListAdaptor;
import sysnetlab.android.sdc.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SensorListFragment extends ListFragment {
	
	private OnFragmentClickListener mCallback;
    private View mHeaderView;
    private View mFooterView;
    private ArrayAdapter<AndroidSensor> mSensorList;
    boolean mStandalone; 

    public interface OnFragmentClickListener {
        public void onSensorClicked_SensorListFragment(AndroidSensor sensor);
    	public void onBtnRunClicked_SensorListFragment(View v);
    	public void onBtnClearClicked_SensorListFragment();
    	public void onBtnBackClicked_SensorListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	//TODO: handle configuration changes 
		LayoutInflater inflator = getLayoutInflater(savedInstanceState);
	    mFooterView = inflator.inflate(R.layout.sensor_list_footer, null);
	    mHeaderView = inflator.inflate(R.layout.sensor_list_header, null);	
        
	    mSensorList = new SensorListAdaptor(getActivity(), SensorDiscoverer.discoverSensorList(getActivity()));
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
            mCallback = (OnFragmentClickListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnClickedListener");
        }
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
		mCallback.onSensorClicked_SensorListFragment((AndroidSensor)lv.getItemAtPosition(position));
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
    			mCallback.onBtnRunClicked_SensorListFragment(v);
    		}
    	});

    	((Button)mFooterView.findViewById(R.id.btnDataSensorsClear))
    	.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			mCallback.onBtnClearClicked_SensorListFragment();
    		}
    	});
    	
    	((Button)mFooterView.findViewById(R.id.btnBack))
    	.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			mCallback.onBtnBackClicked_SensorListFragment();
    		}
    	});
    }   
   
}
