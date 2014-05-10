package sysnetlab.android.sdc.ui;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class ExperimentRunTaggingFragment extends Fragment {
	private View mView;
	private OnFragmentClickListener mCallback;
	private Activity mActivity;
	
	public interface OnFragmentClickListener {
        public void onTagClicked_ExperimentRunTaggingFragment(int position);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	    GridView gv = (GridView)mView.findViewById(R.id.gridview_experiment_tagging);
	    
	    gv.setOnItemClickListener(new GridView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				mCallback.onTagClicked_ExperimentRunTaggingFragment(position); 
			}
	    	
	    });
		
		
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = activity;
		
        try {
            mCallback = (OnFragmentClickListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExperimentRunTaggingFragment.OnFragmentClickedListener");
        }			
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = super.onCreateView(inflater, container, savedInstanceState);
		
        mView = inflater.inflate(R.layout.fragment_experiment_tagging, container, false);
	        
        
		// Temporary testing data
		ArrayList<String> tmpTags = new ArrayList<String>();
	    
	    tmpTags.add("Tag001"); 
	    tmpTags.add("Tag002"); 
	    tmpTags.add("Tag003");
	    tmpTags.add("Tag004");
	    
	    GridView gv = (GridView)mView.findViewById(R.id.gridview_experiment_tagging);

	    ArrayAdapter<String> adapter = 
	    		new ArrayAdapter<String>(mActivity, 
	    				android.R.layout.simple_list_item_1, 
	    				tmpTags);
	    
	    gv.setAdapter(adapter);
	    
		return mView;
	}
}
