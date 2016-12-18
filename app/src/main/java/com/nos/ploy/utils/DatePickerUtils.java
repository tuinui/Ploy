package com.nos.ploy.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import rx.functions.Action1;

/**
 * Created by Saran on 18/12/2559.
 */

public class DatePickerUtils {
    public static void chooseTime(Context context, final Action1<String> onSuccess) {
        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker tp, int i, int i1) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
                calendar.set(Calendar.MINUTE, tp.getCurrentMinute());
                calendar.clear(Calendar.SECOND); //reset seconds to zero
                String dateString = DateParseUtils.parseDate(calendar.getTime(), DateParseUtils.DATE_TIME_PATTERN_HH_mm);
                if (null != onSuccess) {
                    onSuccess.call(dateString);
                }
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true).show();

    }

    public static void chooseDate(Context context, final Action1<String> onSuccess) {
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); //reset seconds to zero
                String dateString = DateParseUtils.parseDate(calendar.getTime(), DateParseUtils.ISO8601_DATE_PATTERN_FROM_API);
                if (null != onSuccess) {
                    onSuccess.call(dateString);
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();

    }
}
