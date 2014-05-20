
package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CreateExperimentNotesFragment extends Fragment {
	
	private OnFragmentClickListener mCallback;
    private View mView;    
    
    public interface OnFragmentClickListener {            	
    	public void onBtnBackClicked_CreateExperimentNotesFragment();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_note_creation, container,
                false);        
        ((TextView) mView.findViewById(R.id.textview_fragment_note_viewing_note))
                .setText("This is a note.");

        return mView;
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
    
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
   	   	   
    	
    	((Button)mView.findViewById(R.id.btnBack))
    	.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			mCallback.onBtnBackClicked_CreateExperimentNotesFragment();
    		}
    	});
    }  
}
