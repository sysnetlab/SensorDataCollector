
package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.ui.adaptors.SensorListAdaptor;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ExperimentSensorSelectionFragment extends Fragment {
    private boolean mHavingHeader;
    private boolean mHavingFooter;

    private View mView;
    private ListView mListView;
    private ArrayAdapter<AndroidSensor> mSensorList;

    private OnFragmentClickListener mCallback;

    public interface OnFragmentClickListener {
        public void onBtnConfirmClicked_ExperimentSensorSelectionFragment();

        public void onBtnClearClicked_ExperimentSensorSelectionFragment();

        public void onSensorClicked_ExperimentSensorSelectionFragment(AndroidSensor sensor);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("SensorDataCollector",
                "ExperimentSensorSelectionFragment::onCreate(): " +
                        "entering ......");
        mHavingHeader = false;
        mHavingFooter = false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i("SensorDataCollector",
                "ExperimentSensorSelectionFragment::onCreateView(): " +
                        "entering with mHavingHeader = " + mHavingHeader);
        super.onCreateView(inflater, container, savedInstanceState);

        // inflate the fragment
        mView = inflater.inflate(R.layout.fragment_sensor_selection, container, false);

        mHavingHeader = getActivity().getIntent().getBooleanExtra("havingheader", false);
        mHavingFooter = getActivity().getIntent().getBooleanExtra("havingfooter", false);

        if (mHavingHeader) {
            RelativeLayout layout = (RelativeLayout) mView
                    .findViewById(R.id.layout_sensor_selection_header);
            layout.setVisibility(View.VISIBLE);
        }

        if (mHavingFooter) {
            RelativeLayout layout = (RelativeLayout) mView
                    .findViewById(R.id.layout_sensor_selection_footer);
            layout.setVisibility(View.VISIBLE);
        }

        // find the container for the list
        RelativeLayout layout = (RelativeLayout) mView
                .findViewById(R.id.layout_sensor_selection_list);

        mListView = new ListView(getActivity());
        mSensorList = new SensorListAdaptor(getActivity(),
                SensorDiscoverer.discoverSensorList(getActivity()));
        mListView.setAdapter(mSensorList);
        layout.addView(mListView);

        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            Log.e("SensorDataCollector",
                    "ExperimentSensorSelectionFragment::onAttach(): failed to cast Activity to OnFragmentClickListener.");
            e.printStackTrace();
            throw new RuntimeException("Failed to cast Activity to OnFragmentClickListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("SensorDataCollector", "ExperimentSensorSelection::onActivityCreated() called.");

        if (mHavingFooter) {

            try {
                if (mHavingFooter)
                    mCallback = (OnFragmentClickListener) getActivity();
                else
                    mCallback = null;
            } catch (ClassCastException e) {
                Log.e("SensorDataCollector",
                        "ExperimentSensorSelectionFragment::onAttach(): failed to cast Activity to OnFragmentClickListener.");
                e.printStackTrace();
                throw new RuntimeException("Failed to cast Activity to OnFragmentClickListener");
            }

            ((Button) mView.findViewById(R.id.button_sensor_selection_confirm))
                    .setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCallback.onBtnConfirmClicked_ExperimentSensorSelectionFragment();
                        }
                    });

            ((Button) mView.findViewById(R.id.button_sensor_selection_clear))
                    .setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCallback.onBtnClearClicked_ExperimentSensorSelectionFragment();
                        }
                    });
        }

        // ((ListView) mView.findViewById(R.id.layout_sensor_list))
        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                mCallback.onSensorClicked_ExperimentSensorSelectionFragment(
                        (AndroidSensor) listView.getItemAtPosition(position));
            }
        });        
    }

    public ArrayAdapter<AndroidSensor> getSensorListAdapter() {
        return mSensorList;
    }

}
