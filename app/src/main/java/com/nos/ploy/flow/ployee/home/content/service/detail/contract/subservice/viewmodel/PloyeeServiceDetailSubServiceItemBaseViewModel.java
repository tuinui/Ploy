package com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Saran on 1/12/2559.
 */

public interface PloyeeServiceDetailSubServiceItemBaseViewModel {
    int NONE = -404;
    int HEADER = 1;
    int ITEM = 3;
    int SPACE_FULL = 4;
    int SPACE_ONE_ELEMENT = 5;

    @ViewType
    int getViewType();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, HEADER, ITEM, SPACE_FULL, SPACE_ONE_ELEMENT})
    @interface ViewType {
    }
}
