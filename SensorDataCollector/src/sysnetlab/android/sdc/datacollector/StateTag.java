
package sysnetlab.android.sdc.datacollector;

public class StateTag {
    private Tag mTag;
    private TaggingState mState;

    public StateTag(Tag tag, TaggingState state) {
        mTag = tag;
        mState = state;
    }

    public Tag getTag() {
        return mTag;
    }

    public TaggingState getState() {
        return mState;
    }

    public void setState(TaggingState taggingState) {
        mState = taggingState;
    }
}
