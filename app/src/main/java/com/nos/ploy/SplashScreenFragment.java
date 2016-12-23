package com.nos.ploy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nos.ploy.base.BaseSupportFragment;

/**
 * Created by User on 1/10/2559.
 */

public class SplashScreenFragment extends BaseSupportFragment {

    public static SplashScreenFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SplashScreenFragment fragment = new SplashScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_splash_screen,container,false);
    }
}
