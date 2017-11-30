package com.nos.ploy;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.cache.LanguageAppLabelManager;
import com.nos.ploy.custom.UncaughtExceptionHandler;

import io.fabric.sdk.android.Fabric;

/**
 * Created by User on 9/11/2559.
 */

public class MyApplication extends MultiDexApplication {

    static MyApplication mInstance;
    static Context context;
    private LanguageAppLabelGson.Data labelLanguage;

    public int isShowEditProfile = 0;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        MyApplication.context = getApplicationContext();


        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        if(!BuildConfig.DEBUG){
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));

        }
        Fabric.with(this, new Crashlytics());
        LanguageAppLabelManager.forceRefreshLanguageLabel(this, null);
    }


    public void setLabelLanguage(LanguageAppLabelGson.Data labelLanguage) {
        this.labelLanguage = labelLanguage;
    }

    public LanguageAppLabelGson.Data getLabelLanguage() {
        return labelLanguage;
    }
}
