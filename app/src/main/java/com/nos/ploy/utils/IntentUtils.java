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

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

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


    public static void makeACall(final Context context, final String numberToCall) {
        if (!TextUtils.isEmpty(numberToCall) && null != context) {
            final Uri number = Uri.parse("tel:" + numberToCall);
//            Intent callIntent = new Intent(Intent.ACTION_CALL, number);
//            context.startActivity(callIntent);
            List<PopupMenuUtils.MenuVM> menus = new ArrayList<>();
            menus.add(new PopupMenuUtils.MenuVM<>("Call", "Call", new Action1<String>() {
                @Override
                public void call(String s) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL, number);
                    context.startActivity(callIntent);
                }
            }));

            menus.add(new PopupMenuUtils.MenuVM<>("SMS", "SMS", new Action1<String>() {
                @Override
                public void call(String s) {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", numberToCall, null));
                    context.startActivity(smsIntent);
                }
            }));

            PopupMenuUtils.showDialogMenu(menus,context);


//            Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", numberToCall, null));
//            Intent chooserIntent = Intent.createChooser(callIntent, "Contact...");
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { smsIntent });
//            context.startActivity(chooserIntent);
        }
    }
    public static void startActivity(BaseFragment fragment, Class<? extends Activity> desireClass) {
        if (null != fragment && null != fragment.getActivity() && !fragment.getActivity().isFinishing()) {
            startActivity(fragment.getActivity(), desireClass);
        }
    }
}
