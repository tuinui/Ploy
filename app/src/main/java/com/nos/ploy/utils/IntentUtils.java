package com.nos.ploy.utils;

import android.app.Activity;
import android.content.Intent;

import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.base.BaseSupportFragment;

/**
 * Created by User on 10/11/2559.
 */

public class IntentUtils {

    public static void startActivity(BaseActivity activity, Class<? extends Activity> desireClass) {
        if (null != activity && !activity.isFinishing() && !activity.isActivityDestroyedCompat()) {
            Intent intent = new Intent(activity, desireClass);
            activity.startActivity(intent);
        }
    }

    public static void startActivity(BaseSupportFragment fragment, Class<? extends Activity> desireClass) {
        if (null != fragment && null != fragment.getActivity() && !fragment.getActivity().isFinishing()) {
            Intent intent = new Intent(fragment.getContext(), desireClass);
            fragment.startActivity(intent);
        }
    }
}
