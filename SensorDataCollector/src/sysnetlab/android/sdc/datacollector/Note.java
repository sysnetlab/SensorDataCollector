package sysnetlab.android.sdc.datacollector;

import java.text.DateFormat;
import java.util.Calendar;

public class Note {
	private String mNote;
	private String mDateTime;
	
	public Note(String note) {
		mNote = note;

		DateFormat df = DateFormat.getDateTimeInstance();
		mDateTime = df.format(Calendar.getInstance().getTime());		
	}
	
	public Note(String note, String dt) {
		mNote = note;
		mDateTime = dt;
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

	public void setDateTime(String mDateTime) {
		this.mDateTime = mDateTime;
	}
}