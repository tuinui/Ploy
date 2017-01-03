package com.nos.ploy.custom.view;

/**
 * Created by Saran on 3/1/2560.
 */

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;

public class InputFilterMinMax implements InputFilter {

    private final EditText editText;
    private int min, max;
    private static final String TAG = "InputFilterMinMax";

    public InputFilterMinMax(int min, int max, EditText editText) {
        this.min = min;
        this.max = max;
        this.editText = editText;
    }

//    public InputFilterMinMax(String min, String max) {
//        this.min = Integer.parseInt(min);
//        this.max = Integer.parseInt(max);
//        editText = null;
//    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input)) {
                return null;
            } else if (input >= max) {
                editText.setText("" + max);
            } else if (input <= min) {
                editText.setText("" + min);
            }
        } catch (NumberFormatException ignored) {
            Log.w(TAG, ignored.toString());
            editText.setText("" + max);
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
