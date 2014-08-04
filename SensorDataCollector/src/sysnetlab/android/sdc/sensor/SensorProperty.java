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

package sysnetlab.android.sdc.sensor;

import android.text.TextUtils;

public class SensorProperty {
    private String mName;
    private String mValue;

    public SensorProperty() {
        this("", "");
    }

    public SensorProperty(String name, String value) {
        mName = name;
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(final String value) {
        mValue = value;
    }
    
    public boolean equals(Object rhs) {
        if (this == rhs) return true;
        
        if (!(rhs instanceof SensorProperty)) return false;
        
        SensorProperty property = (SensorProperty) rhs;
        
        if (!TextUtils.equals(mName, property.mName)) return false;
        
        if (!TextUtils.equals(mValue, property.mValue)) return false;
        
        return true;
    }
}
