package sysnetlab.android.sdc.sensor.audio;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.fragments.FragmentUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;



/**
 * It is to detect what possible audio recording parameters that the
 * device supports. The app stores the parameter in a SQLite database
 * for later retrieval during recording. 
 */
public class AudioSensorHelperActivity extends FragmentActivity {
    private AudioRecordSettingFragment mAudioRecordSettingFragment; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        
        // display allowed parameter settings and allow rescan
        mAudioRecordSettingFragment = new AudioRecordSettingFragment();
        FragmentUtil.addFragment(this, mAudioRecordSettingFragment, "audiorecordsetting");
    } 
}
