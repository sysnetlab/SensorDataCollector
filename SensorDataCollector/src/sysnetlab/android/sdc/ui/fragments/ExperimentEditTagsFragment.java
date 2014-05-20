
package sysnetlab.android.sdc.ui.fragments;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import sysnetlab.android.sdc.ui.adaptors.TagListAdapter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.Activity;

public class ExperimentEditTagsFragment extends Fragment {
    private OnFragmentClickListener mCallback;
    private View mView;
    private TagListAdapter mTagListAdapter;
    
    public interface OnFragmentClickListener {
    	public void onBtnConfirmClicked_ExperimentEditTagsFragment();
    	public void onBtnAddTagClicked_ExperimentEditTagsFragment(String strTag, String strDescription);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // TODO: handle configuration changes

        mView = inflater.inflate(R.layout.fragment_experiment_tag_editing, container, false);

        ListView lv = (ListView) mView.findViewById(R.id.listview_tags);    
        
        ArrayList<Tag> tmpTags;
        Activity activity = getActivity();
        

        if (activity instanceof CreateExperimentActivity) {
            tmpTags = ((CreateExperimentActivity) activity).getExperiment().getTags();
        }  else {
            tmpTags = ((ViewExperimentActivity) activity).getExperiment().getTags();
        }
        mTagListAdapter = new TagListAdapter(activity, tmpTags);     
        
        Log.i("SensorDataCollector", "ExperimentEditTagsFragment.onCreateView: tmpTags = " + tmpTags);
        Log.i("SensorDataCollector", "ExperimentEditTagsFragment.onCreateView: lv = " + lv);
        Log.i("SensorDataCollector", "ExperimentEditTagsFragment.onCreateView: adapter = " + mTagListAdapter);

        if (activity instanceof CreateExperimentActivity){
        	lv.setAdapter(mTagListAdapter);
        }
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
        // TODO: handle configuration changes
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

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Button) mView.findViewById(R.id.btn_add_tag))
        .setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ExperimentEditTagsFragment", "Add Tag Clicked");
                EditText editTextTag = (EditText) mView.findViewById(R.id.edittext_tag);
                EditText editTextDescription = (EditText) mView.findViewById(R.id.edittext_description);
                mCallback.onBtnAddTagClicked_ExperimentEditTagsFragment(editTextTag.getText().toString(),
                		editTextDescription.getText().toString());
                mTagListAdapter.notifyDataSetChanged();
                editTextTag.setText("");
                editTextDescription.setText("");
            }
        });
        
        ((Button) mView.findViewById(R.id.btn_tag_edit_confirm))
        .setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onBtnConfirmClicked_ExperimentEditTagsFragment();
            }
        });

    }
    
    public ArrayAdapter<Tag> getTagListAdapter(){
    	return mTagListAdapter;
    }    
}
