package com.nos.ploy.flow.ployee.home.content.availability.contract;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Saran on 22/11/2559.
 */

public interface AvailabilityViewModel {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE,HEADER_TIME, HEADER_DAY,ITEM})
    public @interface ViewType {
    }

    public static final int NONE = -404;
    public static final int HEADER_TIME = 1;
    public static final int HEADER_DAY = 2;
    public static final int ITEM = 3;

    @ViewType
    int getAvailibilityViewType();

}
