package sysnetlab.android.sdc.ui;

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
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        super.onTouchEvent(event);
        return false;
    }   
    

    @Override
    public boolean performClick() {
        return super.performClick();
    }      
}
