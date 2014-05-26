
package sysnetlab.android.sdc.datacollector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Note implements Parcelable {
    private String mNote;
    private String mDateTime;

    public Note(String note) {
        mNote = note != null ? note : "";

        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        mDateTime = df.format(Calendar.getInstance().getTime());
    }

    public Note(String note, String dt) {
        mNote = note != null ? note : "";
        mDateTime = dt != null ? dt : "";
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String mNote) {
        this.mNote = mNote;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String dateTime) {
        mDateTime = dateTime;
    }
    
    public boolean equals(Object rhs) {
        if (this == rhs) return true;
        // it also takes care of the case that rhs is null
        if (!(rhs instanceof Note)) return false;
        
        Note note = (Note) rhs;
        // Considering that mNote or mDateTime may be null, use TextUtils  
        if (!TextUtils.equals(mNote,  note.mNote)) return false;
        if (!TextUtils.equals(mDateTime, note.mDateTime)) return false;
        
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
        mDateTime = inParcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeString(mNote);
        outParcel.writeString(mDateTime);
    }
}
