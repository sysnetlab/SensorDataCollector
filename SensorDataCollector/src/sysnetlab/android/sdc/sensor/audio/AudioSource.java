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

package sysnetlab.android.sdc.sensor.audio;

public class AudioSource {
    private int mSourceId;
    private int mSourceNameResId;

    public AudioSource(int id, int resId) {
        mSourceId = id;
        mSourceNameResId = resId;
    }

    public int getSourceId() {
        return mSourceId;
    }

    public int getSourceNameResId() {
        return mSourceNameResId;
    }

    public boolean equals(Object rhs) {
        if (this == rhs)
            return true;

        if (!(rhs instanceof AudioSource))
            return false;

        AudioSource s = (AudioSource) rhs;

        if (mSourceId != s.mSourceId)
            return false;

        return true;
    }
}
