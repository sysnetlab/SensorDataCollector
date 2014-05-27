
package sysnetlab.android.sdc.ui.adaptors;

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
    private final List<Tag> mList;
    private final Activity mContext;

    public TagListAdapter(Activity context, List<Tag> list) {
        super(context, R.layout.tag_row_layout, list);
        mContext = context;
        mList = list;
    }

    static class ViewHolder {
        protected TextView tag;
        protected TextView description;
    }
    
    @Override
    public int getCount() {
        return mList.size();
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
        // TODO: If the Tag description exceeds one line, cut off and add ellipses.
        holder.tag.setText(mList.get(position).getName());
        holder.description.setText(mList.get(position).getShortDescription());
        return view;
    }
}
