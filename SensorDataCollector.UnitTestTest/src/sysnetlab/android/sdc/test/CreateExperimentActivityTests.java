
package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import android.content.Context;
import android.content.Intent;

public class CreateExperimentActivityTests
        extends android.test.ActivityUnitTestCase<CreateExperimentActivity> {

    private CreateExperimentActivity mCreateExperimentActivity;

    public CreateExperimentActivityTests() {
        super(CreateExperimentActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        
        Context context = getInstrumentation().getTargetContext();
        context.setTheme(R.style.Theme_AppCompat);
        Intent intent = new Intent(context, CreateExperimentActivity.class);
        
        startActivity(intent, null, null);                
        mCreateExperimentActivity = getActivity();
        
        
        // createExperimentActivity = launchActivity(context.getPackageName(), CreateExperimentActivity.class, null);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testLayout() {
        assertNotNull("The activity cannot be null.", mCreateExperimentActivity);
        assertNotNull("Fragment container must exist.", mCreateExperimentActivity.findViewById(R.id.fragment_container));
        assertNotNull("ProgressBar Layout must exist.", mCreateExperimentActivity.findViewById(R.id.progressbar_task_in_progress));
    }

    public void testSensorDataCollectionState() {
        assertNotNull("The activity cannot be null.", mCreateExperimentActivity);
        assert(mCreateExperimentActivity.getCurrentCollectionState() == DataCollectionState.DATA_COLLECTION_STOPPED);
    }
}
