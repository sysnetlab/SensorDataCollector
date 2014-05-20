
package sysnetlab.android.sdc.datacollector;

public class TaggingAction {
    private Tag mTag;
    private ExperimentTime mTime;
    private TagState mTagState;
    
    public TaggingAction(Tag tag, ExperimentTime time, TagState tagState) {
        mTag = tag;
        mTime = time;
        mTagState = tagState;
    }   
    
    public TaggingAction(Tag tag, ExperimentTime time) {
        mTag = tag;
        mTime = time;
        mTagState = TagState.TAG_CONTEXT;
    } 
    
    public Tag getTag() {
        return mTag;
    }

    public void setTag(Tag tag) {
        mTag = tag;
    }

    public ExperimentTime getTime() {
        return mTime;
    }
    
    public void setTime(ExperimentTime time) {
        mTime = time;
    }
    
    public TagState getTagState() {
        return mTagState;
    }
    
    public void setTagState(TagState tagState) {
        mTagState = tagState;
    }
    
    public String toString() {
        return mTag.toString() + "\n" + mTime.toString() + "\n" + mTagState;
    }
}
