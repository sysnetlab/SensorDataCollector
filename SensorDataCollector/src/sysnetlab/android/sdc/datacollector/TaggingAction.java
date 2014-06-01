
package sysnetlab.android.sdc.datacollector;

public class TaggingAction {
    private Tag mTag;
    private ExperimentTime mTime;
    private TaggingState mTagState;
    
    public TaggingAction(Tag tag, ExperimentTime time, TaggingState tagState) {
        mTag = tag;
        mTime = time;
        mTagState = tagState;
    }   
    
    public TaggingAction(Tag tag, ExperimentTime time) {
        mTag = tag;
        mTime = time;
        mTagState = TaggingState.TAG_CONTEXT;
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
    
    public TaggingState getTagState() {
        return mTagState;
    }
    
    public void setTagState(TaggingState tagState) {
        mTagState = tagState;
    }
    
    public String toString() {
        return mTag.toString() + "\n" + mTime.toString() + "\n" + mTagState;
    }
    
    public boolean equals(Object rhs) {
        if (rhs == this) {
            return true;
        }
        
        if (!(rhs instanceof TaggingAction)) {
            return false;
        }
        
        TaggingAction action = (TaggingAction) rhs;
        
        if (mTag != null && action.mTag == null) {
            return false;
        } else if (!mTag.equals(action.mTag)) {
            return false;
        }
        
        if (mTime != null && action.mTime == null) {
            return false;
        } else if (!mTime.equals(action.mTime)) {
            return false;
        }
        
        if (mTagState != action.mTagState) {
            return false;
        }
        
        return true;
    }
}
