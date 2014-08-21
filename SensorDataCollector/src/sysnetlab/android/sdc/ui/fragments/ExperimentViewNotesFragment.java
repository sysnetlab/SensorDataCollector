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

package sysnetlab.android.sdc.ui.fragments;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentManagerSingleton;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.ui.GestureEventListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExperimentViewNotesFragment extends Fragment {

    private View mView;
    private int mCurrentNoteNo;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mView.setOnTouchListener(new GestureEventListener(getActivity()) {

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

                Experiment experiment = ExperimentManagerSingleton.getInstance()
                        .getActiveExperiment();
                if (mCurrentNoteNo < experiment.getNotes().size() - 1) {
                    mCurrentNoteNo++;
                    updateNoteView(experiment.getNotes(), mCurrentNoteNo);
                }
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                Experiment experiment = ExperimentManagerSingleton.getInstance()
                        .getActiveExperiment();
                if (mCurrentNoteNo > 0) {
                    mCurrentNoteNo--;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_view_notes, container, false);

        Experiment experiment = ExperimentManagerSingleton.getInstance().getActiveExperiment();

        String strHeadingSubTextFormatter = getResources().getString(
                R.string.text_for_experiment_name_x);
        String strHeadingSubText = String.format(strHeadingSubTextFormatter, experiment.getName());
        ((TextView) mView.findViewById(R.id.textview_experiment_note_viewing_subtext))
                .setText(strHeadingSubText);

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_notes_experiment_time_created))
                .setText(experiment.getDateTimeCreatedAsString());

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_notes_experiment_time_done))
                .setText(experiment.getDateTimeDoneAsString());

        mCurrentNoteNo = 0;
        updateNoteView(experiment.getNotes(), mCurrentNoteNo);

        return mView;
    }

    private void updateNoteView(List<Note> list, int noteNo) {

        String strNoteCaption = "";
        String strNoteText = "";
        if (list == null || list.isEmpty()) {
            strNoteCaption = mView.getResources().getString(R.string.text_no_notes_were_taken);
            strNoteText = mView.getResources().getString(R.string.text_no_notes);
        } else {
            String strNoteCaptionFormatter = mView.getResources().getString(
                    R.string.text_note_x_of_y_taken_at_time_z);
            strNoteCaption = String.format(strNoteCaptionFormatter, noteNo + 1, list.size(), list
                    .get(noteNo).getDateCreatedAsString());
            strNoteText = list.get(noteNo).getNote();
        }

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_notes_note_caption))
                .setText(strNoteCaption);

        ((TextView) mView
                .findViewById(R.id.textview_fragment_experiment_view_notes_note_text))
                .setText(strNoteText);
    }
}
