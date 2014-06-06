
package sysnetlab.android.sdc.sensor.audio;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AudioRecordSettingDataSource {

    private SQLiteDatabase database;
    private AudioRecordSettingDBHelper dbHelper;
    private String[] allColumns = {
            AudioRecordSettingDBHelper.COLUMN_NAME_ID,
            AudioRecordSettingDBHelper.COLUMN_NAME_SAMPLING_RATE,
            AudioRecordSettingDBHelper.COLUMN_NAME_CHANNEL_IN_ID,
            AudioRecordSettingDBHelper.COLUMN_NAME_CHANNEL_IN_RES_ID,
            AudioRecordSettingDBHelper.COLUMN_NAME_CHANNEL_ENCODING_ID,
            AudioRecordSettingDBHelper.COLUMN_NAME_CHANNEL_ENCODING_RES_ID,
            AudioRecordSettingDBHelper.COLUMN_NAME_AUDIO_SOURCE_ID,
            AudioRecordSettingDBHelper.COLUMN_NAME_AUDIO_SOURCE_RES_ID,
            AudioRecordSettingDBHelper.COLUMN_NAME_MIN_BUFFER_SIZE
    };
    private String[] statusColumns = {
            AudioRecordSettingDBHelper.COLUMN_NAME_STATUS      
    };

    public AudioRecordSettingDataSource(Context context) {
        dbHelper = new AudioRecordSettingDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private void insertAudioRecordParameter(AudioRecordParameter audioRecordParam) {
        ContentValues values = new ContentValues();

        values.put(AudioRecordSettingDBHelper.COLUMN_NAME_SAMPLING_RATE,
                audioRecordParam.getSamplingRate());
        values.put(AudioRecordSettingDBHelper.COLUMN_NAME_CHANNEL_IN_ID, audioRecordParam
                .getChannel().getChannelId());
        values.put(AudioRecordSettingDBHelper.COLUMN_NAME_CHANNEL_IN_RES_ID, audioRecordParam
                .getChannel().getChannelNameResId());
        values.put(AudioRecordSettingDBHelper.COLUMN_NAME_CHANNEL_ENCODING_ID, audioRecordParam
                .getEncoding().getEncodingId());
        values.put(AudioRecordSettingDBHelper.COLUMN_NAME_CHANNEL_ENCODING_RES_ID, audioRecordParam
                .getEncoding().getEncodingNameResId());
        values.put(AudioRecordSettingDBHelper.COLUMN_NAME_AUDIO_SOURCE_ID, audioRecordParam
                .getSource().getSourceId());
        values.put(AudioRecordSettingDBHelper.COLUMN_NAME_AUDIO_SOURCE_RES_ID, audioRecordParam
                .getSource().getSourceNameResId());
        values.put(AudioRecordSettingDBHelper.COLUMN_NAME_MIN_BUFFER_SIZE,
                audioRecordParam.getMinBufferSize());

        database.insert(AudioRecordSettingDBHelper.TABLE_AUDIORECORDSETTINGS, null, values);
    }
    
    public void addAllAudioRecordParameters(List<AudioRecordParameter> params) {
        for (AudioRecordParameter p : params) {
            insertAudioRecordParameter(p);
        }
    }

    public List<AudioRecordParameter> getAllAudioRecordParameters() {
        List<AudioRecordParameter> params = new ArrayList<AudioRecordParameter>();

        Cursor cursor = database.query(AudioRecordSettingDBHelper.TABLE_AUDIORECORDSETTINGS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AudioRecordParameter p = cursorToAudioRecordParameter(cursor);
            params.add(p);
            cursor.moveToNext();
        }

        cursor.close();
        return params;
    }
    
    public boolean isDataSourceReady() {

        Cursor cursor = database.query(AudioRecordSettingDBHelper.TABLE_AUDIORECORDDISCOVERSTATUS,
                statusColumns, null, null, null, null, null);

        cursor.moveToFirst();
        
        int status = cursor.getInt(0);

        cursor.close();
        
        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    private AudioRecordParameter cursorToAudioRecordParameter(Cursor cursor) {
        AudioRecordParameter param = new AudioRecordParameter();

        param.setSamplingRate(cursor.getInt(1));
        param.setChannel(new AudioChannelIn(cursor.getInt(2), cursor.getInt(3)));
        param.setEncoding(new AudioEncoding(cursor.getInt(4), cursor.getInt(5)));
        param.setSource(new AudioSource(cursor.getInt(6), cursor.getInt(7)));
        param.setMinBufferSize(cursor.getInt(8));

        return param;
    }
}
