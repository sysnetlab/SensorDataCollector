
package sysnetlab.android.sdc.ui;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datacollector.Note;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExperimentViewNotesFragment extends Fragment {
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_note_viewing, container,
                false);

        ArrayList<Note> lstNotes = ExperimentManagerSingleton.getInstance().getActiveExperiment()
                .getNotes();
        if (lstNotes != null && !lstNotes.isEmpty()) {
            Note note = lstNotes.get(0);
            ((TextView) mView.findViewById(R.id.textview_fragment_note_viewing_note))
                    .setText(note.getNote());
        } else {
            ((TextView) mView.findViewById(R.id.textview_fragment_note_viewing_note))
                    .setText("No notes found.");
        }

        return mView;
    }

}
