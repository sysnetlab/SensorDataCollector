
package sysnetlab.android.sdc.test;

import sysnetlab.android.sdc.datacollector.Note;
import android.os.Parcel;
import android.test.AndroidTestCase;

public class NoteTests extends AndroidTestCase {
    private Note mNote;

    public NoteTests() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testConstructors() {
        mNote = new Note(null, null);
        assertEquals("The constructor failed to initialize the object with null arguments", "",
                mNote.getNote());
        assertNotNull("The constructor failed to initialize the object with null arguments",
                mNote.getDateCreatedAsString());        
        mNote = new Note("testNote");
        assertEquals("The constructor failed to initialize the Note", "testNote", mNote.getNote());
        /*
        Log.e("SensorDataCollector.UnitTest", "mNote = " 
                + mNote.getNote() + ":" + mNote.getDateCreatedAsStringUTC() 
                + " after = " 
                + after.getNote() + ":" + after.getDateCreatedAsStringUTC());
        Log.e("SensorDataCollector.UnitTest", "mNote = " 
                + mNote.getNote() + ":" + mNote.getDateCreatedAsString() 
                + " after = " 
                + after.getNote() + ":" + after.getDateCreatedAsString());
                */
    }

    public void testParcel() {
        Note after;

        mNote = new Note("test");
        Parcel parcel = Parcel.obtain();
        mNote.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        after = Note.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        assertEquals("The Note failed being reconstructed from a parcel", mNote, after);

    }
}
