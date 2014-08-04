/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package sysnetlab.android.sdc.sensor.audio;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.ui.AudioSensorProbingActivity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseIntArray;

public class AudioSensorHelper {

    private static List<AudioRecordParameter> mListAudioRecordParameters = new ArrayList<AudioRecordParameter>();

    private static final int[] SAMPLE_RATES = {
            192000,
            96000,
            48000,
            44100,
            22050,
            16000,
            11025,
            8000
    };

    // TODO: these arrays should be completely replaced by the SpartIntArrays

    private final static AudioChannelIn[] CHANNEL_IN_ARRAY = {
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

    private static final SparseIntArray AUDIO_CHANNEL_IN_MAP;
    static
    {
        AUDIO_CHANNEL_IN_MAP = new SparseIntArray();
        for (int i = 0; i < CHANNEL_IN_ARRAY.length; i++) {
            AUDIO_CHANNEL_IN_MAP.put(CHANNEL_IN_ARRAY[i].getChannelId(),
                    CHANNEL_IN_ARRAY[i].getChannelNameResId());
        }
    }

    private static AudioEncoding[] AUDIO_ENCODING_ARRAY = {
            new AudioEncoding(AudioFormat.ENCODING_DEFAULT,
                    R.string.text_audio_encoding_default),
            new AudioEncoding(AudioFormat.ENCODING_PCM_16BIT,
                    R.string.text_audio_encoding_pcm_16bit),
            new AudioEncoding(AudioFormat.ENCODING_PCM_8BIT,
                    R.string.text_audio_encoding_pcm_8bit)
    };

    private static final SparseIntArray AUDIO_ENCODING_MAP;
    static
    {
        AUDIO_ENCODING_MAP = new SparseIntArray();
        for (int i = 0; i < AUDIO_ENCODING_ARRAY.length; i++) {
            AUDIO_ENCODING_MAP.put(AUDIO_ENCODING_ARRAY[i].getEncodingId(),
                    AUDIO_ENCODING_ARRAY[i].getEncodingNameResId());
        }
    }

    private static AudioSource[] AUDIO_SOURCE_ARRAY = {
            new AudioSource(MediaRecorder.AudioSource.DEFAULT,
                    R.string.text_audio_source_default),
            new AudioSource(MediaRecorder.AudioSource.MIC,
                    R.string.text_audio_source_mic),
            new AudioSource(MediaRecorder.AudioSource.CAMCORDER,
                    R.string.text_audio_source_camcorder)
    };

    private static final SparseIntArray AUDIO_SOURCE_MAP;
    static
    {
        AUDIO_SOURCE_MAP = new SparseIntArray();
        for (int i = 0; i < AUDIO_SOURCE_ARRAY.length; i++) {
            AUDIO_SOURCE_MAP.put(AUDIO_SOURCE_ARRAY[i].getSourceId(),
                    AUDIO_SOURCE_ARRAY[i].getSourceNameResId());
        }
    }

    public static int getChannelInNameResId(int channelInId) {
        return AUDIO_CHANNEL_IN_MAP.get(channelInId);
    }

    public static int getEncodingNameResId(int encodingId) {
        return AUDIO_ENCODING_MAP.get(encodingId);
    }

    public static int getSourceNameResId(int sourceId) {
        return AUDIO_SOURCE_MAP.get(sourceId);
    }

    public static int estimateWorkLoadOnSensorProbing() {
        return SAMPLE_RATES.length * CHANNEL_IN_ARRAY.length * AUDIO_ENCODING_ARRAY.length
                * AUDIO_SOURCE_ARRAY.length * 2;
    }

    public static List<AudioRecordParameter> getValidRecordingParameters() {
        return getValidRecordingParameters(null);
    }

    public static List<AudioRecordParameter> getValidRecordingParameters(
            AsyncTask<Void, Integer, Void> asyncTask) {
        List<AudioRecordParameter> listParams = new ArrayList<AudioRecordParameter>();
        scanValidRecordingParametersFirstPass(listParams, asyncTask);
        scanValidRecordingParametersSecondPass(listParams, mListAudioRecordParameters, asyncTask);

        return mListAudioRecordParameters;
    }

    private static void scanValidRecordingParametersFirstPass(
            List<AudioRecordParameter> listParams, AsyncTask<Void, Integer, Void> asyncTask) {

        // Log.d("SensorDataCollector", "asyncTask = " + asyncTask == null?
        // "null" : "not null");
        if (listParams == null) {
            return;
        }

        listParams.clear();

        int progressValue = 0;
        for (int rate : SAMPLE_RATES) {
            for (AudioChannelIn channel : CHANNEL_IN_ARRAY) {
                for (AudioEncoding format : AUDIO_ENCODING_ARRAY) {
                    int bufferSize = AudioRecord.getMinBufferSize(rate, channel.getChannelId(),
                            format.getEncodingId());

                    if (bufferSize <= 0) {
                        if (asyncTask != null
                                && asyncTask instanceof AudioSensorProbingActivity.AudioSensorProbingTask) {
                            progressValue += AUDIO_SOURCE_ARRAY.length;
                            // Log.d("SensorDataCollector",
                            // "to call doPublishProgress...");
                            ((AudioSensorProbingActivity.AudioSensorProbingTask) asyncTask)
                                    .doPublishProgress(progressValue);
                        }

                        Log.d("SensorDataCollector",
                                "getValidRecordingParametersFirstPass(): proving audio sensor: " +
                                        "(rate, channl, encoding, buffersize) = " +
                                        "(" + rate + ", " + channel.getChannelId() + ", " +
                                        format.getEncodingId() + ", " + bufferSize + "): failure.");

                        continue;
                    }

                    AudioRecord r = null;
                    for (AudioSource source : AUDIO_SOURCE_ARRAY) {
                        if (asyncTask != null
                                && asyncTask instanceof AudioSensorProbingActivity.AudioSensorProbingTask) {
                            progressValue++;
                            ((AudioSensorProbingActivity.AudioSensorProbingTask) asyncTask)
                                    .doPublishProgress(progressValue);
                        }
                        try {
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
                                    "getValidRecordingParametersFirstPass(): probing audio sensor "
                                            +
                                            "(source, rate, channl, encoding, buffersize) = " +
                                            "(" + source.getSourceId() + ", " +
                                            rate + ", " + channel.getChannelId() + ", " +
                                            format.getEncodingId() + ", " + bufferSize
                                            + "): success.");
                        } catch (IllegalArgumentException e) {
                            Log.d("SensorDataCollector",
                                    "getValidRecordingParametersFirstPass(): probing audio sensor "
                                            +
                                            "(source, rate, channl, encoding, buffersize) = " +
                                            "(" + source.getSourceId() + ", " +
                                            rate + ", " + channel.getChannelId() + ", " +
                                            format.getEncodingId() + ", " + bufferSize
                                            + "): failure with reason: " + e.toString());
                            if (r != null) {
                                r.release();
                                r = null;
                            }
                            continue;
                        } catch (IllegalStateException e) {
                            Log.d("SensorDataCollector",
                                    "getValidRecordingParametersFirstPass(): probing audio sensor "
                                            +
                                            "(source, rate, channl, encoding, buffersize) = " +
                                            "(" + source.getSourceId() + ", " +
                                            rate + ", " + channel.getChannelId() + ", " +
                                            format.getEncodingId() + ", " + bufferSize
                                            + "): failure with reason: " + e.toString());
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
            List<AudioRecordParameter> listParamsIn, List<AudioRecordParameter> listParamsOut,
            AsyncTask<Void, Integer, Void> asyncTask) {
        if (listParamsOut == null) {
            return;
        }
        listParamsOut.clear();

        int progressStep = 0;
        int progressValue = 0;
        if (asyncTask != null
                && asyncTask instanceof AudioSensorProbingActivity.AudioSensorProbingTask) {
            progressValue = estimateWorkLoadOnSensorProbing() / 2;
            progressStep = progressValue / listParamsIn.size();
        }

        for (AudioRecordParameter param : listParamsIn) {

            AudioRecord r = null;
            try {
                r = new AudioRecord(param.getSource().getSourceId(),
                        param.getSamplingRate(),
                        param.getChannel().getChannelId(),
                        param.getEncoding().getEncodingId(),
                        param.getBufferSize());

                r.startRecording();
                ByteBuffer buf = ByteBuffer.allocateDirect(param.getBufferSize());
                r.read(buf, param.getBufferSize());
                r.stop();
                r.release();
                r = null;
                listParamsOut.add(param);

                Log.d("SensorDataCollector",
                        "getValidRecordingParametersSecondPass(): probing audio sensor " +
                                "(source, rate, channl, encoding, buffersize) = " +
                                "(" + param.getSource().getSourceId() + ", " +
                                param.getSamplingRate() + ", " +
                                param.getChannel().getChannelId() + ", " +
                                param.getEncoding().getEncodingId() + ", " +
                                param.getBufferSize() + "): success.");
            } catch (Exception e) {
                Log.d("SensorDataCollector",
                        "getValidRecordingParametersSecondPass(): probing audio sensor " +
                                "(source, rate, channl, encoding, buffersize) = " +
                                "(" + param.getSource().getSourceId() + ", " +
                                param.getSamplingRate() + ", " +
                                param.getChannel().getChannelId() + ", " +
                                param.getEncoding().getEncodingId() + ", " +
                                param.getBufferSize() + "): failure with reason: " + e.toString());
                if (r != null) {
                    r.release();
                    r = null;
                }
            }

            if (asyncTask != null
                    && asyncTask instanceof AudioSensorProbingActivity.AudioSensorProbingTask) {
                progressValue += progressStep;
                ((AudioSensorProbingActivity.AudioSensorProbingTask) asyncTask)
                        .doPublishProgress(progressValue);

            }
        }
        Log.d("SensorDataCollector",
                "getValidRecordingParametersSecondPass(): number of parameters = "
                        + listParamsOut.size());
    }

}
