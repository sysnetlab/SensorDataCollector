package sysnetlab.android.sdc.replay;

import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.test.RenamingDelegatingContext;


public class MockSensingContext extends RenamingDelegatingContext {

	private static final String MOCK_FILE_PREFIX = "test.";
	
	private SensorEventListener capturedEventListener = null;
	
	public MockSensingContext(Context context)
	{
		super(context, MOCK_FILE_PREFIX);
		
		//for mockito
		System.setProperty("dexmaker.dexcache", getCacheDir().getPath());
	}
	
	
	public SensorEventListener getCapturedEventListener()
	{
		return capturedEventListener;
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getSystemService(String name)
	{			
		Object response = super.getSystemService(name);
		if(name == SENSOR_SERVICE)	
		{
			//TODO: mock the SensorManager			
			SensorManager spySensorManager = (SensorManager) spy(response);
			
			when(spySensorManager.registerListener(any(SensorEventListener.class), 
												   any(Sensor.class), 
												   anyInt())).thenAnswer(new Answer() {
				public Object answer(InvocationOnMock invocation) {
					Object[] args = invocation.getArguments();
					capturedEventListener = (SensorEventListener) args[0];
					//Object mock = invocation.getMock();
					return true;
				}
			});
			
			return spySensorManager;
		}
		else
		{
			return response;
		}
	}
}
