package com.nos.ploy.flow.pre;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 10/11/2559.
 */

public class SigninSignupActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.button_signup_signup)
    Button mButtonSignup;
    @BindView(R.id.button_signup_signin)
    Button mButtonSignin;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        initView();
        initToolbar(mToolbar);
    }

    private void initToolbar(Toolbar toolbar) {
        enableBackButton(toolbar);
        toolbar.setTitle(R.string.Ploy);
    }

    private void initView() {
        mButtonSignin.setOnClickListener(this);
        mButtonSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mButtonSignin.getId()) {
            showFragment(SignInFragment.newInstance());
        } else if (id == mButtonSignup.getId()) {
            showFragment(SignupFragment.newInstance());
        }
    }
}
