
package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.ui.fragments.ExperimentSensorListFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentViewFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentViewNotesFragment;
import sysnetlab.android.sdc.ui.fragments.ExperimentViewSensorDataFragment;
import sysnetlab.android.sdc.ui.fragments.FragmentUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

public class ViewExperimentActivity extends FragmentActivity implements
        ExperimentViewFragment.OnFragmentClickListener,
        ExperimentSensorListFragment.OnFragmentClickListener {

    private ExperimentViewFragment mExperimentViewFragment;
    private ExperimentViewNotesFragment mExperimentViewNotesFragment;
    private ExperimentViewSensorDataFragment mExperimentViewSensorDataFragment;
    private Experiment mExperiment;

    public Experiment getExperiment() {
        return mExperiment;
    }   
    
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
            Toast.makeText(this, "Failed to  load experiment.", Toast.LENGTH_LONG).show();
            finish();
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
    public void onBtnCloneClicked_ExperimentViewFragment() {
        Intent intent = new Intent(this, CreateExperimentActivity.class);
        intent.putExtra(SensorDataCollectorActivity.APP_OPERATION_KEY,
                SensorDataCollectorActivity.APP_OPERATION_CLONE_EXPERIMENT);
        startActivity(intent);
    }
    

    @Override
    public void onTagsClicked_ExperimentViewFragment() {
        // TODO Auto-generated method stub
        // choose to do nothing. 
    }

    @Override
    public void onNotesClicked_ExperimentViewFragment() {
        if (mExperimentViewNotesFragment == null) {
            mExperimentViewNotesFragment = new ExperimentViewNotesFragment();
        }
        FragmentUtil.switchToFragment(this, mExperimentViewNotesFragment,
                "experimentviewmorenotes");

    }

    @Override
    public void onSensorsClicked_ExperimentViewFragment() {
        if (mExperimentViewSensorDataFragment == null) {
            mExperimentViewSensorDataFragment = new ExperimentViewSensorDataFragment();
        }
        FragmentUtil.switchToFragment(this, mExperimentViewSensorDataFragment,
                "experimentviewsensordata");
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
    
    public ExperimentViewFragment getExperimentViewFragment(){
    	return mExperimentViewFragment;
    }

}
