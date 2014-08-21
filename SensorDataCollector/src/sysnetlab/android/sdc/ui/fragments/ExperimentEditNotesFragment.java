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

import sysnetlab.android.sdc.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ExperimentEditNotesFragment extends Fragment {

    private View mView;
    private OnFragmentClickListener mCallback;

    public interface OnFragmentClickListener {
        public void onButtonAddNoteClicked_ExperimentEditNotesFragment(String note);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_note_editing, container,
                false);
        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnClickedListener");
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Button) getActivity().findViewById(R.id.button_experiment_note_editing_add_note))
                .setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        mCallback.onButtonAddNoteClicked_ExperimentEditNotesFragment(
                                ((EditText) getActivity().findViewById(
                                        R.id.edittext_experiment_note_editing_note)).getText()
                                        .toString()
                                );
                    }
                });
    }

    public boolean hasNotes() {
        String noteDescription = ((EditText) mView
                .findViewById(R.id.edittext_experiment_note_editing_note))
                .getText().toString();
        if (!noteDescription.trim().equals(""))
            return true;
        return false;
    }

    public View getView() {
        return mView;
    }
}
