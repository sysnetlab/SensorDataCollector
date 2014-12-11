package sysnetlab.android.sdc.replay;

import java.io.BufferedReader;
import java.io.InputStreamReader;


import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SensingReplayer {

	private InputStreamReader isReader;
	private InputStreamReader isMetaDataReader;
	private SensorEventListener listener;
	
	public SensingReplayer(InputStreamReader isReader, InputStreamReader isMetaDataReader, SensorEventListener listener)
	{
		this.isReader = isReader;
		this.isMetaDataReader = isMetaDataReader;
		this.listener = listener;
	}
	
	public void replay() {		
		try { 
			BufferedReader br = new BufferedReader(isReader); 		
			for(String line; (line = br.readLine()) != null; ) 
			{
				//parse line				
				//TODO: works for accelerometer only for now
				String [] strValues = line.split("[\\s,]+");
				
				if(strValues.length > 4)
				{
					long timestamp = Long.parseLong(strValues[0]);
				
					float [] values = new float [3];
					for(int i=0; i<3; i++)
					{
						values[i] = Float.parseFloat(strValues[i+2]);
					}
				
					//create a SensorEvent object
					//TODO: complete accuracy and sensor, check timestamp
					
					/*
					SensorEvent se = WhiteBox.invokeConstructor(SensorEvent.class, 3);
					se.timestamp = timestamp;
					se.values[0] = values[0];
					se.values[1] = values[1];
					se.values[2] = values[2];
					*/
					
					//se.values = values;
					//when(se.values).thenReturn(values);
					//when(se.accuracy).thenReturn(0);
					//when(se.sensor).thenReturn(Sensor.TYPE_ACCELEROMETER);
				
					//listener.onSensorChanged(se);
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			//...
		}		
	}

}
