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

package sysnetlab.android.sdc.datacollector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private String mNote;
    private Date mDateCreated;

    public Note(String note) {
        mNote = note != null ? note : "";
        mDateCreated = Calendar.getInstance().getTime();
    }

    public Note(String note, Date dateCreated) {
        mNote = note != null ? note : "";
        mDateCreated = dateCreated != null ? dateCreated : Calendar.getInstance().getTime();
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String mNote) {
        this.mNote = mNote;
    }

    public Date getDateCreated() {
        return mDateCreated;
    }

    public String getDateCreatedAsString() {
        return SimpleDateFormat.getDateTimeInstance().format(mDateCreated);
    }

    public String getDateCreatedAsStringUTC() {
        // use XML dateTimeType format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd'T'HH:mm:ss.SSSZ", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(mDateCreated);
    }

    public void setDateCreatedFromString(String dateTime) {
        try {
            mDateCreated = SimpleDateFormat.getDateTimeInstance().parse(dateTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public long getDateCreatedAsLong() {
        return mDateCreated.getTime();
    }

    public void setDateCreatedFromLong(long dateTime) {
        mDateCreated = new Date(dateTime);
    }

    public void setDateCreatedFromStringUTC(String dateTime) {
        try {
            // use XML dateTimeType format
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd'T'HH:mm:ss.SSSZ",
                    Locale.US);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            mDateCreated = formatter.parse(dateTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean equals(Object object) {
        if (this == object)
            return true;
        // it also takes care of the case that rhs is null
        if (!(object instanceof Note))
            return false;

        Note note = (Note) object;
        if (!this.mNote.equals(note.mNote))
            return false;
        if (!this.mDateCreated.equals(note.mDateCreated))
            return false;

        return true;
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

        @Override
        public Note createFromParcel(Parcel inParcel) {
            return new Note(inParcel);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }

    };

    public Note(Parcel inParcel) {
        mNote = inParcel.readString();
        setDateCreatedFromLong(inParcel.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeString(mNote);
        outParcel.writeLong(getDateCreatedAsLong());
    }
}
