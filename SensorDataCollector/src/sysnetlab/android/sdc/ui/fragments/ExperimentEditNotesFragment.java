
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

    private OnFragmentClickListener mCallback;

    public interface OnFragmentClickListener {
        public void onButtonConfirmClicked_ExperimentEditNotesFragment(String note);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_experiment_note_editing, container,
                false);
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

        ((Button) getActivity().findViewById(R.id.button_experiment_note_editing_confirm))
                .setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        mCallback.onButtonConfirmClicked_ExperimentEditNotesFragment(
                                ((EditText) getActivity().findViewById(
                                        R.id.edittext_experiment_note_editing_note)).getText()
                                        .toString()
                                );
                    }
                });
    }
}
