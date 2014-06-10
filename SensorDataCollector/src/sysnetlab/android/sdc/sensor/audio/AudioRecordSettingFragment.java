package sysnetlab.android.sdc.sensor.audio;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;

public class AudioRecordSettingFragment extends Fragment {

    List<AudioRecordParameter> mParams;
    
    @Override
    public void onResume() {
        super.onResume();
        
        AudioRecordSettingDataSource ds = new AudioRecordSettingDataSource((Context)getActivity());
        
        ds.open();
        
        if (!ds.isDataSourceReady()) {
            ds.prepareDataSource();
        } 
        
        mParams = ds.getAllAudioRecordParameters();
               
        ds.close();
    }

}
