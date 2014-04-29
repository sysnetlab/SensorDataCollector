/* $Id$ */
package edu.vsu.cs.sensordatacollector;

public class DataCollectionState {
	public static final int DATA_COLLECTION_IN_PROGRESS = 1;
	public static final int DATA_COLLECTION_STOPPED = 2;
	
	private static int mState = DATA_COLLECTION_STOPPED;
	
	public static int getState() {
		return mState;
	}
	
	public static void setState(int state) {
		mState = state;
	}
}
