package com.nos.ploy.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.nos.ploy.R;


/**
 * Created by softbaked on 9/26/2016 AD.
 */

public class RatioImageView extends AppCompatImageView {

    private float mWidthRatio = 16f;
    private float mHeightRatio = 9f;


    public RatioImageView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView, defStyleAttr, defStyleRes);

        try {
            mWidthRatio = array.getInteger(R.styleable.RatioImageView_ratioWidth, -1);
            mHeightRatio = array.getInteger(R.styleable.RatioImageView_ratioHeight, -1);
        } finally {
            array.recycle();
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            double height = ((width * mHeightRatio) / mWidthRatio);
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
            }
            setMeasuredDimension(width, (int) Math.round(height));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
