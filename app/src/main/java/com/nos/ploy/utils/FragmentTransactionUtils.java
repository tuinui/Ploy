package com.nos.ploy.utils;

/**
 * Created by Saran on 19/11/2559.
 */

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.base.BaseFragment;


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
        addFragmentToActivity(fragmentManager, fragment, frameId, tag, false);
    }

    /**
     * The {@code fragment} is added to the container view enqueue id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.animation_localization_activity_transition_in, R.anim.animation_localization_activity_transition_out);
        if(addToBackStack){
            transaction.add(frameId, fragment, tag);
        }else{
            transaction.replace(frameId, fragment, tag);
        }

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


    public static void showFragment(@NonNull BaseFragment parentFragment, @NonNull BaseFragment fragmentToShow) {
        showFragment(parentFragment, fragmentToShow, null);
    }


    public static void showFragment(@NonNull BaseFragment parentFragment, @NonNull BaseFragment fragmentToShow, String tag) {
        if (parentFragment.isReadyForFragmentTransaction()) {
            showFragment(parentFragment.getFragmentManager(), fragmentToShow, tag);
        }
    }

    public static void showFragment(@NonNull BaseActivity activity, @NonNull BaseFragment fragmentToShow) {
        showFragment(activity, fragmentToShow, null);

    }

    public static void showFragment(@NonNull BaseActivity activity, @NonNull BaseFragment fragmentToShow, String tag) {
        if (!activity.isFinishing() && !activity.isActivityDestroyedCompat()) {
            showFragment(activity.getSupportFragmentManager(), fragmentToShow, tag);
        }
    }

    private static void showFragment(@NonNull FragmentManager fragmentManager, @NonNull BaseFragment fragmentToShow, String tag) {
        fragmentToShow.show(fragmentManager, tag);
    }


    public static void dismissFragment(@NonNull BaseFragment baseFragment) {
        if (baseFragment.isReadyForFragmentTransaction() && null != baseFragment.getFragmentManager() && baseFragment.isResumed()) {
            baseFragment.dismiss();
        }
    }
}

