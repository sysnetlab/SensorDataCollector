
package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class ViewExperimentActivity extends FragmentActivity implements
        ExperimentViewFragment.OnFragmentClickListener,
        ExperimentSensorListFragment.OnFragmentClickListener,
        ExperimentViewSensorDataFragment.OnFragmentClickListener,
        ExperimentViewNotesFragment.OnFragmentClickListener,
        ExperimentViewMoreNotesFragment.OnFragmentClickListener {
    private ExperimentViewFragment mExperimentViewFragment;
    private ExperimentViewMoreNotesFragment mExperimentViewMoreNotesFragment;
    private ExperimentViewSensorDataFragment mExperimentViewSensorDataFragment;
    private Experiment mExperiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO handle configuration change
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        // mExperiment = (Experiment)
        // getIntent().getParcelableExtra("experiment");

        mExperiment = ExperimentManagerSingleton.getInstance().getActiveExperiment();

        if (mExperiment == null) {
            Log.i("SensorDataColelctor",
                    "ViewExperimentActivity failed to get experiment from intent");
        }

        Log.i("SensorDataCollector",
                "ViewExperimentActivity: experiment is " + mExperiment.toString());

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            mExperimentViewFragment = new ExperimentViewFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.add(R.id.fragment_container, mExperimentViewFragment);
            transaction.commit();
        }

        Log.i("SensorDataCollector", "ViewExperimentActivity.onCreate called.");
    }

    @Override
    public void onBtnBackClicked_ExperimentViewFragment() {
        Intent intent = new Intent(this, SensorDataCollectorActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBtnCloneClicked_ExperimentViewFragment() {
        // TODO Auto-generated method stub

    }

    public Experiment getExperiment() {
        return mExperiment;
    }

    @Override
    public void onImageViewMoreOrLessNotesClicked_ExperimentViewNotesFragment() {
        Log.i("SensorDataCollector",
                "entered onImageViewMoreOrLessNotesClicked_ExperimentViewNotesFragment");

        if (mExperimentViewMoreNotesFragment == null) {
            mExperimentViewMoreNotesFragment = new ExperimentViewMoreNotesFragment();
        }
        FragmentUtil
                .switchToFragment(this, mExperimentViewMoreNotesFragment, "experimentviewnotes");
    }

    @Override
    public void onBtnBackClicked_ExperimentViewMoreNotesFragment() {
        FragmentUtil.switchToFragment(this, mExperimentViewFragment, "experimentview");
    }

    @Override
    public void onListItemClicked_ExperimentSensorListFragment(int sensorNo) {
        if (mExperimentViewSensorDataFragment == null) {
            mExperimentViewSensorDataFragment = new ExperimentViewSensorDataFragment();
        }
        
        mExperimentViewSensorDataFragment.setSensorNo(sensorNo);
        
        FragmentUtil.switchToFragment(this, mExperimentViewSensorDataFragment,
                "experimentviewsensordata");
    }

    @Override
    public void onBtnBackClicked_ExperimentViewSensorDataFragment() {
        FragmentUtil.switchToFragment(this, mExperimentViewFragment, "experimentview");
    }

}
