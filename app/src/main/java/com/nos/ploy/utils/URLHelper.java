package com.nos.ploy.utils;

import android.text.TextUtils;

/**
 * Created by adisit on 22/10/2017 AD.
 */

public class URLHelper {

    public static String changURLEndpoint(String old){

        if (TextUtils.isEmpty(old)){
            return "";
        }

        old = old.replace("http://138.68.100.120:8080", "https://app.geeniz.com");
        old = old.replace("http://app.geeniz.com:8080", "https://app.geeniz.com");
        return old;

    }
}
