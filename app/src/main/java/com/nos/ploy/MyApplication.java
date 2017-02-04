package com.nos.ploy;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.nos.ploy.cache.LanguageAppLabelManager;
import com.nos.ploy.custom.UncaughtExceptionHandler;

import io.fabric.sdk.android.Fabric;

/**
 * Created by User on 9/11/2559.
 */

public class MyApplication extends MultiDexApplication {
    // Updated your class body:
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        if(!BuildConfig.DEBUG){
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
            Fabric.with(this, new Crashlytics());
        }
        LanguageAppLabelManager.forceRefreshLanguageLabel(this, null);
    }
}
