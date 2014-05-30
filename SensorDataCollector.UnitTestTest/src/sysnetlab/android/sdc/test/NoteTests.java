
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
        Parcel parcel;
        Note after;

        mNote = new Note(null, null);
        assertEquals("The constructor failed to initialize the object with null arguments", "",
                mNote.getNote());
        assertEquals("The constructor failed to initialize the object with null arguments", "",
                mNote.getDateCreatedAsString());

        mNote = new Note("testNote");
        assertEquals("The constructor failed to initialize the Note", "testNote", mNote.getNote());

        mNote = new Note("test");
        parcel = Parcel.obtain();
        mNote.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        after = Note.CREATOR.createFromParcel(parcel);
        parcel.recycle();
    }

    public void testParcel() {
        Note after;
        mNote = new Note("test");
        Parcel parcel = Parcel.obtain();
        mNote.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        after = Note.CREATOR.createFromParcel(parcel);
        assertTrue("The Note failed to be constructed out of a Parcel", mNote.equals(after));
    }
}
