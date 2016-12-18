package com.nos.ploy.flow.pre;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nos.ploy.R;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.flow.ployee.profile.PloyeeProfileFragment;
import com.nos.ploy.flow.pre.signup.SignUpFragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    @BindView(R.id.loginbutton_signup)
    LoginButton mLoginButton;
    private SignInFragment mFragmentSignIn = SignInFragment.newInstance();
    private SignUpFragment mFragmentSignUp = SignUpFragment.newInstance();
    private CallbackManager mCallbackManager;
    private static final String TAG = "SigninSignupActivity";
    private FacebookCallback<LoginResult> mLoginResultCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.i(TAG, loginResult.toString());
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "onCancel");
        }
//


        @Override
        public void onError(FacebookException error) {
            Log.i(TAG, error.toString());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        initView();
        initToolbar(mToolbar);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, mLoginResultCallback);

    }

    private void initToolbar(Toolbar toolbar) {
        enableBackButton(toolbar);
        toolbar.setTitle(R.string.Ploy);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
        if (null != mFragmentSignIn) {
            mFragmentSignIn.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        testShowProfile();
    }

    private void testShowProfile() {
        showFragment(PloyeeProfileFragment.newInstance());

    }

    private void initView() {
        mButtonSignin.setOnClickListener(this);
        mButtonSignup.setOnClickListener(this);
        mLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mButtonSignin.getId()) {
            showFragment(mFragmentSignIn);
        } else if (id == mButtonSignup.getId()) {
            showFragment(mFragmentSignUp);
        }
    }
}
