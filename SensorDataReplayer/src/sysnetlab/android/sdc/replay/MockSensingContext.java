package sysnetlab.android.sdc.replay;

import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import android.content.Context;
import android.hardware.SensorManager;
import android.test.RenamingDelegatingContext;


public class MockSensingContext extends RenamingDelegatingContext {

	private static final String MOCK_FILE_PREFIX = "test.";

	public MockSensingContext(Context context)
	{
		super(context, MOCK_FILE_PREFIX);
	}
	
	@Override
	public Object getSystemService(String name)
	{
		Object response = super.getSystemService(name);
		if(name == SENSOR_SERVICE)	
		{
			//TODO: mock the SensorManager			
			//using mockito
			SensorManager spySensorManager = (SensorManager) spy(response);
			/*
			when(spySensorManager.registerListener(anyObject(), anyObject(), anyInt()).thenAnswer(new Answer() {
				public Object answer(InvocationOnMock invocation) {
					Object[] args = invocation.getArguments();
					Object mock = invocation.getMock();
					return "called with arguments: " + args;
				}
			});
			*/
			
			//mSensorManager.registerListener(this, mAccelerometer,
			//		SensorManager.SENSOR_DELAY_UI);
			
			return spySensorManager;
		}
		else
		{
			return response;
		}
	}
}
