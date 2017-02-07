package com.nos.ploy.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.widget.TextView;

/**
 * Created by Saran on 4/2/2560.
 */

public class LanguageTextUtils {

    public static String getPloyeeCountDisplay(long count, String suffix) {
        String ployeeCounts = "0 " + suffix;
        if (count == 1) {
            ployeeCounts = "1 " + suffix;
        } else if (count > 1) {
            ployeeCounts = count + " " + suffix;
        }
        return ployeeCounts;
    }

    public static TextView createLink(TextView targetTextView, String completeString,
                                      String partToClick, String partToClick2, ClickableSpan clickableAction, ClickableSpan clickableAction2) {

        SpannableString spannableString = new SpannableString(completeString);

        // make sure the String is exist, if it doesn't exist
        // it will throw IndexOutOfBoundException
        int startPosition = completeString.indexOf(partToClick);
        int endPosition = completeString.lastIndexOf(partToClick) + partToClick.length();

        int startPosition2 = completeString.indexOf(partToClick2);
        int endPosition2 = completeString.lastIndexOf(partToClick2) + partToClick2.length();

        spannableString.setSpan(clickableAction, startPosition, endPosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(clickableAction2, startPosition2, endPosition2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        targetTextView.setText(spannableString);
        targetTextView.setMovementMethod(LinkMovementMethod.getInstance());

        return targetTextView;
    }
}
