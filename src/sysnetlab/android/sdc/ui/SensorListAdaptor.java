/* $Id$ */
package sysnetlab.android.sdc.ui;

import java.util.List;

import sysnetlab.android.sdc.datacollector.DataCollectionState;
import sysnetlab.android.sdc.sensor.AndroidSensor;
import edu.vsu.cs.sensordatacollector.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


public class SensorListAdaptor extends ArrayAdapter<AndroidSensor> {
	private final List<AndroidSensor> mList;
	private final Activity mContext;

	public SensorListAdaptor(Activity context, List<AndroidSensor> list) {
		super(context, R.layout.sensor_row_layout, list);
		mContext = context;
		mList = list;
	}

	static class ViewHolder {
		protected TextView text;
		protected CheckBox checkbox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = mContext.getLayoutInflater();
			view = inflator.inflate(R.layout.sensor_row_layout, null);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) view.findViewById(R.id.label);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (DataCollectionState.getState() == DataCollectionState.DATA_COLLECTION_STOPPED) {
						AndroidSensor sensor = (AndroidSensor) viewHolder.checkbox.getTag();
						sensor.setSelected(buttonView.isChecked());
					} else {
						viewHolder.checkbox.toggle();
			    		Toast.makeText(mContext, "Data Collection In Progress!", Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			view.setTag(viewHolder);
		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.checkbox.setTag(mList.get(position));
		holder.text.setText(mList.get(position).getName());
		holder.checkbox.setChecked(mList.get(position).isSelected());
		return view;
	}
}
