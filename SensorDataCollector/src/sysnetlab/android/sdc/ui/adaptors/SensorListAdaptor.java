/* $Id$ */
package sysnetlab.android.sdc.ui.adaptors;

import java.util.List;

import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;



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
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					AndroidSensor sensor = (AndroidSensor) viewHolder.checkbox.getTag();
					sensor.setSelected(buttonView.isChecked());
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
