package com.nos.ploy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.base.BaseFragment;

/**
 * Created by User on 10/11/2559.
 */

public class IntentUtils {

//    public static void startActivity(BaseActivity activity, Class<? extends Activity> desireClass) {
//        if (null != activity && !activity.isFinishing() && !activity.isActivityDestroyedCompat()) {
//            Intent intent = new Intent(activity, desireClass);
//            activity.startActivity(intent);
//        }
//    }

    public static void startActivity(Context context, Class<? extends Activity> desireClass) {
//        Intent intent = new Intent(context, desireClass);
        startActivity(context,desireClass,null);
    }


    public static void startActivity(Context context, Class<? extends Activity> desireClass,Bundle bundle) {
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            if (!activity.isFinishing() && !activity.isActivityDestroyedCompat()) {
//                startActivity((BaseActivity) context, desireClass);
                Intent intent = new Intent(activity, desireClass);
                if(null != bundle){
                    intent.putExtras(bundle);
                }
                activity.startActivity(intent);
            }
        } else if (null != context) {
            Intent intent = new Intent(context, desireClass);
            if(null != bundle){
                intent.putExtras(bundle);
            }

            context.startActivity(intent);
        }
    }

    public static void getDirection(Context context,LatLng latLng1,LatLng latLng2){
        if(null == context){
            return;
        }
        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+latLng1.latitude+","+latLng1.longitude+"&daddr="+latLng2.latitude+","+latLng2.longitude;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(Intent.createChooser(intent, "Select an application"));
    }




    public static void sendEmail(Context context, String email){
        if(null == context){
            return;
        }
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+email));
        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }


    public static void makeACall(Context context, String numberToCall) {
        if (!TextUtils.isEmpty(numberToCall) && null != context) {
            Uri number = Uri.parse("tel:" + numberToCall);
            Intent callIntent = new Intent(Intent.ACTION_CALL, number);
//            context.startActivity(callIntent);



            Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", numberToCall, null));
            Intent chooserIntent = Intent.createChooser(callIntent, "Contact...");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { smsIntent });
            context.startActivity(chooserIntent);
        }
    }
    public static void startActivity(BaseFragment fragment, Class<? extends Activity> desireClass) {
        if (null != fragment && null != fragment.getActivity() && !fragment.getActivity().isFinishing()) {
            startActivity(fragment.getActivity(), desireClass);
        }
    }
}
