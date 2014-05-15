
package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExperimentViewTagsFragment extends Fragment {
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_experiment_tag_viewing, container,
                false);

        LinearLayout layout = (LinearLayout) mView
                .findViewById(R.id.layout_fragment_tag_viewing_tags);

        for (int i = 1; i < 4; i++) {
            TextView tv = (TextView) inflater.inflate(R.layout.textview_experiment_tag, null);
            tv.setText("Tag" + i);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(8, 0, 8, 0);
            tv.setTextAppearance(tv.getContext(), android.R.style.TextAppearance_Small);
            layout.addView(tv, layoutParams);
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.commit();
        return mView;
    }
}
