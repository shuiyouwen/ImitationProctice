package com.syw.imitationproctice.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.syw.imitationproctice.R;

/**
 * Created by Shui on 2018/6/19.
 */
public class MusicListBehavior extends HeaderBehavior {
    public MusicListBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        View controller = parent.findViewById(R.id.rl_controller);
        View headerExpand = parent.findViewById(R.id.cl_header_expand);
        int top = controller.getMeasuredHeight() + headerExpand.getMeasuredHeight();
        child.layout(0, top, child.getMeasuredWidth(), top + child.getMeasuredHeight());
        return super.onLayoutChild(parent, child, layoutDirection);
    }
}
