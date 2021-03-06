package com.syw.imitationproctice.ui.wechat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.syw.imitationproctice.R;
import com.syw.imitationproctice.utils.InteractionUtils;
import com.syw.imitationproctice.utils.ScreenUtil;

/**
 * @author: Shui
 * @data: 2018/6/1
 * @description: recyclerView包裹view，实现一些效果
 */
public class RecyclerViewContainerView extends LinearLayout {
    public static final float PARALLAX_FACTOR = 1.8f;
    private static final int DEVIATION = 3;//判断展开还是收缩的误差值
    private static final int VELOCITY_FACTOR = 500;//手势加速度判断参数

    private RecyclerView mRecyclerView;
    private float mLastY;
    private HeaderView mHeaderView;
    private float mHeaderHeight;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

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
        mVelocityTracker = VelocityTracker.obtain();
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

                if (isInTop()) {
                    //在顶端
                    Log.d("RecyclerViewContainerVi", "在顶端");
                    if (dy > 0) {
                        //在顶端下拉操作
                        return true;
                    }
                    if (dy < 0 && mHeaderHeight > 0) {
                        //上滑，并且headview展开了一些
                        return true;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 是否在最顶端
     *
     * @return
     */
    // TODO: 2018/6/2 缺陷，recyclerview向上滑动一点之后此方法一直返回false
    private boolean isInTop() {
        //判断是否滑动到顶部， recyclerView.canScrollVertically(-1);返回false表示不能往下滑动，即代表到顶部了；
        return !mRecyclerView.canScrollVertically(-1);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                float dy = event.getRawY() - mLastY;
                Log.d("RecyclerViewContainerVi", "dy:" + dy);

                mLastY = event.getRawY();

                if (mRecyclerView == null || mRecyclerView.getLayoutManager() == null
                        || !(mRecyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
                    return super.onTouchEvent(event);
                }

                if (isInTop()) {
                    // 在顶端
                    if (dy > 0 || (dy < 0 && mHeaderHeight > 0)) {
                        //在顶端下拉操作 或者   上滑，并且headview展开了一些
                        setHeaderVisibleHeight(mHeaderHeight + dy / PARALLAX_FACTOR);
                        return true;
                    }
                }
                break;
            default:
                if (mHeaderHeight > 0) {
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float yVelocity = mVelocityTracker.getYVelocity();
                    if (yVelocity > VELOCITY_FACTOR) {
                        //下拉
                        expendHead();
                    } else if (yVelocity < -VELOCITY_FACTOR) {
                        //上滑
                        collapseHead();
                    } else {
                        if (mHeaderView.shouldExpend()) {
                            //需要展开，展开到头部完全张开状态
                            expendHead();
                        } else {
                            //不需要展开，则恢复原样
                            collapseHead();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 收缩头部
     */
    private void collapseHead() {
        mScroller.startScroll(0, (int) mHeaderHeight, 0, (int) -mHeaderHeight);
        invalidate();
    }

    /**
     * 展开头部expend
     */
    private void expendHead() {
        mScroller.startScroll(0, (int) mHeaderHeight, 0
                , (int) (mHeaderView.getExpendHeight() - mHeaderHeight));
        invalidate();
    }

    private void setHeaderVisibleHeight(float height) {
        mHeaderHeight = height < 0 ? 0 : height;
        Log.d("RecyclerViewContainerVi", "mHeaderHeight:" + mHeaderHeight);
        ViewGroup.LayoutParams layoutParams = mHeaderView.getLayoutParams();
        layoutParams.height = (int) height;
        mHeaderView.setLayoutParams(layoutParams);

        mHeaderView.onHeightChange(height);
    }

    private int mLastCurrY = -1;
    private boolean mPlaySoundFlag;

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            setHeaderVisibleHeight(mScroller.getCurrY());

            if (mScroller.getCurrY() == mLastCurrY) {
                //滑动停止了
                if (Math.abs(mScroller.getCurrY()) < DEVIATION) {
                    //收缩完成
                    mHeaderView.reset();
                    mPlaySoundFlag = false;
                } else if (Math.abs(mScroller.getCurrY() - mHeaderView.getExpendHeight()) < DEVIATION) {
                    //展开完成
                    mHeaderView.setExpended(true);
                    if (!mPlaySoundFlag) {
                        InteractionUtils.playSound(R.raw.app_brand_pull_recent_vew_down_sound);
                    }
                    mPlaySoundFlag = true;
                }
            }
            mLastCurrY = mScroller.getCurrY();

            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
        }
        super.onDetachedFromWindow();
    }
}
