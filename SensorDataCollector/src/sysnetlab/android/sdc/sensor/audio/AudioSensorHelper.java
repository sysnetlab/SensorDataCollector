
package sysnetlab.android.sdc.sensor.audio;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import sysnetlab.android.sdc.R;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioSensorHelper {

    private static List<AudioRecordParameter> mListAudioRecordParameters = new ArrayList<AudioRecordParameter>();

    private static final int[] SAMPLING_RATES = {
            192000,
            96000,
            48000,
            44100,
            22050,
            16000,
            11025,
            8000
    };

    final static AudioChannelIn[] CHANNEL_IN_ARRAY = {
            new AudioChannelIn(AudioFormat.CHANNEL_IN_DEFAULT, R.string.text_channel_in_default),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_BACK, R.string.text_channel_in_back),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_BACK_PROCESSED,
                    R.string.text_channel_in_back_processed),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_FRONT, R.string.text_channel_in_front),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_FRONT_PROCESSED,
                    R.string.text_channel_in_front_processed),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_LEFT, R.string.text_channel_in_left),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_LEFT_PROCESSED,
                    R.string.text_channel_in_left_processed),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_MONO, R.string.text_channel_in_mono),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_PRESSURE, R.string.text_channel_in_pressure),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_RIGHT, R.string.text_channel_in_right),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_RIGHT_PROCESSED,
                    R.string.text_channel_in_right_processed),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_STEREO, R.string.text_channel_in_stereo),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_VOICE_DNLINK,
                    R.string.text_channel_in_voice_dnlink),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_VOICE_UPLINK,
                    R.string.text_channel_in_voice_uplink),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_X_AXIS, R.string.text_channel_in_x_axis),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_Y_AXIS, R.string.text_channel_in_y_axis),
            new AudioChannelIn(AudioFormat.CHANNEL_IN_Z_AXIS, R.string.text_channel_in_z_axis)
    };

    private static AudioEncoding[] AUDIO_ENCODING_ARRAY = {
            new AudioEncoding(AudioFormat.ENCODING_DEFAULT,
                    R.string.text_audio_encoding_default),
            new AudioEncoding(AudioFormat.ENCODING_PCM_16BIT,
                    R.string.text_audio_encoding_pcm_16bit),
            new AudioEncoding(AudioFormat.ENCODING_PCM_8BIT,
                    R.string.text_audio_encoding_pcm_8bit)
    };

    private static AudioSource[] AUDIO_SOURCE_ARRAY = {
        new AudioSource(MediaRecorder.AudioSource.DEFAULT,
                R.string.text_audio_source_default),
            new AudioSource(MediaRecorder.AudioSource.MIC,
                    R.string.text_audio_source_mic),
            new AudioSource(MediaRecorder.AudioSource.CAMCORDER,
                    R.string.text_audio_source_camcorder)
    };
    

    public static List<AudioRecordParameter> getValidRecordingParameters() {
        List<AudioRecordParameter> listParams = new ArrayList<AudioRecordParameter>();
        scanValidRecordingParametersFirstPass(listParams);
        scanValidRecordingParametersSecondPass(listParams, mListAudioRecordParameters);
        
        return mListAudioRecordParameters;
    }    
    
    private static void scanValidRecordingParametersFirstPass(List<AudioRecordParameter> listParams) {
        if (listParams == null) {
            return;
        }
        
        listParams.clear();
        
        for (int rate : SAMPLING_RATES) {
            for (AudioChannelIn channel : CHANNEL_IN_ARRAY) {
                for (AudioEncoding format : AUDIO_ENCODING_ARRAY) {
                    int bufferSize = AudioRecord.getMinBufferSize(rate, channel.getChannelId(),
                            format.getEncodingId());

                    if (bufferSize <= 0)
                        continue;

                    AudioRecord r = null;
                    for (AudioSource source : AUDIO_SOURCE_ARRAY) {
                        try {
                            Log.d("SensorDataCollector", "getValidRecordingParametersFirstPass(): " +
                                    "(source, rate, channl, encoding, buffersize) = " +
                                    "(" + source.getSourceId() + ", " +
                                    rate + ", " + channel.getChannelId() + ", " +
                                    format.getEncodingId() + ", " + bufferSize + ")");
                            r = new AudioRecord(source.getSourceId(), rate,
                                    channel.getChannelId(), format.getEncodingId(), bufferSize);
                            r.startRecording();
                            ByteBuffer buf = ByteBuffer.allocateDirect(bufferSize);
                            r.read(buf, bufferSize);
                            r.stop();
                            r.release();
                            r = null;
                            listParams.add(new AudioRecordParameter(rate, channel,
                                    format, source, bufferSize, bufferSize));
                            Log.d("SensorDataCollector",
                                    "getValidRecordingParametersFirstPass(): Success & added");
                        } catch (IllegalArgumentException e) {
                            Log.d("SensorDataCollector", "getValidRecordingParametersFirstPass(): " +
                                    "rate = " + rate + "; bufferSize = " + bufferSize +
                                    "<- " + e.toString());
                            if (r != null) {
                                r.release();
                                r = null;
                            }
                            continue;
                        } catch (IllegalStateException e) {
                            Log.d("SensorDataCollector", "getValidRecordingParametersFirstPass(): " +
                                    "rate = " + rate + "; bufferSize = " + bufferSize +
                                    "<- " + e.toString());
                            if (r != null) {
                                r.release();
                                r = null;
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    private static void scanValidRecordingParametersSecondPass(
            List<AudioRecordParameter> listParamsIn, List<AudioRecordParameter> listParamsOut) {
        if (listParamsOut == null) {
            return;
        }
        listParamsOut.clear();
            
        for (AudioRecordParameter param : listParamsIn) {

            AudioRecord r = null;
            try {
                r = new AudioRecord(param.getSource().getSourceId(),
                        param.getSamplingRate(),
                        param.getChannel().getChannelId(), param
                                .getEncoding().getEncodingId(),
                        param.getBufferSize());

                Log.d("SensorDataCollector", "getValidRecordingParametersSecondPass(): " +
                        "(source, rate, channl, encoding, buffersize) = " +
                        "(" + param.getSource().getSourceId() + ", " +
                        param.getSamplingRate() + ", " +
                        param.getChannel().getChannelId() + ", " +
                        param.getEncoding().getEncodingId() + ", " +
                        param.getBufferSize() + ")");
                r.startRecording();
                ByteBuffer buf = ByteBuffer.allocateDirect(param.getBufferSize());
                r.read(buf, param.getBufferSize());
                r.stop();
                r.release();
                r = null;
                listParamsOut.add(param);
            } catch (Exception e) {
                Log.d("SensorDataCollector", "getValidRecordingParametersSecondPass(): " +
                        "(source, rate, channl, encoding, buffersize) = " +
                        "(" + param.getSource().getSourceId() + ", " +
                        param.getSamplingRate() + ", " +
                        param.getChannel().getChannelId() + ", " +
                        param.getEncoding().getEncodingId() + ", " +
                        param.getBufferSize() + ") will be removed.");
                if (r != null) {
                    r.release();
                    r = null;
                }
            }
        }
        Log.d("SensorDataCollector", "getValidRecordingParametersSecondPass(): number of parameters = " + listParamsOut.size());
    }

}
