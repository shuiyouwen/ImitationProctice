package com.syw.imitationproctice.wechat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.syw.imitationproctice.R;
import com.syw.imitationproctice.utils.InteractionUtils;

import static com.syw.imitationproctice.utils.ScreenUtil.dip2px;

/**
 * @author: Shui
 * @data: 2018/6/1
 * @description:
 */

public class HeaderView extends FrameLayout {

    private float mMiddlePointMaxSize;
    private float mEndPointSize;
    private float mRadius;
    private Context mContext;
    private Paint mPaint;
    private int mCx;
    private int mCy;
    private int mStepSize;
    private int mCurrentStep;
    private int mLeftCx;
    private int mRightCx;
    private boolean mIsShouldDrawPoint;//是否需要绘制3个点
    private RecyclerView mRecyclerView;
    private float mContainerHeight;
    private boolean mIsVibration;//是否震动过标识
    private boolean mShouldExpend;//标识是否需要展开
    private float mExpendHeight;//展开的高度
    private boolean mIsExpended;//是否已经展开

    public HeaderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#b7b7b7"));
        mMiddlePointMaxSize = dip2px(context, 5);
        mEndPointSize = dip2px(context, 3);
        mStepSize = dip2px(mContext, 30);
        mExpendHeight = mStepSize * 4;

        setBackgroundColor(Color.parseColor("#e5e5e5"));
        View container = inflate(context, R.layout.header_customer, this);
        mRecyclerView = container.findViewById(R.id.recycler_view);
        initContainerHeight(container);
    }

    private void initContainerHeight(final View container) {
        container.post(new Runnable() {
            @Override
            public void run() {
                container.measure(0, 0);
                mContainerHeight = container.getMeasuredHeight();
                Log.d("HeaderView", "mContainerHeight:" + mContainerHeight);

                initRecyclerView(mRecyclerView);
            }
        });
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ComponentAdapter componentAdapter = new ComponentAdapter(mContext);
        recyclerView.setAdapter(componentAdapter);

        setComponentTopMargin(-mContainerHeight);
    }

    /**
     * 设置小组件marginTop的值
     *
     * @param margin
     */
    private void setComponentTopMargin(float margin) {
        margin = margin > 0 ? 0 : margin;

        MarginLayoutParams layoutParams = (MarginLayoutParams) mRecyclerView.getLayoutParams();
        layoutParams.topMargin = (int) margin;
        mRecyclerView.setLayoutParams(layoutParams);
    }

    /**
     * 小组件消失
     */
    private void goneComponentView() {
        setComponentTopMargin(-mContainerHeight);
    }


    public void onHeightChange(float height) {
        mCx = getMeasuredWidth() / 2;
        mCy = (int) (height / 2);
        mPaint.setAlpha(255);
        mIsShouldDrawPoint = false;

        if (mIsExpended) {
            setComponentTopMargin((int) ((1f - height / mExpendHeight) * (-mContainerHeight)));
            mShouldExpend = height / mExpendHeight >= 0.5f;
            return;
        }

        if (height <= mStepSize) {
            //中间的点持续放大
            mCurrentStep = 1;
            mRadius = height / mStepSize * mMiddlePointMaxSize;

            goneComponentView();

            mShouldExpend = false;
        } else if (height > mStepSize && height <= mStepSize * 2) {
            //中间的开始缩小，并从中间开始分化出两个点
            mCurrentStep = 2;

            mLeftCx = (int) (mCx - mMiddlePointMaxSize / 2);
            mRightCx = (int) (mCx + mMiddlePointMaxSize / 2);

            int scale = (int) ((mRadius - mEndPointSize) * (height - mStepSize) / mStepSize);
            mRadius -= scale;
            Log.d("HeaderView", "scale:" + scale);
            int dx = (int) ((height - mStepSize) / mStepSize * mEndPointSize * 4);
            mLeftCx -= dx;
            mRightCx += dx;

            goneComponentView();

            mShouldExpend = false;
        } else if (height > mStepSize * 2 && height <= mStepSize * 3) {
            //小组件开始出现  三个点从上到下渐变消失
            mCurrentStep = 3;
            float ratio = (height - mStepSize * 2) / mStepSize;
            mCy = (int) (mCy + mCy * ratio);
            mPaint.setAlpha((int) (255 * (1 - ratio)));

            updateMargin(height);

            vibration();

            //用户松手时，需要展开头部
            mShouldExpend = true;
        } else {
            mIsShouldDrawPoint = true;

            updateMargin(height);
        }
        invalidate();
    }

    /**
     * 震动效果
     */
    private void vibration() {
        if (!mIsVibration) {
            InteractionUtils.vibration();
        }
        mIsVibration = true;
    }

    private void updateMargin(float height) {
        //距离走到mStepSize * 2时开始更新marginTop
        float ratio = (height - mStepSize * 2) / (mExpendHeight - mStepSize * 2);
        setComponentTopMargin((int) (-mContainerHeight * (1 - ratio)));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!mIsShouldDrawPoint && !mIsExpended) {
            canvas.drawCircle(mCx, mCy, mRadius, mPaint);

            if (mCurrentStep > 1) {
                canvas.drawCircle(mLeftCx, mCy, mEndPointSize, mPaint);
                canvas.drawCircle(mRightCx, mCy, mEndPointSize, mPaint);
            }
        }
    }

    /**
     * 是否需要展开头部
     *
     * @return
     */
    public boolean shouldExpend() {
        return mShouldExpend;
    }

    /**
     * 重置头部状态
     */
    public void reset() {
        mIsVibration = false;
        mShouldExpend = false;
        mIsExpended = false;
        goneComponentView();
    }

    /**
     * 是否已经展开
     *
     * @return
     */
    public boolean isExpended() {
        return mIsExpended;
    }

    public void setExpended(boolean isExpended) {
        mIsExpended = isExpended;
    }

    /**
     * 获取展开的高度
     */
    public float getExpendHeight() {
        return mExpendHeight;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsExpended) {
            //父控件不处理事件，把事件交给headerView中的recyclerview处理
            requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }
}
