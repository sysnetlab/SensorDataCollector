package sysnetlab.android.sdc.sensor.audio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppSettingDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "SensorDataCollector.Settings";
    private static final String DB_TABLE_AUDIORECORDSETTINGS_CREATE = "CREATE TABLE AudioRecordSettings (" +
            "id integer primary key autoincrement," +
            "sampling_rate integer," +
            "channel_in integer," +
            "channel_in_text varchar(60)," +
            "channel_encoding integer," +
            "channel_encoding_text varchar(60)," +
            "audio_source integer," +
            "audio_source_text varchar(60)," +
            "min_buffer_size)";
    
    public AppSettingDBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_TABLE_AUDIORECORDSETTINGS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SensorDataCollector::" + AppSettingDBHelper.class.getName(), "Upgrading database "
                + DB_NAME + " from version " + oldVersion + " to " + newVersion
                + ", which destroys all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_AUDIORECORDSETTINGS_CREATE);
        onCreate(db);
    }

}
