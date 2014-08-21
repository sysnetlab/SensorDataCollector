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

package sysnetlab.android.sdc.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.GridView;

public class TaggingGridView extends GridView {
    private boolean mTagResized = false;
    int mGdvHeight;

    public TaggingGridView(Context context) {
        super(context);
    }

    public TaggingGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TaggingGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * the code below follows the documentation at
     * http://developer.android.com/reference/android/widget/GridView.html but
     * it produces many "requestLayout() improperly called by
     * android.widget.TextView warning messages Futher study is needed
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mGdvHeight = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(width, mGdvHeight);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (mTagResized && UserInterfaceUtils.isInLayoutCompatible(this))
            return;

        for (int i = 0; i < getChildCount(); i++) {
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    mGdvHeight / getChildCount());
            getChildAt(i).setLayoutParams(param);
        }
        mTagResized = true;
    }
}
