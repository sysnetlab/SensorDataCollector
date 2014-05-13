package sysnetlab.android.sdc.ui;

import java.util.ArrayList;
import java.util.List;

import sysnetlab.android.sdc.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;

public class ExperimentTagsFragment extends Fragment {
	private OnFragmentEventListener mCallback;
    private View mView;
    private List<String> mExperimenTags;

    public interface OnFragmentEventListener {
    	public void onTxtFldEnterPressed_ExperimentTagsFragment();
    	public void onBtnLabelClicked_ExperimentTagsFragment();
    }    
    
    @Override 
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	// Load the assigned labels here.
    	mExperimenTags = new ArrayList<String>();
    	mExperimenTags.add("test1");
    	mExperimenTags.add("test2");
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

    	//TODO: handle configuration changes 
    	
        mView = inflater.inflate(R.layout.fragment_experiment_tag_editing, container, false);
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

        try {
            mCallback = (OnFragmentEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentClickListener");
        }
    }
    
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	LinearLayout labelList = (LinearLayout) mView.findViewById(R.id.layout_label_list);
    	for (String label : mExperimenTags) {
    		Button btnLabel = new Button(mView.getContext());
    		btnLabel.setMinimumHeight(0);
    		btnLabel.setText(label);
    		labelList.addView(btnLabel);
    	}

    	((EditText) mView.findViewById(R.id.edittext_new_label))
    	.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Log.i("ExperimentTagsFragment", "Enter Pressed");
				mCallback.onTxtFldEnterPressed_ExperimentTagsFragment();
				return true;
			}
    	});

    }
    
}
