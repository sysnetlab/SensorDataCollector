
package sysnetlab.android.sdc.ui.adaptors;

import java.util.List;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datastore.AbstractStore;
import sysnetlab.android.sdc.datastore.SimpleFileStore;
import sysnetlab.android.sdc.datastore.SimpleXMLFileStore;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class DataStoreListAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<AbstractStore> mStores;
    private int mInitialPositionChecked;

    public DataStoreListAdapter(Activity activity, List<AbstractStore> stores, int positionChecked) {
        super();
        mActivity = activity;
        mStores = stores;
        mInitialPositionChecked = positionChecked;
    }

    @Override
    public int getCount() {
        return mStores == null ? 0 : mStores.size();
    }

    @Override
    public Object getItem(int position) {
        if (mStores == null) {
            return null;
        } else {
            return mStores.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        protected TextView mainText;
        protected TextView subText;
        protected RadioButton radioButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = mActivity.getLayoutInflater();
            view = inflator.inflate(R.layout.datastore_row, parent, false);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.mainText = (TextView) view
                    .findViewById(R.id.textview_experiment_datastore_selecting_maintext);
            viewHolder.subText = (TextView) view
                    .findViewById(R.id.textview_experiment_datastore_selecting_subtext);
            viewHolder.radioButton = (RadioButton) view
                    .findViewById(R.id.radiobutton_experiment_datastore_selecting);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        if (mStores.get(position) instanceof SimpleXMLFileStore) {
            holder.mainText.setText(mActivity.getResources().getString(
                    R.string.text_simple_xml_file_store));
            holder.subText.setText(mActivity.getResources().getString(
                    R.string.text_simple_xml_file_store_subtext_text));
        } else if (mStores.get(position) instanceof SimpleFileStore) {
            holder.mainText.setText(mActivity.getResources().getString(
                    R.string.text_simple_file_store));
            holder.subText.setText(mActivity.getResources().getString(
                    R.string.text_simple_file_store_subtext_text));
        }

        holder.radioButton.setChecked(false);

        if (mInitialPositionChecked >= 0 && mInitialPositionChecked == position) {
            holder.radioButton.setChecked(true);
        } 
       
        
        return view;
    }
}
