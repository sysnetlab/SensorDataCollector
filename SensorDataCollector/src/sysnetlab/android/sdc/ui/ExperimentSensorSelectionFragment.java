package sysnetlab.android.sdc.ui;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.ui.adaptors.SensorListAdaptor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ExperimentSensorSelectionFragment extends Fragment {
	private View mView;
    private ArrayAdapter<AndroidSensor> mSensorList;
    private ArrayList<View> mChildViewsInHeader;
    private ArrayList<View> mChildViewsInFooter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = super.onCreateView(inflater, container, savedInstanceState);
		
		// inflate the fragment
	    mView = inflater.inflate(R.layout.fragment_sensor_selection, container, false);

	    arrangeHeaderChildViews();
	    arrangeFooterChildViews();
	    
	    // find the container for the list
	    RelativeLayout layout = (RelativeLayout)mView.findViewById(R.id.layout_sensor_selection_list);    

	    
	    ListView listView = new ListView(getActivity());
	    mSensorList = new SensorListAdaptor(getActivity(), SensorDiscoverer.discoverSensorList(getActivity()));
	    listView.setAdapter(mSensorList);	    
	    layout.addView(listView);
	    
		return mView;
	}
	
	public void addViewToHeader(View v)
	{
		if (mChildViewsInHeader == null)
			mChildViewsInHeader = new ArrayList<View>();
		mChildViewsInHeader.add(v);
	}
	
	public void addViewToFooter(View v)
	{
		if (mChildViewsInFooter == null)
			mChildViewsInFooter = new ArrayList<View>();		
		mChildViewsInFooter.add(v);
	}
	
	private void arrangeHeaderChildViews() {
		if (mView == null || 
				mChildViewsInHeader == null ||
				mChildViewsInHeader.isEmpty()) return;
		
	    // find the container for the header
	    RelativeLayout layout = (RelativeLayout)mView.findViewById(R.id.layout_sensor_selection_header);
	    LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    
		for (View v : mChildViewsInHeader) {
			layout.addView(v, lp);
		}
	}

	private void arrangeFooterChildViews() {
		if (mView == null ||
				mChildViewsInFooter == null ||
				mChildViewsInFooter.isEmpty()) return;	
	    // find the container for the header
	    RelativeLayout layout = (RelativeLayout)mView.findViewById(R.id.layout_sensor_selection_header);
		
	    for (View v : mChildViewsInHeader) {
			layout.addView(v);
		}   
	}

}
