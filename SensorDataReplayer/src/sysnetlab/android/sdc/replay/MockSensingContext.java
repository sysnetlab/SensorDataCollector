package sysnetlab.android.sdc.replay;

import android.content.Context;
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
		if(name == SENSOR_SERVICE)	
		{
			//TODO: mock the SensorManager
			return null;
		}
		else
		{
			return super.getSystemService(name);
		}
	}
}
