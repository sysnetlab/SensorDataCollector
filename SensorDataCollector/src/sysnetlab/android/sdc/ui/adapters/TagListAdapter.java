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
import sysnetlab.android.sdc.datacollector.Tag;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TagListAdapter extends ArrayAdapter<Tag> {
    private final Activity mContext;

    public TagListAdapter(Activity context, List<Tag> list) {
        super(context, R.layout.tag_row_layout, list);
        mContext = context;
    }

    static class ViewHolder {
        protected TextView tag;
        protected TextView description;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = mContext.getLayoutInflater();
            view = inflator.inflate(R.layout.tag_row_layout, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.tag = (TextView) view.findViewById(R.id.tv_tagname);
            viewHolder.description = (TextView) view.findViewById(R.id.tv_tagdescription);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        // TODO: If the Tag description exceeds one line, cut off and add
        // ellipses.
        holder.tag.setText(getItem(position).getName());
        holder.description.setText(getItem(position).getShortDescription());
        return view;
    }
}
