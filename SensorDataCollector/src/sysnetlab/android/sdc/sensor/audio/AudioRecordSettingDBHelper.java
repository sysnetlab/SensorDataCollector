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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AudioRecordSettingDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_AUDIORECORDSETTINGS = "AudioRecordSettings";
    public static final String TABLE_AUDIORECORDDISCOVERSTATUS = "AudioRecordDiscoverStatus";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_SAMPLING_RATE = "sampling_rate";
    public static final String COLUMN_NAME_CHANNEL_IN_ID = "channel_in_id";
    //public static final String COLUMN_NAME_CHANNEL_IN_RES_ID = "channel_in_res_id";
    public static final String COLUMN_NAME_CHANNEL_ENCODING_ID = "channel_encoding_id";
    //public static final String COLUMN_NAME_CHANNEL_ENCODING_RES_ID = "channel_encoding_res_id";
    public static final String COLUMN_NAME_AUDIO_SOURCE_ID = "audio_source_id";
    //public static final String COLUMN_NAME_AUDIO_SOURCE_RES_ID = "audio_source_res_id";
    public static final String COLUMN_NAME_MIN_BUFFER_SIZE = "min_buffer_size";
    public static final String COLUMN_NAME_STATUS = "status";

    private static final int DATABASE_VERSION = 2;
    private static final String DB_NAME = "SensorDataCollector.Settings";
    private static final String DB_TABLE_AUDIORECORDSETTINGS_CREATE = "CREATE TABLE "
            + TABLE_AUDIORECORDSETTINGS + " (" +
            COLUMN_NAME_ID + " integer primary key autoincrement," +
            COLUMN_NAME_SAMPLING_RATE + " integer not null," +
            COLUMN_NAME_CHANNEL_IN_ID + " integer not null," +
            //COLUMN_NAME_CHANNEL_IN_RES_ID + " integer not null," +
            COLUMN_NAME_CHANNEL_ENCODING_ID + " integer not null," +
            //COLUMN_NAME_CHANNEL_ENCODING_RES_ID + " integer not null," +
            COLUMN_NAME_AUDIO_SOURCE_ID + " integer not null," +
            //COLUMN_NAME_AUDIO_SOURCE_RES_ID + " integer not null," +
            COLUMN_NAME_MIN_BUFFER_SIZE + " integer not null)";
    private static final String DB_TABLE_AUDIORECORDDISCOVERSTATUS_CREATE = "CREATE TABLE "
            + TABLE_AUDIORECORDDISCOVERSTATUS + " ("
            + COLUMN_NAME_STATUS + " integer primary key)";

    public AudioRecordSettingDBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_TABLE_AUDIORECORDSETTINGS_CREATE);
        db.execSQL(DB_TABLE_AUDIORECORDDISCOVERSTATUS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SensorDataCollector", AudioRecordSettingDBHelper.class.getName() +
                ":" + "Upgrading database "
                        + DB_NAME + " from version " + oldVersion + " to " + newVersion
                        + ", which destroys all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIORECORDSETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIORECORDDISCOVERSTATUS);
        onCreate(db);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SensorDataCollector", AudioRecordSettingDBHelper.class.getName() +
                ":" + "Downgrade database "
                        + DB_NAME + " from version " + newVersion + " to " + oldVersion
                        + ", which destroys all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIORECORDSETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIORECORDDISCOVERSTATUS);
        onCreate(db);
    }    
}
