
package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class ViewExperimentActivity extends FragmentActivity implements
        ExperimentViewFragment.OnFragmentClickListener,
        ExperimentSensorSelectionFragment.OnFragmentClickListener {
    private ExperimentViewFragment mExperimentViewFragment;
    private Experiment mExperiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO handle configuration change
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        // mExperiment = (Experiment) getIntent().getParcelableExtra("experiment");
        mExperiment =  ExperimentManagerSingleton.getInstance().getActiveExperiment();
        
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
    public void onSensorClicked_ExperimentSensorSelectionFragment(AndroidSensor sensor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBtnConfirmClicked_ExperimentSensorSelectionFragment() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onBtnClearClicked_ExperimentSensorSelectionFragment() {
        // TODO Auto-generated method stub
        
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

}
