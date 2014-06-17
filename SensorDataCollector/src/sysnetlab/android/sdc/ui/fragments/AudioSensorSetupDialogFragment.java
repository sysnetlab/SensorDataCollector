
package sysnetlab.android.sdc.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.sensor.audio.AudioChannelIn;
import sysnetlab.android.sdc.sensor.audio.AudioEncoding;
import sysnetlab.android.sdc.sensor.audio.AudioRecordParameter;
import sysnetlab.android.sdc.sensor.audio.AudioRecordSettingDataSource;
import sysnetlab.android.sdc.sensor.audio.AudioSource;
import sysnetlab.android.sdc.ui.UserInterfaceUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.ListView;

public class AudioSensorSetupDialogFragment extends DialogFragment {
    public static final int SELECT_SOURCE = 1;
    public static final int SELECT_CHANNEL_IN = 2;
    public static final int SELECT_ENCODING = 3;
    public static final int SELECT_SAMPLING_RATE = 4;
    public static final int SELECT_MIN_BUFFER_SIZE = 5;

    // assume that list is short. use sequential search
    private List<AudioRecordParameter> mListAudioRecordParameters;
    private AudioRecordParameter mAudioRecordParameter;
    private Activity mActivity;
    private ListView mListView;

    public static AudioSensorSetupDialogFragment newInstance(Activity activity,
            List<AudioRecordParameter> allParams,
            AudioRecordParameter param, ListView listView, int operation) {
        AudioSensorSetupDialogFragment d = new AudioSensorSetupDialogFragment();

        d.mActivity = activity;
        d.mAudioRecordParameter = param;
        d.mListAudioRecordParameters = allParams;
        d.mListView = listView;

        // Supply operation as an argument.
        Bundle args = new Bundle();
        args.putInt("operation", operation);
        d.setArguments(args);

        return d;
    }

    public AudioSensorSetupDialogFragment() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int operation = getArguments().getInt("operation");

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        CharSequence[] cs;
        int checkedItem;
        
        AudioRecordSettingDataSource.initializeInstance(getActivity());
        AudioRecordSettingDataSource dbSource = AudioRecordSettingDataSource.getInstance();

