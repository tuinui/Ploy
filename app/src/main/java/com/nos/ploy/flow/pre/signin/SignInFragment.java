package com.nos.ploy.flow.pre.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nos.ploy.R;
import com.nos.ploy.api.authentication.AuthenticationApi;
import com.nos.ploy.api.authentication.model.PostLoginFacebookGson;
import com.nos.ploy.api.authentication.model.PostLoginGson;
import com.nos.ploy.api.authentication.model.UserTokenGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.flow.ployee.home.PloyeeHomeActivity;
import com.nos.ploy.utils.IntentUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

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
    private CallbackManager mCallbackManager;
    private static final String TAG = "SignInFragment";
    private FacebookCallback<LoginResult> mLoginResultCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            setFacebookData(loginResult);
        }

        @Override
        public void onCancel() {

        }


        @Override
        public void onError(FacebookException error) {

        }
    };

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
        mLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        mLoginButton.setFragment(this);
        mLoginButton.registerCallback(mCallbackManager, mLoginResultCallback);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = getRetrofit().create(AuthenticationApi.class);
        mCallbackManager = CallbackManager.Factory.create();
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
        mEditTextEmail.setText("nuitest@gmail.com");
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

    private void requestSigninWithFacebook(final Context context, String email, String firstName, String lastName, String fUserId) {
        showLoading();
        RetrofitCallUtils
                .with(mService.postLoginFacebook(new PostLoginFacebookGson(email, firstName, lastName, fUserId)),
                        new RetrofitCallUtils.RetrofitCallback<UserTokenGson>() {
                            @Override
                            public void onDataSuccess(UserTokenGson data) {
                                dismissLoading();
                                UserTokenManager.saveToken(context, data.getData());
                                dismiss();
                                goToPloyeeHome();
                            }

                            @Override
                            public void onDataFailure(ResponseMessage failCause) {
                                dismissLoading();
                            }
                        })
                .enqueue(context);
    }

    private void setFacebookData(final LoginResult loginResult) {

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");

                            Profile profile = Profile.getCurrentProfile();
                            String id = profile.getId();

                            requestSigninWithFacebook(getActivity(), email, firstName, lastName, id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void requestSignin(final Context context, String email, String password) {
        showLoading();
        RetrofitCallUtils.with(mService.postLogin(new PostLoginGson(email, password)), new RetrofitCallUtils.RetrofitCallback<UserTokenGson>() {
            @Override
            public void onDataSuccess(UserTokenGson data) {
                dismissLoading();
                UserTokenManager.saveToken(context, data.getData());
                goToPloyeeHome();
            }

            @Override
            public void onDataFailure(ResponseMessage failCause) {
                dismissLoading();
            }
        }).enqueue(context);
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
        finishThisActivity();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mCallbackManager) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
