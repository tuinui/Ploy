package com.nos.ploy.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.nos.ploy.R;
import com.nos.ploy.utils.ScreenUtils;

public class TextViewDrawableSize extends AppCompatTextView {

    private int mDrawableWidth;
    private int mDrawableHeight;

    public TextViewDrawableSize(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public TextViewDrawableSize(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public TextViewDrawableSize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextViewDrawableSize, defStyleAttr, defStyleRes);

        try {
            mDrawableWidth = array.getDimensionPixelSize(R.styleable.TextViewDrawableSize_compoundDrawableWidth, -1);
            mDrawableHeight = array.getDimensionPixelSize(R.styleable.TextViewDrawableSize_compoundDrawableHeight, -1);
        } finally {
            array.recycle();
        }

        if (mDrawableWidth > 0 || mDrawableHeight > 0) {
            initCompoundDrawableSize();
        }
    }
    public void setCompoundDrawableWidth(int dp){
        mDrawableWidth = ScreenUtils.convertDIPToPixels(getContext(),dp);
        if (mDrawableWidth > 0 || mDrawableHeight > 0) {
            initCompoundDrawableSize();
        }

    }

    public void setCompoundDrawableHeight(int dp){
        mDrawableHeight = ScreenUtils.convertDIPToPixels(getContext(),dp);
        if (mDrawableWidth > 0 || mDrawableHeight > 0) {
            initCompoundDrawableSize();
        }

    }

    private void initCompoundDrawableSize() {
        Drawable[] drawables = getCompoundDrawables();
        for (Drawable drawable : drawables) {
            if (drawable == null) {
                continue;
            }

            setBound(drawable);
        }
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    private void setBound(Drawable drawable) {
        if (drawable == null) {
            return;
        }

        Rect realBounds = drawable.getBounds();
        float scaleFactor = realBounds.height() / (float) realBounds.width();

        float drawableWidth = realBounds.width();
        float drawableHeight = realBounds.height();

        if (mDrawableWidth > 0) {
            // save scale factor of image
            if (drawableWidth > mDrawableWidth) {
                drawableWidth = mDrawableWidth;
                drawableHeight = drawableWidth * scaleFactor;
            }
        }
        if (mDrawableHeight > 0) {
            // save scale factor of image

            if (drawableHeight > mDrawableHeight) {
                drawableHeight = mDrawableHeight;
                drawableWidth = drawableHeight / scaleFactor;
            }
        }

        realBounds.right = realBounds.left + Math.round(drawableWidth);
        realBounds.bottom = realBounds.top + Math.round(drawableHeight);
        drawable.setBounds(realBounds);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        setBound(left);
        setBound(top);
        setBound(right);
        setBound(bottom);
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        setBound(left);
        setBound(top);
        setBound(right);
        setBound(bottom);

        super.setCompoundDrawables(left, top, right, bottom);
    }
}