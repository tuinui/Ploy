package com.nos.ploy.utils;

/**
 * Created by Saran on 7/2/2560.
 */

public class RatingBarUtils {

    private static final String TAG = "RatingBarUtils";

    public static float getRatingbarRoundingNumber(float value) {
        float number = (float) Math.floor(value);
//        Log.i(TAG, "value : " + value + " floor : " + Math.floor(value));
        float decimal = value % 1;
        if (decimal >= 0.5) {
            number += 0.5;
        }
//        Log.i(TAG, "value : " + value + " decimal : " + decimal);
        return number;
    }
}
