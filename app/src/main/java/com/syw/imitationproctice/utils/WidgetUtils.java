package com.syw.imitationproctice.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.syw.imitationproctice.R;

/**
 * @author: Shui
 * @data: 2018/6/19
 * @description:
 */

public class WidgetUtils {
    public static void setRoundedImageView(Context context, ImageView imageView, int resource, int size) {
        Resources resources = context.getResources();
        Bitmap album = BitmapFactory.decodeResource(resources, resource);
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(resources, album);
        roundedDrawable.setCornerRadius(ScreenUtil.dip2px(context, size));
        imageView.setImageDrawable(roundedDrawable);
    }
}
