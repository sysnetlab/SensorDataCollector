
package sysnetlab.android.sdc.datacollector;

public class StateTag {
    private Tag mTag;
    private TaggingState mState;

    public StateTag(Tag tag, TaggingState state) {
        mTag = tag;
        state = mState;
    }

    public Tag getTag() {
        return mTag;
    }

    public TaggingState getState() {
        return mState;
    }
}
