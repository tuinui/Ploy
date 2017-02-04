package com.nos.ploy.flow.generic.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by Saran on 16/1/2560.
 */

public class IntroductionItemFragment extends BaseFragment {
    public static IntroductionItemFragment newInstance() {
        
        Bundle args = new Bundle();
        
        IntroductionItemFragment fragment = new IntroductionItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_introduction_item, container, false);
        ButterKnife.bind(this,v);
        return v;
    }
}
