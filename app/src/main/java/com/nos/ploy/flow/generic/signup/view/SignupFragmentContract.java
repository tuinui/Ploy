package com.nos.ploy.flow.generic.signup.view;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Saran on 18/12/2559.
 */

public class SignupFragmentContract {


    public interface ViewModel {
        public static final int NONE = -404;
        public static final int ADD_MORE = 1;
        public static final int NORMAL = 2;

        @ViewType
        int getViewType();

        String getImageUri();

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({NONE, ADD_MORE, NORMAL})
        public @interface ViewType {
        }
    }
}
