package sysnetlab.android.sdc.ui.adaptors;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.ui.UserInterfaceUtil;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

// Icons from www.thenounproject.com.  Creative Commons Attribution License
// Tags - #48635
// Notes - #8988
// Sensors - #7250
// Varying resolution icons for android generated using 
//  	http://android-ui-utils.googlecode.com/hg/asset-studio/dist/icons-launcher.html

public class OperationAdapter extends BaseAdapter {
    // Operation mode: create/view experiment
    public static final int CREATE_EXPERIMENT = 1;
    public static final int VIEW_EXPERIMENT = 2;

	// Total number of operations.
	public static final int OP_COUNT = 3;

	// Must start from zero and increment by 1.
	public static final int OP_TAGS = 0;
	public static final int OP_NOTES = 1;
	public static final int OP_SENSORS = 2;

	// Names corresponding to the above operations
	private static final String OP_TAGS_NAME = "Tags";
	private static final String OP_NOTES_NAME = "Notes";
	private static final String OP_SENSORS_NAME = "Sensors";

	private Activity mActivity;
	private Experiment mExperiment;
	private int mMode;

    public OperationAdapter(Activity a, Experiment e, int m) {
        mExperiment = e;
        mActivity = a;
        mMode = m;
    }
	
	@Override
	public int getCount() {
		return OP_COUNT;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.operation_listitem, viewGroup, false);
        }
        
	    switch (mMode) {
	        case CREATE_EXPERIMENT:
	            view = getViewForCreatingExperiment(position, view);
	            break;
	        case VIEW_EXPERIMENT:
	            view = getViewForViewingExperiment(position, view);
	            break;
	    }
	    
	    return view;
	}
	
	private View getViewForCreatingExperiment(int position, View view) {
	    
        ImageView icon = (ImageView) view.findViewById(R.id.iv_image);
        TextView operation = (TextView) view.findViewById(R.id.tv_maintext);
        TextView operationInfo = (TextView) view.findViewById(R.id.tv_subtext);

        // TODO: Replace the help text with other information when available,
        // e.g., list of entered tags,
        // selected sensors, or first words of notes??
        if (position == OP_TAGS) {
            icon.setImageResource(R.drawable.icon_tags);
            operation.setText(OP_TAGS_NAME);
            operationInfo.setText(view.getResources().getString(
                    R.string.text_run_tag_operation_text));     
            //listTagsInView(view);
        } else if (position == OP_NOTES) {
            icon.setImageResource(R.drawable.icon_notes);
            operation.setText(OP_NOTES_NAME);
            operationInfo.setText(view.getResources().getString(
                    R.string.text_run_note_operation_text));    
            //listNotesInView(view);
        } else if (position == OP_SENSORS) {
            icon.setImageResource(R.drawable.icon_sensors);
            operation.setText(OP_SENSORS_NAME);
            operationInfo.setText(view.getResources().getString(
                    R.string.text_run_sensor_operation_text));
            //listSensorsSummaryInView(view);
        }

        return view;	    
	}

	private View getViewForViewingExperiment(int position, View view) {
	    
        ImageView icon = (ImageView) view.findViewById(R.id.iv_image);
        TextView operation = (TextView) view.findViewById(R.id.tv_maintext);

        if (position == OP_TAGS) {
            icon.setImageResource(R.drawable.icon_tags);
            operation.setText(OP_TAGS_NAME);
            listTagsInView(view);
        } else if (position == OP_NOTES) {
            icon.setImageResource(R.drawable.icon_notes);
            operation.setText(OP_NOTES_NAME);
            listNotesInView(view);
        } else if (position == OP_SENSORS) {
            icon.setImageResource(R.drawable.icon_sensors);
            operation.setText(OP_SENSORS_NAME);
            listSensorsSummaryInView(view);
        }
        
        return view;
	}
	
	private void listTagsInView(View view) {
        if (mExperiment == null || mExperiment.getTags() == null || mExperiment.getTags().isEmpty()) {
            TextView textview = (TextView) view.findViewById(R.id.tv_subtext);
            textview.setText(view.getResources().getString(
                    R.string.text_no_tagging_action_performed));
            textview.setVisibility(View.VISIBLE);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_subtext);
            layout.setVisibility(View.GONE);
            return;
        }
        
        ((TextView) view.findViewById(R.id.tv_subtext)).setVisibility(View.GONE);
	    
	    LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_subtext);
	    layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 24, 0);
        
        layout.removeAllViews();
        for (Tag tag : mExperiment.getTags()) {
            TextView tvTag = new TextView(mActivity);
            //tvTag.setBackgroundResource(R.drawable.rounded_corner);
            tvTag.setTextAppearance(mActivity, android.R.attr.textAppearanceSmall);
            tvTag.setText(tag.getName());
            tvTag.setPadding(0, 2, 8, 2);
            layout.addView(tvTag, layoutParams);
        }
	    
	    layout.setVisibility(View.VISIBLE);
	}
	
    private void listNotesInView(View view) {
        if (mExperiment == null || mExperiment.getNotes() == null
                || mExperiment.getNotes().isEmpty()) {
            TextView textview = (TextView) view.findViewById(R.id.tv_subtext);
            textview.setText(view.getResources().getString(R.string.text_no_notes_were_taken));
            textview.setVisibility(View.VISIBLE);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_subtext);
            layout.setVisibility(View.GONE);
            return;
        }
        
        ((TextView) view.findViewById(R.id.tv_subtext)).setText(view.getResources().getString(
                R.string.text_tap_to_see_more_notes)); 
        ((TextView) view.findViewById(R.id.tv_subtext)).setVisibility(View.VISIBLE);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_subtext);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 24, 0);

        layout.removeAllViews();        
        Note note = mExperiment.getNotes().get(0);
        TextView tvNote = new TextView(mActivity);
        //tvNote.setBackgroundResource(R.drawable.rounded_corner);
        tvNote.setTextAppearance(mActivity, android.R.attr.textAppearanceSmall);
        tvNote.setText(note.getDateCreatedAsString() + ": " + note.getNote());
        tvNote.setPadding(0, 2, 8, 2);
        UserInterfaceUtil.setEllipsizeforTextView(tvNote, TextUtils.TruncateAt.END);

        layout.addView(tvNote, layoutParams);

        layout.setVisibility(View.VISIBLE);
    }
    
    private void listSensorsSummaryInView(View view) {
        TextView textview = (TextView) view.findViewById(R.id.tv_subtext);
        if (mExperiment == null || mExperiment.getSensors() == null
                || mExperiment.getSensors().isEmpty()) {
            textview.setText(view.getResources().getString(R.string.text_no_sensors_were_selected));
        } else {
            textview.setText(view.getResources().getString(R.string.text_tap_to_see_more_sensors));
        }
        textview.setVisibility(View.VISIBLE);
        return;
    }
	
}
