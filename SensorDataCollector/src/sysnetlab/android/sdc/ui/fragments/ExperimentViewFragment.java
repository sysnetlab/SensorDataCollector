
package sysnetlab.android.sdc.ui.fragments;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.ui.UserInterfaceUtil;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExperimentViewFragment extends Fragment {
    private ExperimentViewTagsFragment mExperimentViewTagsFragment;
    private ExperimentViewMoreNotesFragment mExperimentViewMoreNotesFragment;
    private ExperimentSensorListFragment mExperimentSensorListFragment;
    private OnFragmentClickListener mCallback;

    public interface OnFragmentClickListener {
        public void onBtnBackClicked_ExperimentViewFragment();
        public void onBtnCloneClicked_ExperimentViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.fragment_experiment_view, container,
                false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((TextView) getActivity().findViewById(R.id.textview_experiment_view_experiment_name))
                .setText(((ViewExperimentActivity) getActivity()).getExperiment().getName());
        
        listTagsInView(getActivity());
        listNotesInView(getActivity());
        
        
        
        // use nested fragment
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        /*
        if (mExperimentViewTagsFragment == null) {
            mExperimentViewTagsFragment = new ExperimentViewTagsFragment();
            // mExperimentViewTagsFragment.setArguments(getArguments());
            transaction.add(R.id.layout_experiment_view_tags, mExperimentViewTagsFragment);
        }

        if (mExperimentViewNotesFragment == null) {
            mExperimentViewNotesFragment = new ExperimentViewNotesFragment();
            // mExperimentViewNotesFragment.setArguments(getArguments());
            transaction.add(R.id.layout_experiment_view_notes, mExperimentViewNotesFragment);
        }
        
        */
        
        if (mExperimentSensorListFragment == null) {
            mExperimentSensorListFragment = new ExperimentSensorListFragment();
            transaction.add(R.id.layout_experiment_view_sensor_list, mExperimentSensorListFragment);
        }
        transaction.commit();

        

        ((Button) getActivity().findViewById(R.id.button_experiment_view_clone))
                .setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mCallback.onBtnCloneClicked_ExperimentViewFragment();
                    }
                });

        ((Button) getActivity().findViewById(R.id.button_experiment_view_back))
                .setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mCallback.onBtnBackClicked_ExperimentViewFragment();
                    }
                });
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
    
    private void listTagsInView(Activity activity) {
        List<Tag> listTags = ((ViewExperimentActivity) getActivity()).getExperiment().getTags();

        if (listTags == null || listTags.isEmpty()) {
            ((TextView) activity.findViewById(R.id.textview_experiment_view_tag_subtext))
                    .setText(activity.getResources().getString(
                            R.string.text_experiment_has_no_tags));
            return;
        }
        
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layout_experiment_view_tag_list);
        
        int nTags = 0;
        boolean hasMore = false;
        for (Tag tag : listTags) {
            nTags ++;
            
            TextView textviewTag = new TextView(getActivity());

            textviewTag.setText(tag.getName());
            
//            textviewTag.setBackgroundResource(R.drawable.layerlist_background_bottom_border_line);
//            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(12, 0, 12, 0);
//            textviewTag.setLayoutParams(layoutParams);
            layout.addView(textviewTag);
            
            if (nTags > 3) {
                hasMore = true;
                break;
            }
        }
        
        if (hasMore) {
            TextView textviewTag = new TextView(getActivity());
            textviewTag.setText(activity.getResources().getString(R.string.text_tag_here_for_more));
            textviewTag.setBackgroundResource(R.drawable.layerlist_background_bottom_border_line);
            layout.addView(textviewTag);
        }
        
    }
    
    private void listNotesInView(Activity activity) {
        List<Note> listNotes = ((ViewExperimentActivity) getActivity()).getExperiment().getNotes();

        if (listNotes == null || listNotes.isEmpty()) {
            ((TextView) activity.findViewById(R.id.textview_experiment_view_notes_subtext))
                    .setText(activity.getResources().getString(
                            R.string.text_experiment_has_no_notes));
            return;
        }
        
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layout_experiment_view_note_list);
        
        
        int nNotes = 0;
        boolean hasMore = false;
        for (Note note : listNotes) {
            nNotes ++;
            
            TextView textviewNote = new TextView(getActivity());
            textviewNote.setText(note.getNote());
            // textviewNote.setBackgroundResource(R.drawable.layerlist_background_bottom_border_line);
            UserInterfaceUtil.setEllipsizeforTextView(textviewNote, TextUtils.TruncateAt.END);

            layout.addView(textviewNote);
            
            if (nNotes > 0) {
                hasMore = true;
                break;
            }
        }
        
        if (hasMore) {
            TextView textviewNote = new TextView(getActivity());
            textviewNote.setText(activity.getResources().getString(R.string.text_tag_here_for_more));
            textviewNote.setBackgroundResource(R.drawable.layerlist_background_bottom_border_line);
            
            textviewNote.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (mExperimentViewMoreNotesFragment == null) {
                        mExperimentViewMoreNotesFragment = new ExperimentViewMoreNotesFragment();
                    }
                    FragmentUtil.switchToFragment(getActivity(), mExperimentViewMoreNotesFragment,
                            "experimentviewmorenotes");

                }
            });
            
            layout.addView(textviewNote);
        }
    }
    
}
