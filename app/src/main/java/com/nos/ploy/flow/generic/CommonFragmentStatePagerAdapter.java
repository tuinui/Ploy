package com.nos.ploy.flow.generic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 6/1/2560.
 */

public  class CommonFragmentStatePagerAdapter extends FragmentStatePagerAdapter {


    private final List<Fragment> fragments = new ArrayList<>();

    public CommonFragmentStatePagerAdapter(FragmentManager fm, List<? extends Fragment> fragments) {
        super(fm);
        this.fragments.clear();
        this.fragments.addAll(fragments);
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

