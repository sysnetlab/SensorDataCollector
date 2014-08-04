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
