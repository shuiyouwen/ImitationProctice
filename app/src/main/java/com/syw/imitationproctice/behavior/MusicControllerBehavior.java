package com.syw.imitationproctice.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.syw.imitationproctice.R;

import java.lang.ref.WeakReference;

/**
 * Created by Shui on 2018/6/19.
 */
public class MusicControllerBehavior extends HeaderBehavior {
    private WeakReference<View> mDependencyViewRef;

    public MusicControllerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        View headerExpand = parent.findViewById(R.id.cl_header_expand);
        int headerHeight = headerExpand.getMeasuredHeight();
        child.layout(0, headerHeight, child.getMeasuredWidth(), headerHeight + child.getMeasuredHeight());
        return super.onLayoutChild(parent, child, layoutDirection);
    }
}
