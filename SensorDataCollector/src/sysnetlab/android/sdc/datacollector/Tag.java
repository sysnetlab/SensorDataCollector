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

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Tag implements Parcelable {
    private String mName;
    private String mShortDescription;
    private String mLongDescription;
    
    public Tag(String name) {
        this(name, "", "");
    }

    public Tag(String name, String shortDesc) {
        this(name, shortDesc, "");
    }
 
    public Tag(String name, String shortDesc, String longDesc) {
        mName = name;
        mShortDescription = shortDesc;
        mLongDescription = longDesc;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public String getLongDescription() {
        return mLongDescription;
    }

    public String toString() {
        return mName;
    }

    public void setLongDescription(String mLongDescription) {
        this.mLongDescription = mLongDescription;
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs == this) {
            return true;
        }
        
        if (!(rhs instanceof Tag)) {
            return false;
        }
        
        Tag tag = (Tag) rhs;
       
        if (!TextUtils.equals(mName, tag.mName)){
            return false;
        }
        
        if (!TextUtils.equals(mShortDescription, tag.mShortDescription)) {
            return false;
        }

        if (!TextUtils.equals(mLongDescription, tag.mLongDescription)) {
            return false;
        }
        
        return true;
    }
    
    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {

        @Override
        public Tag createFromParcel(Parcel inParcel) {
            return new Tag(inParcel);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
        
    };
    
    public Tag(Parcel inParcel) {
        mName = inParcel.readString();
        mShortDescription = inParcel.readString();
        mLongDescription = inParcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeString(mName);
        outParcel.writeString(mShortDescription);
        outParcel.writeString(mLongDescription);       
    }
    
    public Parcelable.Creator<Tag> getCreator() {
		return CREATOR;
	}
}
