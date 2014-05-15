
package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
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

        ((TextView) mView.findViewById(R.id.textview_fragment_note_viewing_note))
                .setText("This is a note.");

        return mView;
    }

}
