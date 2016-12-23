package com.nos.ploy.utils;

/**
 * Created by Saran on 19/11/2559.
 */

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.base.BaseSupportFragment;


/**
 * Created by softbaked on 10/18/2016 AD.
 */

/**
 * This provides methods to help Activities load their UI.
 */
public class FragmentTransactionUtils {

    /**
     * The {@code fragment} is added to the container view enqueue id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        addFragmentToActivity(fragmentManager, fragment, frameId, null);
    }


    /**
     * The {@code fragment} is added to the container view enqueue id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.animation_localization_activity_transition_in, R.anim.animation_localization_activity_transition_out);
        transaction.replace(frameId, fragment, tag);
        transaction.commit();
    }

    public static void showFragment(@NonNull BaseSupportFragment parentFragment, @NonNull BaseSupportFragment fragmentToShow) {
        showFragment(parentFragment, fragmentToShow, null);
    }


    public static void showFragment(@NonNull BaseSupportFragment parentFragment, @NonNull BaseSupportFragment fragmentToShow, String tag) {
        if (parentFragment.isReadyForFragmentTransaction()) {
            showFragment(parentFragment.getFragmentManager(), fragmentToShow, tag);
        }
    }

    public static void showFragment(@NonNull BaseActivity activity, @NonNull BaseSupportFragment fragmentToShow) {
        showFragment(activity, fragmentToShow, null);

    }

    public static void showFragment(@NonNull BaseActivity activity, @NonNull BaseSupportFragment fragmentToShow, String tag) {
        if (!activity.isFinishing() && !activity.isActivityDestroyedCompat()) {
            showFragment(activity.getSupportFragmentManager(), fragmentToShow, tag);
        }
    }

    private static void showFragment(@NonNull FragmentManager fragmentManager, @NonNull BaseSupportFragment fragmentToShow, String tag) {
        fragmentToShow.show(fragmentManager, tag);
    }


    public static void dismissFragment(@NonNull BaseSupportFragment baseFragment) {
        if (baseFragment.isReadyForFragmentTransaction() && null != baseFragment.getFragmentManager() && baseFragment.isResumed()) {
            baseFragment.dismiss();
        }
    }
}

