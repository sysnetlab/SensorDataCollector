package sysnetlab.android.sdc.sensor.audio;

import java.util.ArrayList;
import java.util.List;

import sysnetlab.android.sdc.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class AudioSensorSetupDiaglogFragment extends DialogFragment {
    public static final int SELECT_SOURCE = 1;
    public static final int SELECT_CHANNEL_IN = 2;
    public static final int SELECT_ENCODING = 3;
    public static final int SELECT_SAMPLING_RATE = 4;
    public static final int SELECT_MIN_BUFFER_SIZE = 5;
    
    // assume that list is short. use sequential search
    private List<AudioRecordParameter> mListAudioRecordParameters;
    private AudioRecordParameter mAudioRecordParameter;
    private AudioRecordSettingDataSource mAudioRecordSettingDataSource;
    
    public static AudioSensorSetupDiaglogFragment newInstance(AudioRecordSettingDataSource dbSource, List<AudioRecordParameter> allParams, AudioRecordParameter param, int operation) {
        AudioSensorSetupDiaglogFragment d = new AudioSensorSetupDiaglogFragment();

        d.mAudioRecordParameter = param;
        d.mAudioRecordSettingDataSource = dbSource;
        d.mListAudioRecordParameters = allParams;
        
        // Supply operation as an argument.
        Bundle args = new Bundle();
        args.putInt("operation", operation);
        d.setArguments(args);
        
        return d;
    }
    
    public AudioSensorSetupDiaglogFragment() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int operation = getArguments().getInt("operation");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        switch (operation) {
            case SELECT_SOURCE:
                final List<AudioSource> listSources = mAudioRecordSettingDataSource.getAllAudioSources();
                
                Log.i("SensorDataCollector", "AudioSensorSetupDialogFragment::onCreateDialog() list size = " + listSources.size());
                CharSequence[] sourceSequences = new CharSequence[listSources.size()];
                int checkedItem = 0;
                for (int i = 0; i < listSources.size(); i ++) {
                    AudioSource source = listSources.get(i);
                    sourceSequences[i] = getActivity().getResources().getString(source.getSourceNameResId());
                    if (source.getSourceId() == mAudioRecordParameter.getSource().getSourceId()) {
                        checkedItem = i;
                    }
                    i ++;
                }
                builder.setSingleChoiceItems(sourceSequences, checkedItem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AudioSource source = listSources.get(which);
                        List<AudioRecordParameter> params = lookupAudioRecordParameterFromSource(source);
                        if (params == null || params.isEmpty()) {
                            return;
                        }
                        
                        AudioRecordParameter p = params.get(0);
                        
                        mAudioRecordParameter.setSource(source);
                        mAudioRecordParameter.setChannel(p.getChannel());
                        mAudioRecordParameter.setEncoding(p.getEncoding());
                        mAudioRecordParameter.setSamplingRate(p.getSamplingRate());
                        mAudioRecordParameter.setMinBufferSize(p.getMinBufferSize());
                    }});
                Log.i("SensorDataCollector", "AudioSensorSetupDialogFragment::onCreateDialog() check point #2");
                
                builder.setPositiveButton(getActivity().getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }});

                Log.i("SensorDataCollector", "AudioSensorSetupDialogFragment::onCreateDialog() check point #3");                
                break;
            case SELECT_CHANNEL_IN:
                
                
                
                break;
            case SELECT_ENCODING:
                break;
            case SELECT_SAMPLING_RATE:
                break;
            case SELECT_MIN_BUFFER_SIZE:
                break;
        }
        
        return builder.create();
    }    
    
    private List<AudioRecordParameter> lookupAudioRecordParameterFromSource(AudioSource source) {
        if (mListAudioRecordParameters == null) {
            return null;
        }
        
        List<AudioRecordParameter> params = new ArrayList<AudioRecordParameter>();
        for (AudioRecordParameter p : mListAudioRecordParameters) {
            if (p.getSource().getSourceId() == source.getSourceId()) {
                params.add(p);
            }
        }
        return params;
    }
}
