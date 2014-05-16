
package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.ui.adaptors.ExperimentListAdaptor;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ExperimentListFragment extends ListFragment {

    private OnFragmentClickListener mCallback;
    private View mHeaderView;
    private View mFooterView;
    private ArrayAdapter<Experiment> mExperimentList;

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

        mExperimentList = new ExperimentListAdaptor(getActivity(),
                ExperimentManagerSingleton.getInstance().getExperiments());
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
        super.onActivityCreated(savedInstanceState);

        // TODO: handle configuration changes
        getListView().addHeaderView(mHeaderView);
        getListView().addFooterView(mFooterView);
        setListAdapter(mExperimentList);

        ((Button) mHeaderView.findViewById(R.id.buttonCreateExperiment))
                .setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        mCallback
                                .onCreateExperimentButtonClicked_ExperimentListFragment((Button) v);
                    }
                });
    }

    public ArrayAdapter<Experiment> getExperimentArray() {
        return mExperimentList;
    }
}
