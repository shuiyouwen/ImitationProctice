package com.syw.imitationproctice.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.syw.imitationproctice.R;

import java.lang.ref.WeakReference;

/**
 * Created by Shui on 2018/6/19.
 */
public class MusicHeaderShrinkBehavior extends CoordinatorLayout.Behavior {

    private MusicHeaderExpandBehavior.OnShrinkProgressListener mExpandProgressListener;
    private MusicHeaderExpandBehavior mHeaderExpandBehavior;
    private WeakReference<View> mIvShrinkBgRef;
    private WeakReference<View> mTvTitleRef;

    public MusicHeaderShrinkBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        View headerExpand = parent.findViewById(R.id.cl_header_expand);
        mIvShrinkBgRef = new WeakReference<>(parent.findViewById(R.id.iv_shrink_bg));
        mTvTitleRef = new WeakReference<>(parent.findViewById(R.id.tv_shrink_title));

        if (mExpandProgressListener == null && headerExpand != null) {
            mExpandProgressListener = new MusicHeaderExpandBehavior.OnShrinkProgressListener() {
                @Override
                public void onShrinkProgress(float progress) {
                    handleProgress(progress);
                }
            };
            ViewGroup.LayoutParams layoutParams = headerExpand.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
                if (behavior instanceof MusicHeaderExpandBehavior) {
                    mHeaderExpandBehavior = (MusicHeaderExpandBehavior) behavior;
                    mHeaderExpandBehavior.addOnShrinkProgressListener(mExpandProgressListener);
                }
            }
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    private void handleProgress(float progress) {
        if (mIvShrinkBgRef.get() != null) {
            mIvShrinkBgRef.get().setAlpha(progress);
        }
        if (mTvTitleRef.get() != null) {
            mTvTitleRef.get().setAlpha(progress);
        }
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, View child, View dependency) {
        if (mExpandProgressListener != null) {
            mHeaderExpandBehavior.removeOnShrinkProgressListener(mExpandProgressListener);
        }
        super.onDependentViewRemoved(parent, child, dependency);
    }
}
