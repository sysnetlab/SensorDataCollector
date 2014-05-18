package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

// Icons from www.thenounproject.com.  Creative Commons Attribution License
// Tags - #48635
// Notes - #8988
// Sensors - #7250
// Varying resolution icons for android generated using 
//  	http://android-ui-utils.googlecode.com/hg/asset-studio/dist/icons-launcher.html


public class OperationAdapter extends BaseAdapter {
	
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

	public OperationAdapter(Activity a, Experiment e) {
		mExperiment = e;
		mActivity = a;
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
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        if(arg1==null)
        {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arg1 = inflater.inflate(R.layout.operation_listitem, arg2,false);
        }

        ImageView icon = (ImageView) arg1.findViewById(R.id.iv_image);
        TextView operation = (TextView)arg1.findViewById(R.id.tv_maintext);
        TextView operationInfo = (TextView)arg1.findViewById(R.id.tv_subtext);

        // TODO: Do some stuff to prevent long operationInfo text from wrapping.  If it gets too
        // long, cut it off and add ellipses.  
        // TODO: Replace the  help text with other information when available, e.g., list of entered tags, 
        // selected sensors, or first words of notes??
        if (arg0 == OP_TAGS) {
        	icon.setImageResource(R.drawable.icon_tags);
        	operation.setText(OP_TAGS_NAME);
        	operationInfo.setText("Mark important events during an experimental run.");
        }
        else if (arg0 == OP_NOTES) {
        	icon.setImageResource(R.drawable.icon_notes);
        	operation.setText(OP_NOTES_NAME);
        	operationInfo.setText("Log general information about an experiment.");
        }
        else if (arg0 == OP_SENSORS) {
        	icon.setImageResource(R.drawable.icon_sensors);
        	operation.setText(OP_SENSORS_NAME);
        	operationInfo.setText("Select and configure the sensors that will be used during the experiment.");
        }

        return arg1;
    }

} 