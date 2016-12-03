package com.nos.ploy.flow.pre;

import android.content.Context;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.nos.ploy.R;
import com.nos.ploy.api.authentication.AuthenticationApi;
import com.nos.ploy.api.authentication.model.PostLoginGson;
import com.nos.ploy.api.authentication.model.UserTokenGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.flow.ployee.home.PloyeeHomeActivity;
import com.nos.ploy.utils.IntentUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends BaseFragment implements View.OnClickListener {
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
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindString(R.string.This_field_is_required)
    String LThis_field_is_required;
    @BindString(R.string.Log_in)
    String LLogin;
    private AuthenticationApi mService;

    public static SignInFragment newInstance() {

        Bundle args = new Bundle();

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signin, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = getRetrofit().create(AuthenticationApi.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initToolbar();
        dummyData();
    }

    private void initToolbar() {
        mToolbar.setTitle(LLogin);
        enableBackButton(mToolbar);
    }

    private void dummyData() {
        mEditTextEmail.setText("nuitest2@gmail.com");
        mEditTextPassword.setText("123456");
    }

    private void initView() {
        mButtonSignin.setOnClickListener(this);
        mTextViewForgotPassword.setOnClickListener(this);
    }

    private void attempSignin() {
        String email = extractAndCheckError(mEditTextEmail);
        String password = extractAndCheckError(mEditTextPassword);
        boolean canSubmit = !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);
        if (canSubmit) {
            requestSignin(mEditTextEmail.getContext(), email, password);
        }
    }

    private void requestSignin(final Context context, String email, String password) {
        showLoading();
        mService.getLogin(new PostLoginGson(email, password))
                .enqueue(new Callback<UserTokenGson>() {
                    @Override
                    public void onResponse(Call<UserTokenGson> call, Response<UserTokenGson> response) {
                        dismissLoading();
                        if (response.isSuccessful() && null != response.body() && response.body().isSuccess()) {
                            SharePreferenceUtils.saveUserTokenGson(context, response.body());
                            goToPloyeeHome();
                        } else {
                            showToast("isNotSuccessful");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserTokenGson> call, Throwable t) {
                        dismissLoading();
                        showToast("onFailure");
                    }
                });
    }

    private String extractAndCheckError(EditText editText) {
        String result = extractString(editText);
        if (TextUtils.isEmpty(result)) {
            editText.setError(LThis_field_is_required);
            editText.requestFocus();
        } else {
            editText.setError(null);
        }
        return result;
    }

    private void goToPloyeeHome() {
        IntentUtils.startActivity(this, PloyeeHomeActivity.class);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mButtonSignin.getId()) {
            attempSignin();
        } else if (id == mTextViewForgotPassword.getId()) {
            showFragment(ForgotPasswordFragment.newInstance());
        }
    }
}
