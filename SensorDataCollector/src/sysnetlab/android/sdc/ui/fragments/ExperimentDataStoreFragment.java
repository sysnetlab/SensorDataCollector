package sysnetlab.android.sdc.ui.fragments;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.ui.adaptors.DataStoreListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;

public class ExperimentDataStoreFragment extends Fragment {
    private View mView;
    private int mItemChecked = -1;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_experiment_datastore_selecting, container, false);
        return mView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        ListView listView = (ListView) mView.findViewById(R.id.listview_experiment_datastore_selecting);
        
        DataStoreListAdapter storeAdapter = new DataStoreListAdapter(getActivity(), ExperimentManagerSingleton.getInstance().getStores(), mItemChecked);
        
        listView.setAdapter(storeAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mItemChecked == position) {
                    setItemSelected(view, true);
                } else {
                    if (mItemChecked >= 0) {
                        setItemSelected(parent.getChildAt(mItemChecked), false);
                    }
                    setItemSelected(view, true);
                    mItemChecked = position;
                }
            }
            
        });        
    }

    private void setItemSelected(View view, boolean isSelected) {
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.radiobutton_experiment_datastore_selecting);
        radioButton.setChecked(isSelected);
    }
    
    public void setStorePositionChecked(int position) {
        mItemChecked = position;
    }
    
    public int getStorePositionChecked() {
        return mItemChecked;
    }
}
