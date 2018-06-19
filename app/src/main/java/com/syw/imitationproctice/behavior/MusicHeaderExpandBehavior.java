package com.syw.imitationproctice.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.syw.imitationproctice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shui on 2018/6/19.
 */
public class MusicHeaderExpandBehavior extends CoordinatorLayout.Behavior {
    private static final float HEADER_PARALLAX = 0.7f;
    private int mOffSetY;
    private int mMin;
    private List<OnShrinkProgressListener> mOnShrinkProgressListeners = new ArrayList<>();

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
            if (dy < 0 && target.canScrollVertically(-1)) {
                //向下滑动,并且滑动到顶，header消耗掉所有滑动
                return;
            }

            consumed[1] = dy;
        }
        ViewCompat.offsetTopAndBottom(child, offSet);
        mOffSetY += offSet;

        if (!mOnShrinkProgressListeners.isEmpty()) {
            for (OnShrinkProgressListener listener : mOnShrinkProgressListeners) {
                listener.onShrinkProgress((float) mOffSetY / (float) mMin);
            }
        }
    }

    public int getOffSetY() {
        return mOffSetY;
    }

    /**
     * 设置收缩监听器
     *
     * @param listener
     */
    public void addOnShrinkProgressListener(@NonNull OnShrinkProgressListener listener) {
        mOnShrinkProgressListeners.add(listener);
    }

    public void removeOnShrinkProgressListener(@NonNull OnShrinkProgressListener listener) {
        mOnShrinkProgressListeners.remove(listener);
    }

    public interface OnShrinkProgressListener {
        /**
         *
         * @param progress
         */
        void onShrinkProgress(float progress);
    }
}
