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

package sysnetlab.android.sdc.ui.fragments;

import java.util.ArrayList;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.ui.CreateExperimentActivity;
import sysnetlab.android.sdc.ui.ViewExperimentActivity;
import sysnetlab.android.sdc.ui.adaptors.TagListAdapter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.Activity;

public class ExperimentEditTagsFragment extends Fragment {
    private OnFragmentClickListener mCallback;
    private View mView;
    private TagListAdapter mTagListAdapter;

    public interface OnFragmentClickListener {
        public boolean onBtnAddTagClicked_ExperimentEditTagsFragment(String strTag,
                String strDescription);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // TODO: handle configuration changes

        mView = inflater.inflate(R.layout.fragment_experiment_tag_editing, container, false);

        ListView lv = (ListView) mView.findViewById(R.id.listview_tags);

        ArrayList<Tag> tmpTags;
        Activity activity = getActivity();

        if (activity instanceof CreateExperimentActivity) {
            tmpTags = (ArrayList<Tag>) ((CreateExperimentActivity) activity).getExperiment()
                    .getTags();
        } else {
            tmpTags = (ArrayList<Tag>) ((ViewExperimentActivity) activity).getExperiment()
                    .getTags();
        }
        mTagListAdapter = new TagListAdapter(activity, tmpTags);

        Log.d("SensorDataCollector", "ExperimentEditTagsFragment.onCreateView: tmpTags = "
                + tmpTags);
        Log.d("SensorDataCollector", "ExperimentEditTagsFragment.onCreateView: lv = " + lv);
        Log.d("SensorDataCollector", "ExperimentEditTagsFragment.onCreateView: adapter = "
                + mTagListAdapter);

        if (activity instanceof CreateExperimentActivity) {
            lv.setAdapter(mTagListAdapter);
        }
        else
            Log.d("SensorDataCollector", "not yet implemented for ViewExperimentActivity");
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: handle configuration changes
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentClickListener");
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Button) mView.findViewById(R.id.btn_add_tag))
                .setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ExperimentEditTagsFragment", "Add Tag Clicked");
                        EditText editTextTag = (EditText) mView.findViewById(R.id.edittext_tag);

                        EditText editTextDescription = (EditText) mView
                                .findViewById(R.id.edittext_description);
                        if (mCallback.onBtnAddTagClicked_ExperimentEditTagsFragment(editTextTag
                                .getText().toString(),
                                editTextDescription.getText().toString()))
                        {
                            mTagListAdapter.notifyDataSetChanged();
                            editTextTag.setText("");
                            editTextDescription.setText("");
                        }

                    }
                });

    }

    public ArrayAdapter<Tag> getTagListAdapter() {
        return mTagListAdapter;
    }

    public boolean hasTags() {
        String tagName = ((EditText) mView.findViewById(R.id.edittext_tag))
                .getText().toString();
        if (!tagName.trim().equals(""))
            return true;
        String tagDescription = ((EditText) mView.findViewById(R.id.edittext_description))
                .getText().toString();
        if (!tagDescription.trim().equals(""))
            return true;
        return false;
    }
}
