package sysnetlab.android.sdc.ui;

import java.util.ArrayList;
import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Tag;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;

public class ExperimentEditTagsFragment extends Fragment {
	private OnFragmentEventListener mCallback;
    private View mView;
    // private List<String> mExperimenTags;
    private Activity mActivity;

    public interface OnFragmentEventListener {
    	public void onTxtFldEnterPressed_ExperimentTagsFragment(String newTag);
    }    
    

    @Override 
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

    	//TODO: handle configuration changes 
    	
        mView = inflater.inflate(R.layout.fragment_experiment_tag_editing, container, false);
        
    	ListView lv = (ListView) mView.findViewById(R.id.listView_tags);
    	
    	ArrayList<Tag> tmpTags;
    	Activity activity = getActivity();
    	
    	if (activity instanceof CreateExperimentActivity)
    		tmpTags = ((CreateExperimentActivity) activity).getExperiment().getTags();
    	else
    		tmpTags = ((ViewExperimentActivity) activity).getExperiment().getTags();
    	ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(mActivity, android.R.layout.simple_list_item_1, tmpTags);
    	
    	Log.i("SensorDataCollector", "ExperimentTagsFragment.onCreateView: lv = " + lv);
    	Log.i("SensorDataCollector", "ExperimentTagsFragment.onCreateView: adapter = " + adapter);
    	
    	if (activity instanceof CreateExperimentActivity)
    		lv.setAdapter(adapter);
    	else
    		Log.i("SensorDataCollector", "not yet implemented for ViewExperimentActivity");
	    
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }    

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    	//TODO: handle configuration changes 
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;

        try {
            mCallback = (OnFragmentEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentClickListener");
        }
    }
    
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	((EditText) mView.findViewById(R.id.edittext_new_label))
    	.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Log.i("ExperimentTagsFragment", "Enter Pressed");
				mCallback.onTxtFldEnterPressed_ExperimentTagsFragment(v.getText().toString());
				v.setText("");
				return true;
			}
    	});

    }
    
}
