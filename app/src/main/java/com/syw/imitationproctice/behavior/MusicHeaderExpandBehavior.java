package com.syw.imitationproctice.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.syw.imitationproctice.R;

/**
 * Created by Shui on 2018/6/19.
 */
public class MusicHeaderExpandBehavior extends CoordinatorLayout.Behavior {
    private static final float HEADER_PARALLAX = 0.6f;
    private int mOffSetY;
    private int mMin;

    public MusicHeaderExpandBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        View headerExpand = parent.findViewById(R.id.cl_header_expand);
        View headerShrink = parent.findViewById(R.id.fl_header_shrink);
        mMin = -(headerExpand.getMeasuredHeight() - headerShrink.getMeasuredHeight());
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        int offSet = (int) (-dy * HEADER_PARALLAX);
        if (mOffSetY + offSet < mMin) {
            offSet = mMin - mOffSetY;
        } else if (mOffSetY + offSet > 0) {
            offSet = -mOffSetY;
        } else {
            consumed[1] = dy;
        }
        ViewCompat.offsetTopAndBottom(child, offSet);
        mOffSetY += offSet;
    }

    public int getOffSetY() {
        return mOffSetY;
    }
}
