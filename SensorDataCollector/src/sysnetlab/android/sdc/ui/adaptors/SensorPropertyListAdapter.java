package sysnetlab.android.sdc.ui.adaptors;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.sensor.SensorProperty;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SensorPropertyListAdapter extends ArrayAdapter<SensorProperty> {
        private final List<SensorProperty> mList;
        private final Activity mActivity;

        public SensorPropertyListAdapter(Activity activity, List<SensorProperty> list) {
            super(activity, R.layout.sensor_property_row, list);
            mActivity = activity;
            mList = list;
        }
        
        static class SensorPropertyViewHolder {
            protected TextView textViewPropertyName;
            protected TextView textViewPropertyValue;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflator = mActivity.getLayoutInflater();
                view = inflator.inflate(R.layout.sensor_property_row, null);

                final SensorPropertyViewHolder viewHolder = new SensorPropertyViewHolder();
                viewHolder.textViewPropertyName = (TextView) view.findViewById(R.id.textview_sensor_property_name);
                viewHolder.textViewPropertyValue = (TextView) view.findViewById(R.id.textview_sensor_property_value);
                view.setTag(viewHolder);
            } else {
                view = convertView;
            }
            SensorPropertyViewHolder holder = (SensorPropertyViewHolder) view.getTag();
            holder.textViewPropertyName.setText(mList.get(position).getName());
            holder.textViewPropertyValue.setText(mList.get(position).getValue());
            return view;
        }
        
        public List<SensorProperty> getProperties(){
        	return mList;
        }
    }