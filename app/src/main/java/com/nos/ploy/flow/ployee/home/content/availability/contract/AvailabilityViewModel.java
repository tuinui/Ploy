package com.nos.ploy.flow.ployee.home.content.availability.contract;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Saran on 22/11/2559.
 */

public interface AvailabilityViewModel {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, WEEK, NORMAL})
    @interface ViewType {
    }

    int NONE = -404;
    int WEEK = 1;
    int NORMAL = 2;

    @ViewType
    int getViewType();

}
