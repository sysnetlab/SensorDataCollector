package sysnetlab.android.sdc.replay;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.hardware.SensorEventListener;

public class SensingReplayer {

	private InputStreamReader isReader;
	private SensorEventListener listener;
	
	public SensingReplayer(InputStreamReader isReader, SensorEventListener listener)
	{
		this.isReader = isReader;
		this.listener = listener;
	}
	
	public void replay() {		
		try { 
			BufferedReader br = new BufferedReader(isReader); 		
			for(String line; (line = br.readLine()) != null; ) 
			{
					// process the line.
				
					// create a SensorEvent object
				
					//listener.onSensorChanged(event);
			}
			br.close();
		}
		catch(Exception e)
		{
			//...
		}		
	}

}
