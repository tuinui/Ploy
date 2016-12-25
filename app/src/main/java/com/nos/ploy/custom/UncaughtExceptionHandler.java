package com.nos.ploy.custom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nos.ploy.flow.generic.FirstScreenActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultUEH;
    private Context myContext;

    public UncaughtExceptionHandler(Context context) {
        myContext = context;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

    }


    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        Log.e("", "UncaughtExceptionHandler : " + stackTrace, exception);

        ////

        Intent intent = new Intent(myContext, FirstScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent myActivity = PendingIntent.getActivity(myContext, 192837, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager;
        alarmManager = (AlarmManager) myContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 15000, myActivity);
        System.exit(2);


        // re-throw critical exception further to the os (important)
        defaultUEH.uncaughtException(thread, exception);


    }
}
