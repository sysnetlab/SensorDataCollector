
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

    /*
    public WaveHeader() {
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (mChunkSize & 0xff);
        header[5] = (byte) ((mChunkSize >> 8) & 0xff);
        header[6] = (byte) ((mChunkSize >> 16) & 0xff);
        header[7] = (byte) ((mChunkSize >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) (mNumberOfChannels & 0xff);
        header[23] = (byte) (mNumberOfChannels >> 8 & 0xff);
        header[24] = (byte) (mSamplingRate & 0xff);
        header[25] = (byte) ((mSamplingRate >> 8) & 0xff);
        header[26] = (byte) ((mSamplingRate >> 16) & 0xff);
        header[27] = (byte) ((mSamplingRate >> 24) & 0xff);
        header[28] = (byte) (mByteRate & 0xff);
        header[29] = (byte) ((mByteRate >> 8) & 0xff);
        header[30] = (byte) ((mByteRate >> 16) & 0xff);
        header[31] = (byte) ((mByteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = (byte) (mBitsPerSample & 0xff); // bits per sample
        header[35] = (byte) (mBitsPerSample >> 8 & 0xff);
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (mSubChunk2Size & 0xff);
        header[41] = (byte) ((mSubChunk2Size >> 8) & 0xff);
        header[42] = (byte) ((mSubChunk2Size >> 16) & 0xff);
        header[43] = (byte) ((mSubChunk2Size >> 24) & 0xff);
    }

    */
    
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
