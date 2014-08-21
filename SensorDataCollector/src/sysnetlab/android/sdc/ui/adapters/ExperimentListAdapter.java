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

package sysnetlab.android.sdc.ui.adapters;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.ui.UserInterfaceUtils;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ExperimentListAdapter extends ArrayAdapter<Experiment> {
    private final Activity mContext;

    public ExperimentListAdapter(Activity context, List<Experiment> list) {
        super(context, R.layout.experiment_row_layout, list);
        mContext = context;
        setList(list);
    }

    public ExperimentListAdapter(Activity context) {
        super(context, R.layout.experiment_row_layout);
        mContext = context;
    }

    public void setList(List<Experiment> list) {
        clear();
        if (list != null) {
            UserInterfaceUtils.addAllCompatible(this, list);
        }
    }

    static class ViewHolder {
        protected TextView text;
        protected TextView dateCreated;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = mContext.getLayoutInflater();
            view = inflator.inflate(R.layout.experiment_row_layout, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.label);
            viewHolder.dateCreated = (TextView) view.findViewById(R.id.date_created);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(getItem(position).getName());
        holder.dateCreated.setText(getItem(position).getDateTimeCreatedAsString());
        return view;
    }
}
