package com.syw.imitationproctice.ui.dribbble.themoment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;

import com.syw.imitationproctice.R;

/**
 * Created by Shui on 2018/6/20.
 */
public class ClickFeedbackDrawable extends Drawable {
    private static final float MAX_RADIUS = 150f;
    private static final float INSIDE_FACTOR = 0.3f;
    private static final float OUTSIDE_FACTOR = 0.5f;

    private final Paint mOutsidePaint;
    private final Paint mInsidePaint;
    private MotionEvent mE;
    private ValueAnimator mAnimator;
    private float mOutsideRadius;
    private float mInsideRadius;

    public ClickFeedbackDrawable(Context context) {
        mOutsidePaint = new Paint();
        mOutsidePaint.setColor(ContextCompat.getColor(context, R.color.gray_click_feedback));
        mInsidePaint = new Paint();
        mInsidePaint.setColor(Color.WHITE);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        if (mE != null) {
            canvas.drawCircle(mE.getRawX(), mE.getRawY(), mOutsideRadius, mOutsidePaint);
            canvas.drawCircle(mE.getRawX(), mE.getRawY(), mInsideRadius, mInsidePaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mOutsidePaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mOutsidePaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void click(@NonNull MotionEvent e) {
        reset();

        mE = e;

        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }

        mAnimator = ValueAnimator.ofFloat(0, 1f);
        mAnimator.setDuration(400);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                if (progress < OUTSIDE_FACTOR) {
                    mOutsideRadius = progress / OUTSIDE_FACTOR * MAX_RADIUS;
                } else {
                    mOutsideRadius = MAX_RADIUS;
                }

                if (progress > INSIDE_FACTOR) {
                    mInsideRadius = (progress - INSIDE_FACTOR) / (1f - INSIDE_FACTOR) * MAX_RADIUS;
                }
                invalidateSelf();
            }
        });
        mAnimator.start();

    }

    private void reset() {
        mOutsideRadius = 0f;
        mInsideRadius = 0f;
    }
}
