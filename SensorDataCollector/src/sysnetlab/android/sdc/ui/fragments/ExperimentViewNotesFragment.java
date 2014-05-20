
package sysnetlab.android.sdc.ui.fragments;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datacollector.Note;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ExperimentViewNotesFragment extends Fragment {
    private View mView;
    private OnFragmentClickListener mCallback;    
    
    public interface OnFragmentClickListener {
        public void onImageViewMoreOrLessNotesClicked_ExperimentViewNotesFragment();
    }    
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentClickListener");
        }    
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        ((ImageView) mView.findViewById(R.id.imageview_notes_plusminus))
        .setOnClickListener(new ImageView.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mCallback.onImageViewMoreOrLessNotesClicked_ExperimentViewNotesFragment();

            }
        });        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_note_viewing, container,
                false);

        ArrayList<Note> lstNotes = ExperimentManagerSingleton.getInstance().getActiveExperiment()
                .getNotes();
        
        /*
        lstNotes.add(new Note("aaa", "2014-05-14"));
        lstNotes.add(new Note("bbb", "2014-05-14"));
        lstNotes.add(new Note("ccc", "2014-05-14"));
        lstNotes.add(new Note("ddd", "2014-05-14"));
        */
        
        if (lstNotes != null && !lstNotes.isEmpty()) {
            Note note = lstNotes.get(0);
            ((TextView) mView.findViewById(R.id.textview_fragment_note_viewing_note))
                    .setText(note.getNote());
        } else {
            ((TextView) mView.findViewById(R.id.textview_fragment_note_viewing_note))
                    .setText(getResources().getString(R.string.text_experiment_has_no_notes));
        }

        return mView;
    }

}
