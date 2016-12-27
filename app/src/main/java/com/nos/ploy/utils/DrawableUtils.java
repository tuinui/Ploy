package com.nos.ploy.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by Saran on 21/12/2559.
 */

public class DrawableUtils {

    public static Drawable changeDrawableColor(Drawable drawable, @ColorInt int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }
}
