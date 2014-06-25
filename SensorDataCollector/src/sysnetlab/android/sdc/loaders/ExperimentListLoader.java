package sysnetlab.android.sdc.loaders;

import java.util.List;

import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

public class ExperimentListLoader extends AsyncTaskLoader<List<Experiment>>{

	public ExperimentListLoader(Context context) {
		super(context);
	}

	@Override
	public List<Experiment> loadInBackground() {
        return ExperimentManagerSingleton.getInstance().getExperimentsSortedByDate();
	}
	
	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		//TODO: display the spinning wheel
	}

	@Override
	public void deliverResult(List<Experiment> data) {
		// TODO Auto-generated method stub
		super.deliverResult(data);
	}
}
