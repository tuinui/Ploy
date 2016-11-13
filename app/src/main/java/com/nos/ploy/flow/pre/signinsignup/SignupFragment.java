package com.nos.ploy.flow.pre.signinsignup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.home.PloyeeHomeActivity;
import com.nos.ploy.utils.IntentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 11/11/2559.
 */

public class SignupFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.button_signup_detail_create_account)
    Button mButtonCreateAccount;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;

    public static SignupFragment newInstance() {
        Bundle args = new Bundle();
        SignupFragment fragment = new SignupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup_detail, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initToolbar(mToolbar);
    }

    private void initView() {
        mButtonCreateAccount.setOnClickListener(this);
    }

    private void initToolbar(Toolbar toolbar) {
        enableBackButton(toolbar);
        toolbar.setTitle(R.string.Sign_up);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mButtonCreateAccount.getId()) {
            IntentUtils.startActivity(this, PloyeeHomeActivity.class);
        }
    }
}
