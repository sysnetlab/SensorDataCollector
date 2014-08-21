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

package sysnetlab.android.sdc.datacollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.widget.ProgressBar;
import sysnetlab.android.sdc.datastore.AbstractStore;

public class ExperimentManager {
    private List<AbstractStore> mStores;
    private Experiment mActiveExperiment;

    public ExperimentManager() {
        mStores = new ArrayList<AbstractStore>();
    }

    public void addExperimentStore(AbstractStore store) {
        if (!mStores.contains(store))
            mStores.add(store);
    }

    public List<Experiment> getExperiments() {
        List<Experiment> allExperiments = new ArrayList<Experiment>();
        for (AbstractStore store : mStores) {
            List<Experiment> experiments = store.listStoredExperiments();
            allExperiments.addAll(experiments);
        }
        return allExperiments;
    }

    public List<Experiment> getExperimentsSortedByDate() {
        List<Experiment> allExperiments = getExperiments();
        Collections.sort(allExperiments, new Comparator<Experiment>() {
            public int compare(Experiment e1, Experiment e2) {
                return -(e1.getDateTimeCreated().compareTo(e2.getDateTimeCreated()));
            }
        });

        return allExperiments;
    }

    public void setActiveExperiment(Experiment experiment) {
        mActiveExperiment = experiment;
    }

    public Experiment getActiveExperiment() {
        return mActiveExperiment;
    }

    public List<AbstractStore> getStores() {
        return mStores;
    }

    public int getCountExperiments() {
        int count = 0;
        for (AbstractStore store : mStores) {
            count += store.getCountExperiments();
        }
        return count;
    }

    public List<Experiment> getExperiments(ProgressBar mProgressBar) {
        List<Experiment> allExperiments = new ArrayList<Experiment>();
        for (AbstractStore store : mStores) {
            List<Experiment> experiments = store.listStoredExperiments(mProgressBar);
            allExperiments.addAll(experiments);
        }
        return allExperiments;
    }
}
