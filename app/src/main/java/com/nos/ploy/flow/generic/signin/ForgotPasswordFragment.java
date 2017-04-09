package com.nos.ploy.flow.generic.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.authentication.AuthenticationApi;
import com.nos.ploy.api.authentication.model.PostForgotPasswordGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.BaseResponse;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.base.BaseFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 29/11/2559.
 */

public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.button_forgot_password_confirm)
    Button mButtonConfirm;
    @BindView(R.id.edittext_forgot_password_email_address)
    MaterialEditText mEditTextEmail;
    @BindView(R.id.edittext_forgot_password_new_password)
    MaterialEditText mEditTextPassword;
    @BindView(R.id.edittext_forgot_password_re_password)
    MaterialEditText mEditTextRePassword;
    @BindView(R.id.textview_forgot_password_description)
    TextView mTextViewDescription;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindString(R.string.Password_reset)
    String LPassword_reset;
    @BindString(R.string.This_field_is_required)
    String LThis_field_is_required;
    @BindString(R.string.Password_does_not_match_the_confirm_password)
    String LPassword_does_not_match_the_confirm_password;

    private RetrofitCallUtils.RetrofitCallback<BaseResponse> mCallbackForgotPassword = new RetrofitCallUtils.RetrofitCallback<BaseResponse>() {
        @Override
        public void onDataSuccess(BaseResponse data) {
            dismissLoading();
            showToast("Success , Please check email");
            dismiss();
        }

        @Override
        public void onDataFailure(String failCause) {
            dismissLoading();

        }
    };

    private AuthenticationApi mApi;


    public static ForgotPasswordFragment newInstance() {
        Bundle args = new Bundle();
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = getRetrofit().create(AuthenticationApi.class);
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mEditTextEmail.setHint(data.passResetScreenEmail);
        mEditTextEmail.setFloatingLabelText(data.passResetScreenEmail);
        mTextViewTitle.setText(data.passResetScreenHeader);
        mTextViewDescription.setText(data.passResetScreenDescript);
        mEditTextPassword.setHint(data.passResetScreenNewpass);
        mEditTextPassword.setFloatingLabelText(data.passResetScreenNewpass);
        mEditTextRePassword.setHint(data.passResetScreenRepass);
        mEditTextRePassword.setFloatingLabelText(data.passResetScreenNewpass);
        mButtonConfirm.setText(data.passResetScreenBtn);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    private void initToolbar() {
        mTextViewTitle.setText(LPassword_reset);
        enableBackButton(mToolbar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initView();
    }

    private void initView() {
        mButtonConfirm.setOnClickListener(this);
    }


    private void attempSubmit() {
        String email = extractAndCheckError(mEditTextEmail);
        String password = extractAndCheckError(mEditTextPassword);
        String rePassword = extractAndCheckError(mEditTextRePassword);

        boolean canSubmit = !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(rePassword);
        if (!isEmailValid(email)) {
            mEditTextEmail.setError(getString(R.string.Invalid_email_address));
            canSubmit = false;
        }
        if (mEditTextRePassword.length() < mEditTextRePassword.getMinCharacters()) {
            canSubmit = false;
            mEditTextRePassword.requestFocus();
        }

        if (mEditTextPassword.length() < mEditTextPassword.getMinCharacters()) {
            canSubmit = false;
            mEditTextPassword.requestFocus();
        }


        if (canSubmit) {
            if (TextUtils.equals(password, rePassword)) {
                mEditTextPassword.setError(null);
                mEditTextRePassword.setError(null);
                requestForgotPassword(new PostForgotPasswordGson(email, password));
            } else {
                mEditTextRePassword.setError(LPassword_does_not_match_the_confirm_password);
            }
        }
    }

    private void requestForgotPassword(PostForgotPasswordGson data) {
        showLoading();
        RetrofitCallUtils.with(mApi.postForgotPassword(data), mCallbackForgotPassword).enqueue(getContext());
    }

    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonConfirm.getId()) {
            attempSubmit();
        }
    }
}
