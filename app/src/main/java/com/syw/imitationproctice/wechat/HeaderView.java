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
import android.view.View;
import android.widget.FrameLayout;

import com.syw.imitationproctice.R;

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
    private boolean mIsContainerShowing = false;

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

        setBackgroundColor(Color.parseColor("#e5e5e5"));
        View view = inflate(context, R.layout.header_customer, this);
//        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
//        initRecyclerView(recyclerView);
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ComponentAdapter componentAdapter = new ComponentAdapter(mContext);
        recyclerView.setAdapter(componentAdapter);
    }


    public void onHeightChange(float height) {
        mCx = getMeasuredWidth() / 2;
        mCy = (int) (height / 2);
        mPaint.setAlpha(255);
        mIsContainerShowing = false;

        if (height <= mStepSize) {
            //中间的点持续放大
            mCurrentStep = 1;
            mRadius = height / mStepSize * mMiddlePointMaxSize;
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
        } else if (height > mStepSize * 2 && height <= mStepSize * 3) {
            //小组件开始出现  三个点从上到下渐变消失
            Log.d("HeaderView", "小组件开始出现  三个点从上到下渐变消失");

            mCurrentStep = 3;
            float ratio = (height - mStepSize * 2) / mStepSize;
            mCy = (int) (mCy + mCy * ratio);
            mPaint.setAlpha((int) (255 * (1 - ratio)));

            //显示组件所在的container

        } else {
            mIsContainerShowing = true;
        }
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!mIsContainerShowing) {
            canvas.drawCircle(mCx, mCy, mRadius, mPaint);

            if (mCurrentStep > 1) {
                canvas.drawCircle(mLeftCx, mCy, mEndPointSize, mPaint);
                canvas.drawCircle(mRightCx, mCy, mEndPointSize, mPaint);
            }
        }
    }
}
