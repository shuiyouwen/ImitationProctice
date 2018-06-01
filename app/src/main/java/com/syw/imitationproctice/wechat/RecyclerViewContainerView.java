package com.syw.imitationproctice.wechat;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.syw.imitationproctice.utils.ScreenUtil;

/**
 * @author: Shui
 * @data: 2018/6/1
 * @description: recyclerView包裹view，实现一些效果
 */

public class RecyclerViewContainerView extends LinearLayout {
    private static final float PARALLAX_FACTOR = 1.8f;

    private RecyclerView mRecyclerView;
    private float mLastY;
    private HeaderView mHeaderView;
    private float mHeaderHeight;
    private Scroller mScroller;

    public RecyclerViewContainerView(Context context) {
        super(context);
        init(context);
    }

    public RecyclerViewContainerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerViewContainerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);

        mScroller = new Scroller(context);
        initHeaderView(context);
    }

    private void initHeaderView(Context context) {
        mHeaderView = new HeaderView(context);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT
                , ScreenUtil.dip2px(context, 0));
        addView(mHeaderView, 0, lp);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        getTargetRecyclerView();
    }

    private void getTargetRecyclerView() {
        if (mRecyclerView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (view instanceof RecyclerView) {
                    mRecyclerView = (RecyclerView) view;
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = ev.getRawY() - mLastY;
                Log.d("RecyclerViewContainerVi", "dy:" + dy);

                mLastY = ev.getRawY();

                if (mRecyclerView == null || mRecyclerView.getLayoutManager() == null
                        || !(mRecyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
                    return super.onInterceptTouchEvent(ev);
                }

                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                if (layoutManager.findFirstVisibleItemPosition() == 0 && dy > 0) {
                    //在顶端下拉操作
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = event.getRawY() - mLastY;
                Log.d("RecyclerViewContainerVi", "dy:" + dy);

                mLastY = event.getRawY();

                if (mRecyclerView == null || mRecyclerView.getLayoutManager() == null
                        || !(mRecyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
                    return super.onTouchEvent(event);
                }

                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    //在顶端下拉操作
                    if (mHeaderHeight >= 0) {
                        setHeaderVisibleHeight(mHeaderHeight + dy / PARALLAX_FACTOR);
                    }
                    return true;
                }

                break;
            default:
                if (mHeaderHeight > 0) {
                    mScroller.startScroll(0, (int) mHeaderHeight, 0, (int) -mHeaderHeight);
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void setHeaderVisibleHeight(float height) {
        mHeaderHeight = height < 0 ? 0 : height;
        Log.d("RecyclerViewContainerVi", "mHeaderHeight:" + mHeaderHeight);
        ViewGroup.LayoutParams layoutParams = mHeaderView.getLayoutParams();
        layoutParams.height = (int) height;
        mHeaderView.setLayoutParams(layoutParams);

        mHeaderView.onHeightChange(height);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            setHeaderVisibleHeight(mScroller.getCurrY());
        }
    }
}
