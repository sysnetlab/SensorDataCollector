
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

public class TagListAdaptor extends ArrayAdapter<Tag> {
    private final List<Tag> mList;
    private final Activity mContext;

    public TagListAdaptor(Activity context, List<Tag> list) {
        super(context, R.layout.tag_row_layout, list);
        mContext = context;
        mList = list;
    }

    static class ViewHolder {
        protected TextView text;        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = mContext.getLayoutInflater();
            view = inflator.inflate(R.layout.tag_row_layout, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.tagName);            
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(mList.get(position).getName());        
        return view;
    }
}
