package com.nos.ploy.utils;

import android.text.TextUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by softbaked on 9/8/16 AD.
 */
public class LanguageFormatter {


    public static CharSequence format(String unformatLanguage, String[] args, String[] values) {
        List<String> argsToUse = new ArrayList<>();
        if (unformatLanguage.contains("{{") && unformatLanguage.contains("}}")) {
            for (String arg : args) {
                argsToUse.add("{{" + arg + "}}");
            }
        } else if (unformatLanguage.contains("%{") && unformatLanguage.contains("}")) {
            for (String arg : args) {
                argsToUse.add("%{" + arg + "}");
            }
        }

        return TextUtils.replace(unformatLanguage, argsToUse.toArray(new String[argsToUse.size()]), values);
    }

    public static CharSequence format(String unformatLanguage, String arg, String value) {
        if (null == value) {
            value = "";
        }
        if (null == arg) {
            arg = "";
        }
        return format(unformatLanguage, new String[]{arg}, new String[]{value});
    }

    public static CharSequence formatDecimal(double value) {
        return String.format(Locale.getDefault(), "%.2f", value);
    }

    public static String formatPrice(double value){
        return NumberFormat.getInstance().format(value);
    }

    public static String formatLong(long value) {
        return String.format(Locale.getDefault(), "%d", value);
    }

    public static CharSequence formatDecimal(float value) {
        return String.format(Locale.getDefault(), "%.2f", value);
    }
}
