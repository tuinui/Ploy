package com.nos.ploy.flow.pre;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.nos.ploy.R;
import com.nos.ploy.api.authentication.AuthenticationApi;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.authentication.model.PostSignupGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.flow.ployee.home.PloyeeHomeActivity;
import com.nos.ploy.utils.IntentUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 11/11/2559.
 */

public class SignupFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.button_signup_detail_create_account)
    Button mButtonCreateAccount;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.edittext_signup_detail_password)
    MaterialEditText mEditTextPassword;
    @BindView(R.id.edittext_signup_detail_re_password)
    MaterialEditText mEditTextRePassword;
    @BindView(R.id.edittext_signup_detail_birthday)
    MaterialEditText mEditTextBirthDay;
    @BindView(R.id.edittext_signup_detail_firstname)
    MaterialEditText mEditTextFirstName;
    @BindView(R.id.edittext_signup_detail_lastname)
    MaterialEditText mEditTextLastName;
    @BindView(R.id.edittext_signup_detail_email)
    MaterialEditText mEditTextEmail;
    @BindString(R.string.This_field_is_required)
    String LThis_field_is_required;
    @BindString(R.string.Password_does_not_match_the_confirm_password)
    String LPassword_does_not_match_the_confirm_password;


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
        dummyData();
    }

    private void dummyData(){
        mEditTextEmail.setText("nuitest2@gmail.com");
        mEditTextFirstName.setText("nuifirst");
        mEditTextLastName.setText("nuilast");
        mEditTextPassword.setText("123456");
        mEditTextRePassword.setText("123456");
        mEditTextBirthDay.setText("2016-02-02");
    }

    private void initView() {
        mButtonCreateAccount.setOnClickListener(this);
    }

    private void initToolbar(Toolbar toolbar) {
        enableBackButton(toolbar);
        toolbar.setTitle(R.string.Sign_up);
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

    private void attempSubmit() {

        mEditTextBirthDay.setError(null);
        mEditTextEmail.setError(null);
        mEditTextFirstName.setError(null);
        mEditTextLastName.setError(null);
        mEditTextPassword.setError(null);
        mEditTextRePassword.setError(null);

        String birthDay = extractAndCheckError(mEditTextBirthDay);
        String email = extractAndCheckError(mEditTextEmail);
        String firstName = extractAndCheckError(mEditTextFirstName);
        String lastName = extractAndCheckError(mEditTextLastName);
        String password = extractAndCheckError(mEditTextPassword);
        String rePassword = extractAndCheckError(mEditTextRePassword);
        boolean canSubmit = (!TextUtils.isEmpty(birthDay) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(rePassword));

        if (canSubmit) {
            if (TextUtils.equals(password, rePassword)) {
                PostSignupGson data = new PostSignupGson(birthDay, email, firstName, lastName, password);
                requestPostSignup(data, mEditTextPassword.getContext());
                mEditTextPassword.setError(null);
                mEditTextRePassword.setError(null);
            } else {
                mEditTextRePassword.setError(LPassword_does_not_match_the_confirm_password);
            }
        }
    }

    private void requestPostSignup(PostSignupGson data, final Context context) {
        showLoading();
        getRetrofit().create(AuthenticationApi.class).postSignup(data).enqueue(new Callback<AccountGson>() {
            @Override
            public void onResponse(Call<AccountGson> call, Response<AccountGson> response) {
                dismissLoading();
                if (response.isSuccessful() && null != response.body() && response.body().isSuccess()) {
                    SharePreferenceUtils.saveAccountGson(context, response.body());
                    goToPloyeeHome();
                } else {
                    showToast(response.body().getResponseMessage().getMessageDescription());
                }
            }

            @Override
            public void onFailure(Call<AccountGson> call, Throwable t) {
                dismissLoading();
                showToast("onFailure");
            }
        });
    }

    private void goToPloyeeHome() {
        IntentUtils.startActivity(SignupFragment.this, PloyeeHomeActivity.class);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mButtonCreateAccount.getId()) {
            attempSubmit();
        }
    }
}
