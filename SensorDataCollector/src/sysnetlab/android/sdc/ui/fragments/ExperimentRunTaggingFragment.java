
package sysnetlab.android.sdc.ui.fragments;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.adaptors.TaggingTagListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class ExperimentRunTaggingFragment extends Fragment {
    private View mView;
    private OnFragmentClickListener mCallback;

    public interface OnFragmentClickListener {
        public void onTagClicked_ExperimentRunTaggingFragment(AdapterView<?> gridview, View view, int position);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridView gridview = (GridView) mView.findViewById(R.id.gridview_experiment_tagging);

        gridview.setOnItemClickListener(new GridView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> gridview, View view, int position, long id) {
                mCallback.onTagClicked_ExperimentRunTaggingFragment(gridview, view, position);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExperimentRunTaggingFragment.OnFragmentClickedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_tagging, container, false);

        ArrayList<Tag> tags = ((CreateExperimentActivity) getActivity()).getExperiment().getTags();

        GridView gridview = (GridView) mView.findViewById(R.id.gridview_experiment_tagging);
        
        TaggingTagListAdapter taggingAdapter = new TaggingTagListAdapter(getActivity(), tags);

        gridview.setAdapter(taggingAdapter);

        return mView;
    }
}
