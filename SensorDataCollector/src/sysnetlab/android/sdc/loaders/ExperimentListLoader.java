/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package sysnetlab.android.sdc.loaders;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;
import android.widget.ProgressBar;
import android.app.Activity;
import android.content.Context;

public class ExperimentListLoader extends AsyncTaskLoader<List<Experiment>>{
	
	ProgressBar mProgressBar;
	
	public ExperimentListLoader(Context context) {
		super(context);
		((Activity)context).findViewById(R.id.layout_progressbar_loading).setVisibility(View.VISIBLE);
		mProgressBar=(ProgressBar) ((Activity)context).findViewById(R.id.progressbar_task_in_progress);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public List<Experiment> loadInBackground() {
		List<Experiment> allExperiments = ExperimentManagerSingleton
				.getInstance()
				.getExperiments(mProgressBar);
        Collections.sort(allExperiments, new Comparator<Experiment>() {
            public int compare(Experiment e1, Experiment e2) {
                return -(e1.getDateTimeCreated().compareTo(e2.getDateTimeCreated()));
            }
        });
        return allExperiments;
	}
	
	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		mProgressBar.setMax(
				ExperimentManagerSingleton.getInstance().getCountExperiments());
	}

	@Override
	public void deliverResult(List<Experiment> data) {
		mProgressBar.setVisibility(View.GONE);
		super.deliverResult(data);
	}
}
