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

package sysnetlab.android.sdc.ui.adaptors;

import java.util.List;

import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SensorListAdapter extends ArrayAdapter<AbstractSensor> {
    private final Activity mContext;
    private final int mCheckBoxVisiblity;

    public SensorListAdapter(Activity context, List<AbstractSensor> list, int checkBoxVisibility) {
        super(context, R.layout.sensor_row_layout, list);
        mContext = context;
        mCheckBoxVisiblity = checkBoxVisibility;
    }

    public SensorListAdapter(Activity context, List<AbstractSensor> list) {
        this(context, list, View.VISIBLE);
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
            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            AbstractSensor sensor = (AbstractSensor) viewHolder.checkbox.getTag();
                            sensor.setSelected(buttonView.isChecked());
                        }
                    });

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.checkbox.setTag(getItem(position));
        holder.text.setText(getItem(position).getName());
        holder.checkbox.setChecked(getItem(position).isSelected());
        holder.checkbox.setVisibility(mCheckBoxVisiblity);
        return view;
    }
}
