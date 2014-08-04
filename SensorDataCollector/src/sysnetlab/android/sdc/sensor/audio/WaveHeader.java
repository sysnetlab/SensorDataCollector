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

import android.util.Log;

public class WaveHeader {

    private byte[] mHeader = {
            'R', 'I', 'F', 'F', // ChunkId = "RIFF"
            0, 0, 0, 0,         // ChunkSize = 36 + SubChunk2Size
            // ChunkSize counting from below to the end of file
            'W', 'A', 'V', 'E', // format = "WAVE"

            // format subchunk or subchunk1
            'f', 'm', 't', ' ', // SubChuck1Id = "fmt "
            16, 0, 0, 0,    // SubChunk1Size 16 for PCM
            // SubChunk1
            1, 0,           // AudioFormat 1 for PCM
            0, 0,           // NumChannels 1 for Mono 2 for Stereo
            0, 0, 0, 0,     // SamplingRate
            0, 0, 0, 0,     // ByteRate == SampleRate * NumChannels * BitsPerSample/8
            0, 0,           // BlockAlign == NumChannels * BitsPerSample/8
            0, 0,           // BitsPerSample 8 bits = 8, 16 bits = 16

            // data subchunk or SubChunk2
            'd', 'a', 't', 'a', // SubChunk2ID == "data"
            0, 0, 0, 0      // SubChunk2Size == NumSamples * NumChannels * BitsPerSample/8
    };

    private long mChunkSize;

    private short mNumberOfChannels;

    private long mSamplingRate;

    private long mByteRate;

    private long mSubChunk2Size;

    private short mBitsPerSample;
    
    private short mBlockAlign;

    private boolean mHeaderFilled = false;

    public WaveHeader(short numberOfChannels, long samplingRate, short bitsPerSample, long numberOfSamples) {
        mNumberOfChannels = numberOfChannels;
        mSamplingRate = samplingRate;
        mBitsPerSample = bitsPerSample;
        mByteRate = samplingRate * numberOfChannels * bitsPerSample / 8;
        fillWaveHeader(numberOfSamples);
    }

    private void fillWaveHeader(long numberOfSamples) {
        mBlockAlign = (short) (mNumberOfChannels * mBitsPerSample / 8);
        mSubChunk2Size = numberOfSamples * mBlockAlign;
        mChunkSize = mSubChunk2Size + 36;

        Log.d("SensorDataCollector", "mChunkSize = " + mChunkSize);

        mHeader[4] = (byte) (mChunkSize & 0xff);
        mHeader[5] = (byte) ((mChunkSize >> 8) & 0xff);
        mHeader[6] = (byte) ((mChunkSize >> 16) & 0xff);
        mHeader[7] = (byte) ((mChunkSize >> 24) & 0xff);

        mHeader[22] = (byte) (mNumberOfChannels & 0xff);
        mHeader[23] = (byte) (mNumberOfChannels >> 8 & 0xff);

        mHeader[24] = (byte) (mSamplingRate & 0xff);
        mHeader[25] = (byte) ((mSamplingRate >> 8) & 0xff);
        mHeader[26] = (byte) ((mSamplingRate >> 16) & 0xff);
        mHeader[27] = (byte) ((mSamplingRate >> 24) & 0xff);
        mHeader[28] = (byte) (mByteRate & 0xff);
        mHeader[29] = (byte) ((mByteRate >> 8) & 0xff);
        mHeader[30] = (byte) ((mByteRate >> 16) & 0xff);
        mHeader[31] = (byte) ((mByteRate >> 24) & 0xff);
        
        mHeader[32] = (byte) (mBlockAlign & 0xff);
        mHeader[31] = (byte) (mBlockAlign >> 8 & 0xff);

        mHeader[34] = (byte) (mBitsPerSample & 0xff); // bits per sample
        mHeader[35] = (byte) (mBitsPerSample >> 8 & 0xff);

        mHeader[40] = (byte) (mSubChunk2Size & 0xff);
        mHeader[41] = (byte) ((mSubChunk2Size >> 8) & 0xff);
        mHeader[42] = (byte) ((mSubChunk2Size >> 16) & 0xff);
        mHeader[43] = (byte) ((mSubChunk2Size >> 24) & 0xff);

        mHeaderFilled = true;
    }
    
    public byte[] getHeader() {
        if (mHeaderFilled) {
            return mHeader;
        } else {
            return null;
        }
    }

    public int getLength() {
        return mHeader.length;
    }
}
