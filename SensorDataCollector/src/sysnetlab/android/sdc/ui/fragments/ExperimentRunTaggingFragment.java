
package sysnetlab.android.sdc.ui.fragments;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class ExperimentRunTaggingFragment extends Fragment {
    private View mView;
    private OnFragmentClickListener mCallback;
    private Activity mActivity;

    public interface OnFragmentClickListener {
        public void onTagClicked_ExperimentRunTaggingFragment(View view, int position);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridView gv = (GridView) mView.findViewById(R.id.gridview_experiment_tagging);

        gv.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                mCallback.onTagClicked_ExperimentRunTaggingFragment(view, position);
            }

        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;

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
        mView = super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_tagging, container, false);

        ArrayList<Tag> tags = ((CreateExperimentActivity) getActivity()).getExperiment().getTags();

        GridView gridview = (GridView) mView.findViewById(R.id.gridview_experiment_tagging);

        ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(mActivity,
                android.R.layout.simple_list_item_1, tags)/* {
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView.setTag(CreateExperimentActivity.BUTTON_TAG_STATE_KEY,
                        CreateExperimentActivity.BUTTON_TAG_STATE_OFF);
                return convertView;
            }
        }*/;

        gridview.setAdapter(adapter);
        
        gridview.setOnItemClickListener(new GridView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> gridview, View view, int position, long id) {
                mCallback.onTagClicked_ExperimentRunTaggingFragment(view, position);
            }
        });
        

        return mView;
    }
}
