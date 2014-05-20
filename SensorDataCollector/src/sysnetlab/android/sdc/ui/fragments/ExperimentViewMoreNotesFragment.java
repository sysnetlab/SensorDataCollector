
package sysnetlab.android.sdc.ui.fragments;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.ui.GestureEventListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ExperimentViewMoreNotesFragment extends Fragment {

    private View mView;
    private OnFragmentClickListener mCallback;
    private int mCurrentNoteNo;

    
    public interface OnFragmentClickListener {
        public void onBtnBackClicked_ExperimentViewMoreNotesFragment();
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        mView.setOnTouchListener(new GestureEventListener(getActivity()) {

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                
                Experiment experiment = ExperimentManagerSingleton.getInstance().getActiveExperiment();
                if (mCurrentNoteNo > 0) {
                        mCurrentNoteNo --;
                        updateNoteView(experiment.getNotes(), mCurrentNoteNo);
                } 
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                
                Experiment experiment = ExperimentManagerSingleton.getInstance().getActiveExperiment();
                if (mCurrentNoteNo < experiment.getNotes().size() - 1) {
                        mCurrentNoteNo ++;
                        updateNoteView(experiment.getNotes(), mCurrentNoteNo);
                } 
            }

            @Override
            public void onSwipeTop() {
                // TODO Auto-generated method stub
                super.onSwipeTop();
            }

            @Override
            public void onSwipeBottom() {
                // TODO Auto-generated method stub
                super.onSwipeBottom();
            }
            
        });
        
        ((Button) mView.findViewById(R.id.button_fragment_experiment_view_more_notes_back))
        .setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCallback.onBtnBackClicked_ExperimentViewMoreNotesFragment();
            }
        });        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // super.onCreateView(inflater, container, savedInstanceState);
        
        mView = inflater.inflate(R.layout.fragment_experiment_view_more_notes, container, false);

        Experiment experiment = ExperimentManagerSingleton.getInstance().getActiveExperiment();

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_more_notes_experiment_name))
                .setText(experiment.getName());

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_more_notes_experiment_time_created))
                .setText(experiment.getDateTimeCreated());

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_more_notes_experiment_time_done))
                .setText(experiment.getDateTimeDone());

        if (experiment.getNotes().size() > 0) {

            mCurrentNoteNo = 0;
            
            updateNoteView(experiment.getNotes(), mCurrentNoteNo);
            
        } else {
            ((TextView) mView
                    .findViewById(R.id.textview_fragment_experiment_view_more_notes_note_indicator))
                    .setText("0/0");
            
            ((TextView) mView
                    .findViewById(R.id.textview_fragment_experiment_view_more_notes_note_time))
                    .setText("");       
            
            ((TextView) mView
                    .findViewById(R.id.textview_fragment_experiment_view_more_notes_note_text))
                    .setText(getResources().getString(R.string.text_experiment_has_no_notes));   
        }
        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
            mCallback = (OnFragmentClickListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSwipeListener");            
        } 
    }
    
    private void updateNoteView(ArrayList<Note> notes, int noteNo) {
        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_more_notes_note_indicator))
                .setText(Integer.toString(noteNo + 1) + "/" + notes.size());
        
        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_more_notes_note_time))
                .setText(notes.get(noteNo).getDateTime());       
        
        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_more_notes_note_text))
                .setText(notes.get(noteNo).getNote());           
    }
}
