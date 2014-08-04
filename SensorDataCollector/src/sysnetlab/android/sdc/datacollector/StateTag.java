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
