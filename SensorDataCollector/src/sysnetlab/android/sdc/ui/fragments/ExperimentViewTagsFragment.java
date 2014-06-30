
package sysnetlab.android.sdc.ui.fragments;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.ui.adaptors.TagListAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ExperimentViewTagsFragment extends Fragment {
    private View mView;
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_view_tags, container, false);
       
        ListView lv = (ListView) mView.findViewById(R.id.listview_fragment_experiment_view_tag_tag_properties); 
        Experiment experiment = ExperimentManagerSingleton.getInstance().getActiveExperiment();
       
        List<Tag> tags = experiment.getTags();
        TagListAdapter adaptTags = new TagListAdapter(getActivity(),tags);
        lv.setAdapter(adaptTags);
         
        String strHeadingSubTextFormatter = getResources().getString(
                R.string.text_for_experiment_name_x);
        String strHeadingSubText = String.format(strHeadingSubTextFormatter, experiment.getName());
        ((TextView) mView.findViewById(R.id.textview_experiment_tag_viewing_subtext))
                .setText(strHeadingSubText);

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_tag_experiment_time_created))
                .setText(experiment.getDateTimeCreatedAsString());

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_tags_experiment_time_done))
                .setText(experiment.getDateTimeDoneAsString());
  
        return mView;
    }
}
