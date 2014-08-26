package sysnetlab.android.sdc.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class TouchPassingListView extends ListView {

    public TouchPassingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TouchPassingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchPassingListView(Context context) {
        super(context);
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //super.onTouchEvent(event);
        return false;
    }   

}
