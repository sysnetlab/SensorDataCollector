
package sysnetlab.android.sdc.ui.fragments;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.ui.adaptors.ExperimentListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import sysnetlab.android.sdc.loaders.ExperimentListLoader;

public class ExperimentListFragment extends ListFragment
	implements LoaderManager.LoaderCallbacks<List<Experiment>>{

    private OnFragmentClickListener mCallback;
    private View mHeaderView;
    private View mFooterView;
    //private ArrayAdapter<Experiment> mExperimentListAdapter;
    private ExperimentListAdapter mExperimentListAdapter;
    
    public interface OnFragmentClickListener {
        public void onExperimentClicked_ExperimentListFragment(Experiment experiment);

        public void onCreateExperimentButtonClicked_ExperimentListFragment(Button b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        // TODO: handle configuration changes
        LayoutInflater inflator = getLayoutInflater(savedInstanceState);
        mFooterView = inflator.inflate(R.layout.experiment_list_footer, null);
        mHeaderView = inflator.inflate(R.layout.experiment_list_header, null);
        Log.d("SensorDataCollector", "called ExperimentListFragment::onCreate().");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentClickedListener");
        }
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        mCallback.onExperimentClicked_ExperimentListFragment((Experiment) lv
                .getItemAtPosition(position));
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("SensorDataCollector", "ExperimentListFragment::onActivityCreated() entered ...");
        super.onActivityCreated(savedInstanceState);

        // TODO: handle configuration changes
        getListView().addHeaderView(mHeaderView);
        getListView().addFooterView(mFooterView);
        
        mExperimentListAdapter=new ExperimentListAdapter(
        		getActivity());//,
        		//ExperimentManagerSingleton.getInstance().getExperimentsSortedByDate()
        		//);
        //mExperimentListAdapter.setList(ExperimentManagerSingleton.getInstance().getExperimentsSortedByDate());
        setListAdapter(mExperimentListAdapter);
        
        ((Button) mHeaderView.findViewById(R.id.button_create_experiment))
                .setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        mCallback
                                .onCreateExperimentButtonClicked_ExperimentListFragment((Button) v);
                    }
                });
        getActivity().getSupportLoaderManager().initLoader(0,  null, this).forceLoad();
    }

    public ArrayAdapter<Experiment> getExperimentListAdapter() {
        return mExperimentListAdapter;
    }
    
    public void reloadExperiments(Experiment experiment){    	
    		mExperimentListAdapter.insert(experiment, 0);
    		mExperimentListAdapter.notifyDataSetChanged();
    }
    
    @Override
	public Loader<List<Experiment>> onCreateLoader(int id, Bundle args) {
		return new ExperimentListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Experiment>> loader,
			List<Experiment> data) {
		mExperimentListAdapter.setList(data);
	}

	@Override
	public void onLoaderReset(Loader<List<Experiment>> loader) {
		
	}
	
}
