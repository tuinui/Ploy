package com.nos.ploy.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Saran on 24/12/2559.
 */

public class GoogleApiAvailabilityUtils {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "GoogleApiAvailabilityUtils";

    @SuppressLint("LongLogTag")
    public static boolean checkPlayServices(@NonNull Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
            return false;
        }
        return true;
    }
}
