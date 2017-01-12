package com.nos.ploy.flow.ployer.member.localization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 12/1/2560.
 */

public class MemberLocalizationFragment extends BaseFragment {

    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textView_title)
    TextView mTextViewTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_member_localization,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
    }

    private void initToolbar() {
        enableBackButton(mToolbar);
        mTextViewTitle.setText(R.string.Localization);
    }
}
