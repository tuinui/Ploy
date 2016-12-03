package com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Saran on 1/12/2559.
 */

public interface PloyeeServiceDetailSubServiceItemBaseViewModel {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, HEADER, ITEM, SPACE})
    @interface ViewType {
    }

    int NONE = -404;
    int HEADER = 1;
    int ITEM = 3;
    int SPACE = 4;

    @ViewType
    int getViewType();
}
