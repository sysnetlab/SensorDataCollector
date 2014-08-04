/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

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
