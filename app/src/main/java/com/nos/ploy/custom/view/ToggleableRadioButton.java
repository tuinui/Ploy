package com.nos.ploy.custom.view;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

/**
 * Created by Saran on 23/12/2559.
 */

public class ToggleableRadioButton extends AppCompatRadioButton {

    public ToggleableRadioButton(Context context) {
        super(context);
    }

    public ToggleableRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleableRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Implement necessary constructors
    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}