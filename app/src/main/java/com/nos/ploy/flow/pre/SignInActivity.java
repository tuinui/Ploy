package com.nos.ploy.flow.pre;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.nos.ploy.R;
import com.nos.ploy.utils.KeyHashUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.button_signup_signin)
    Button mButtonSignin;
    @BindView(R.id.loginbutton_signin)
    LoginButton mLoginButton;
    @BindView(R.id.textview_signin_forgot_password)
    TextView mTextViewForgotPassword;
    @BindView(R.id.edittext_signin_email_address)
    EditText mEditTextEmail;
    @BindView(R.id.edittext_signin_password)
    EditText mEditTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        KeyHashUtils.logKeyHash(this);
    }

    private void initView(){
        mButtonSignin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

    }
}
