
package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datastore.SimpleFileStoreSingleton;
import sysnetlab.android.sdc.sensor.SensorUtilSingleton;
import sysnetlab.android.sdc.ui.fragments.ExperimentListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Button;

public class SensorDataCollectorActivity extends FragmentActivity implements
        ExperimentListFragment.OnFragmentClickListener {
    
    public final static String APP_OPERATION_KEY = "operation";
    public final static int APP_OPERATION_CREATE_NEW_EXPERIMENT = 1;
    public final static int APP_OPERATION_CLONE_EXPERIMENT = 2;
    
    private ExperimentListFragment mExperimentListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        ExperimentManagerSingleton.getInstance().addExperimentStore(
                SimpleFileStoreSingleton.getInstance());

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            mExperimentListFragment = new ExperimentListFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.add(R.id.fragment_container, mExperimentListFragment);
            transaction.commit();
        }

        SensorUtilSingleton.getInstance().setContext(getBaseContext());
    }

    public void onStart() {
        super.onStart();
    }

    @Override
    public void onExperimentClicked_ExperimentListFragment(Experiment experiment) {
        Log.i("SensorDataCollector", "Creating ViewExperimentActivity ...");

        Intent intent = new Intent(this, ViewExperimentActivity.class);

        // intent.putExtra("experiment", experiment);

        ExperimentManagerSingleton.getInstance().setActiveExperiment(experiment);

        startActivity(intent);
    }

    @Override
    public void onCreateExperimentButtonClicked_ExperimentListFragment(Button b) {
        Log.i("SensorDataCollector", "Creating CreateExperimentActivity ...");
        
        Intent intent = new Intent(this, CreateExperimentActivity.class);
        intent.putExtra(SensorDataCollectorActivity.APP_OPERATION_KEY,
                SensorDataCollectorActivity.APP_OPERATION_CREATE_NEW_EXPERIMENT);
        startActivity(intent);
    }

    public ExperimentListFragment getExperimentListFragment() {
        return mExperimentListFragment;
    }

}
