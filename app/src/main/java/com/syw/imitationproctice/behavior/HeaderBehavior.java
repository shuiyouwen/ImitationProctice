package com.syw.imitationproctice.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.syw.imitationproctice.R;

import java.lang.ref.WeakReference;

/**
 * Created by Shui on 2018/6/19.
 */
public class HeaderBehavior extends CoordinatorLayout.Behavior {
    private WeakReference<View> mDependencyViewRef;
    private int mMin;

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        View headerExpand = parent.findViewById(R.id.cl_header_expand);
        View headerShrink = parent.findViewById(R.id.fl_header_shrink);
        mMin = -(headerExpand.getMeasuredHeight() - headerShrink.getMeasuredHeight());
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency.getId() == R.id.cl_header_expand) {
            mDependencyViewRef = new WeakReference<>(dependency);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        int headerOffset = getHeaderOffset();
        headerOffset = headerOffset > 0 ? 0 : headerOffset;
        headerOffset = headerOffset < mMin ? mMin : headerOffset;
        child.setTranslationY(headerOffset);
        return true;
    }

    private int getHeaderOffset() {
        if (mDependencyViewRef != null && mDependencyViewRef.get() != null) {
            CoordinatorLayout.LayoutParams layoutParams
                    = (CoordinatorLayout.LayoutParams) mDependencyViewRef.get().getLayoutParams();
            MusicHeaderExpandBehavior behavior = (MusicHeaderExpandBehavior) layoutParams.getBehavior();
            return behavior == null ? 0 : behavior.getOffSetY();
        } else {
            return 0;
        }
    }
}
