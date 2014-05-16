
package sysnetlab.android.sdc.ui.adaptors;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Experiment;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ExperimentListAdaptor extends ArrayAdapter<Experiment> {
    private final List<Experiment> mList;
    private final Activity mContext;

    public ExperimentListAdaptor(Activity context, List<Experiment> list) {
        super(context, R.layout.experiment_row_layout, list);
        mContext = context;
        mList = list;
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
        holder.text.setText(mList.get(position).getName());
        holder.dateCreated.setText(mList.get(position).getDateTimeCreated());
        return view;
    }
}
