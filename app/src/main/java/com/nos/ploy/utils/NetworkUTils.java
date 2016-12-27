package com.nos.ploy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.nos.ploy.base.BaseActivity;

/**
 * Created by Saran on 28/12/2559.
 */

public class NetworkUtils {
    public static boolean isDeviceOnline(BaseActivity context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());
        if (!isOnline && context.isReady()) {
            Toast.makeText(context, " No internet Connection ", Toast.LENGTH_SHORT).show();
        }


        return isOnline;
    }
}
