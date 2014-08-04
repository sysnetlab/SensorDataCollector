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

import java.util.ArrayList;
import java.util.List;
import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.StateTag;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.datacollector.TaggingState;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaggingTagListAdapter extends BaseAdapter {
    private List<StateTag> mListStateTags;
    private Activity mActivity;
    
    public TaggingTagListAdapter(Activity activity, List<Tag> listTags) {
        mActivity = activity; 
        
        mListStateTags = new ArrayList<StateTag>();
        
        for (Tag tag : listTags) {
            mListStateTags.add(new StateTag(tag, TaggingState.TAG_OFF));
        }
    }

    @Override
    public int getCount() {
        return mListStateTags.size();
    }

    @Override
    public Object getItem(int position) {
        return mListStateTags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = mActivity.getLayoutInflater();
            view = inflator.inflate(R.layout.tag_cell, parent, false);
            
            final ViewHolder viewHolder = new ViewHolder();
            
            viewHolder.text = (TextView) view.findViewById(R.id.textview_tag_cell);
            
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(mListStateTags.get(position).getTag().getName());    
        // view.setBackgroundColor(view.getResources().getColor(android.R.color.background_light));
        return view;
    }
    
    static class ViewHolder {
        protected TextView text;        
    }
}
