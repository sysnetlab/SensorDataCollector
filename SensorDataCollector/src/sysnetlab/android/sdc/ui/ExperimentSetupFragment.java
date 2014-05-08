package sysnetlab.android.sdc.ui;

import sysnetlab.android.sdc.R;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExperimentSetupFragment extends Fragment {
	private View mView;
	
    public interface OnFragmentClickListener {
    	public void onImgvTagsClicked_ExperimentSetupFrang();
    	public void onImgvNotesClicked_ExperimentSetupFrang();
    	public void onImgvSensorsClicked_ExperimentSetupFrang();    	
    	public void onBtnRunClicked_ExperimentSetupFragment(View v, AbstractSensor sensor);
    	public void onBtnBackClicked_ExperimentSetupFragment();
    }	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		
        mView = inflater.inflate(R.layout.fragment_experiment_setup_layout, container, false);
        return mView;		
	}

	public View getView() {
		return mView;
	}
}