        switch (operation) {
            case SELECT_SOURCE:

                final List<AudioSource> listSources = dbSource.getAllAudioSources();

                cs = new CharSequence[listSources.size()];
                checkedItem = 0;
                for (int i = 0; i < listSources.size(); i++) {
                    AudioSource source = listSources.get(i);
                    cs[i] = getActivity().getResources().getString(source.getSourceNameResId());
                    if (source.getSourceId() == mAudioRecordParameter.getSource().getSourceId()) {
                        checkedItem = i;
                    }
                }
                builder.setSingleChoiceItems(cs, checkedItem,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("SensorDataCollector", "AudioSensorSetupDialog::SOURCE_SLECT: which = " + which);
                                AudioSource source = listSources.get(which);
                                List<AudioRecordParameter> params = lookupAudioRecordParameter(source);
                                if (params == null || params.isEmpty()) {
                                    Log.i("SensorDataCollector", "AudioSensorSetupDialog::SOURCE_SELECT:params is empty");                                
                                    return;
                                }

                                AudioRecordParameter p = params.get(0);

                                Log.i("SensorDataCollector", "AudioSensorSetupDialog::SOURCE_SLECT:BEFORE:SourceId = " + mAudioRecordParameter.getSource().getSourceId());                                
                                mAudioRecordParameter.setSource(source);
                                Log.i("SensorDataCollector", "AudioSensorSetupDialog::SOURCE_SLECT:AFTER:SourceId = " + mAudioRecordParameter.getSource().getSourceId());                                
                                
                                mAudioRecordParameter.setChannel(p.getChannel());
                                mAudioRecordParameter.setEncoding(p.getEncoding());
                                mAudioRecordParameter.setSamplingRate(p.getSamplingRate());
                                mAudioRecordParameter.setMinBufferSize(p.getBufferSize());
                            }
                        });

                builder.setPositiveButton(getActivity().getResources().getString(R.string.text_ok),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserInterfaceUtil.updateAudioSensorPropertyListView(mActivity,
                                        mListView, mAudioRecordParameter);
                            }
                        });

                break;
            case SELECT_CHANNEL_IN:
                final List<AudioChannelIn> listChannels = dbSource.getAllAudioChannelIns(mAudioRecordParameter.getSource());

                cs = new CharSequence[listChannels.size()];
                checkedItem = 0;
                for (int i = 0; i < listChannels.size(); i++) {
                    AudioChannelIn channel = listChannels.get(i);
                    cs[i] = getActivity().getResources().getString(channel.getChannelNameResId());
                    if (channel.getChannelId() == mAudioRecordParameter.getChannel().getChannelId()) {
                        checkedItem = i;
                    }
                }

                builder.setSingleChoiceItems(cs, checkedItem,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AudioChannelIn c = listChannels.get(which);
                                AudioSource s = mAudioRecordParameter.getSource();
                                List<AudioRecordParameter> params = lookupAudioRecordParameter(s, c);
                                if (params == null || params.isEmpty()) {
                                    return;
                                }

                                AudioRecordParameter p = params.get(0);

                                mAudioRecordParameter.setSource(s);
                                mAudioRecordParameter.setChannel(c);
                                mAudioRecordParameter.setEncoding(p.getEncoding());
                                mAudioRecordParameter.setSamplingRate(p.getSamplingRate());
                                mAudioRecordParameter.setMinBufferSize(p.getBufferSize());
                            }
                        });

                builder.setPositiveButton(getActivity().getResources().getString(R.string.text_ok),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserInterfaceUtil.updateAudioSensorPropertyListView(mActivity,
                                        mListView, mAudioRecordParameter);
                            }
                        });

                break;
            case SELECT_ENCODING:
                final List<AudioEncoding> listEncodings = dbSource.getAllAudioEncodings(mAudioRecordParameter.getSource(),
                                mAudioRecordParameter.getChannel());

                cs = new CharSequence[listEncodings.size()];
                checkedItem = 0;
                for (int i = 0; i < listEncodings.size(); i++) {
                    AudioEncoding encoding = listEncodings.get(i);
                    cs[i] = getActivity().getResources().getString(encoding.getEncodingNameResId());
                    if (encoding.getEncodingId() == mAudioRecordParameter.getEncoding()
                            .getEncodingId()) {
                        checkedItem = i;
                    }
                }

                builder.setSingleChoiceItems(cs, checkedItem,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AudioEncoding e = listEncodings.get(which);
                                AudioSource s = mAudioRecordParameter.getSource();
                                AudioChannelIn c = mAudioRecordParameter.getChannel();
                                List<AudioRecordParameter> params = lookupAudioRecordParameter(s, c);
                                if (params == null || params.isEmpty()) {
                                    return;
                                }

                                AudioRecordParameter p = params.get(0);

                                mAudioRecordParameter.setSource(s);
                                mAudioRecordParameter.setChannel(c);
                                mAudioRecordParameter.setEncoding(e);
                                mAudioRecordParameter.setSamplingRate(p.getSamplingRate());
                                mAudioRecordParameter.setMinBufferSize(p.getBufferSize());
                            }
                        });

                builder.setPositiveButton(getActivity().getResources().getString(R.string.text_ok),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserInterfaceUtil.updateAudioSensorPropertyListView(mActivity,
                                        mListView, mAudioRecordParameter);
                            }
                        });

                break;
            case SELECT_SAMPLING_RATE:
                final List<Integer> listSamplingRates = dbSource.getAllSamplingRates(mAudioRecordParameter.getSource(),
                                mAudioRecordParameter.getChannel(),
                                mAudioRecordParameter.getEncoding());

                cs = new CharSequence[listSamplingRates.size()];
                checkedItem = 0;
                for (int i = 0; i < listSamplingRates.size(); i++) {
                    int samplingRate = listSamplingRates.get(i);
                    cs[i] = Integer.toString(samplingRate);
                    if (samplingRate == mAudioRecordParameter.getSamplingRate()) {
                        checkedItem = i;
                    }
                }

                builder.setSingleChoiceItems(cs, checkedItem,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int samplingRate = listSamplingRates.get(which);
                                AudioSource s = mAudioRecordParameter.getSource();
                                AudioChannelIn c = mAudioRecordParameter.getChannel();
                                AudioEncoding e = mAudioRecordParameter.getEncoding();
                                List<AudioRecordParameter> params = lookupAudioRecordParameter(s,
                                        c, e);
                                if (params == null || params.isEmpty()) {
                                    return;
                                }

                                AudioRecordParameter p = params.get(0);

                                mAudioRecordParameter.setSource(s);
                                mAudioRecordParameter.setChannel(c);
                                mAudioRecordParameter.setEncoding(e);
                                mAudioRecordParameter.setSamplingRate(samplingRate);
                                mAudioRecordParameter.setMinBufferSize(p.getBufferSize());
                            }
                        });

                builder.setPositiveButton(getActivity().getResources().getString(R.string.text_ok),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserInterfaceUtil.updateAudioSensorPropertyListView(mActivity,
                                        mListView, mAudioRecordParameter);
                            }
                        });

                break;

            case SELECT_MIN_BUFFER_SIZE:
                
                // TODO change it to slider/seek bar instead of list
                final int minBufferSize = dbSource.getMinBufferSize(
                        mAudioRecordParameter.getSource(), mAudioRecordParameter.getChannel(),
                        mAudioRecordParameter.getEncoding(),
                        mAudioRecordParameter.getSamplingRate());

                int maxMultiplier = 20;
                cs = new CharSequence[maxMultiplier];
                checkedItem = 0;
                for (int i = 0; i < maxMultiplier; i++) {
                    int bufferSize = (i + 1) * minBufferSize;
                    cs[i] = Integer.toString(bufferSize);
                    if (mAudioRecordParameter.getBufferSize() == bufferSize) {
                        checkedItem = i;
                    }
                }

                builder.setSingleChoiceItems(cs, checkedItem,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int bufferSize = minBufferSize * (which + 1);
                                AudioSource s = mAudioRecordParameter.getSource();
                                AudioChannelIn c = mAudioRecordParameter.getChannel();
                                AudioEncoding e = mAudioRecordParameter.getEncoding();
                                int r = mAudioRecordParameter.getSamplingRate();

                                mAudioRecordParameter.setSource(s);
                                mAudioRecordParameter.setChannel(c);
                                mAudioRecordParameter.setEncoding(e);
                                mAudioRecordParameter.setSamplingRate(r);
                                mAudioRecordParameter.setMinBufferSize(bufferSize);
                            }
                        });

                builder.setPositiveButton(getActivity().getResources().getString(R.string.text_ok),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserInterfaceUtil.updateAudioSensorPropertyListView(mActivity,
                                        mListView, mAudioRecordParameter);
                            }
                        });

                break;
        }
        dbSource.close();
        Dialog dialog = builder.create();

        return dialog;
    }

    private List<AudioRecordParameter> lookupAudioRecordParameter(AudioSource source) {
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

    private List<AudioRecordParameter> lookupAudioRecordParameter(AudioSource source,
            AudioChannelIn channel) {
        if (mListAudioRecordParameters == null) {
            return null;
        }

        List<AudioRecordParameter> params = new ArrayList<AudioRecordParameter>();
        for (AudioRecordParameter p : mListAudioRecordParameters) {
            if (p.getSource().getSourceId() == source.getSourceId()
                    && p.getChannel().getChannelId() == channel.getChannelId()) {
                params.add(p);
            }
        }
        return params;
    }

    private List<AudioRecordParameter> lookupAudioRecordParameter(AudioSource source,
            AudioChannelIn channel, AudioEncoding encoding) {
        if (mListAudioRecordParameters == null) {
            return null;
        }

        List<AudioRecordParameter> params = new ArrayList<AudioRecordParameter>();
        for (AudioRecordParameter p : mListAudioRecordParameters) {
            if (p.getSource().getSourceId() == source.getSourceId()
                    && p.getChannel().getChannelId() == channel.getChannelId()
                    && p.getEncoding().getEncodingId() == encoding.getEncodingId()) {
                params.add(p);
            }
        }
        return params;
    }
}
