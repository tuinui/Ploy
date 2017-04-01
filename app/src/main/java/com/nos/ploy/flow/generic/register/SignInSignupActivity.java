package com.nos.ploy.flow.generic.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nos.ploy.R;
import com.nos.ploy.api.authentication.AuthenticationApi;
import com.nos.ploy.api.authentication.model.PostLoginFacebookGson;
import com.nos.ploy.api.authentication.model.UserTokenGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.flow.generic.intro.IntroductionFragment;
import com.nos.ploy.flow.generic.signin.SignInFragment;
import com.nos.ploy.flow.generic.signup.SignUpFragment;
import com.nos.ploy.flow.ployee.home.PloyeeHomeActivity;
import com.nos.ploy.flow.ployer.service.PloyerHomeActivity;
import com.nos.ploy.utils.IntentUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 10/11/2559.
 */

public class SignInSignupActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignInSignupActivity";
    @BindView(R.id.button_signup_signup)
    Button mButtonSignup;
    @BindView(R.id.button_signup_signin)
    Button mButtonSignin;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.loginbutton_signup)
    LoginButton mLoginButton;
    private SignInFragment mFragmentSignIn = SignInFragment.newInstance();
    private SignUpFragment mFragmentSignUp = SignUpFragment.newInstance();
    private CallbackManager mCallbackManager;
    private AuthenticationApi mService;
    private FacebookCallback<LoginResult> mLoginResultCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.i(TAG, loginResult.toString());
            setFacebookData(loginResult);
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "onCancel");
        }


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
        mService = getRetrofit().create(AuthenticationApi.class);
        initView();
        initToolbar(mToolbar);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, mLoginResultCallback);

    }

    private void initToolbar(Toolbar toolbar) {
        enableBackButton(toolbar);
        mTextViewTitle.setText(R.string.Geeniz);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (null != mFragmentSignIn) {
            mFragmentSignIn.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mTextViewTitle.setText(data.appName);
        mButtonSignin.setText(data.loginSingupScreenLogin);
        mButtonSignup.setText(data.loginSingupScreenSignup);
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
                                showIntroductionFragment();
                            }

                            @Override
                            public void onDataFailure(String failCause) {
                                UserTokenManager.clearData(SignInSignupActivity.this);
                                dismissLoading();
                            }
                        })
                .enqueue(context);
    }

    private void showIntroductionFragment() {
        showFragment(IntroductionFragment.newInstance(new IntroductionFragment.FragmentInteractionListener() {
            @Override
            public void onClickFindServices(Context context) {
                IntentUtils.startActivity(context, PloyerHomeActivity.class);
                finishThisActivity();
            }

            @Override
            public void onClickOfferServices(Context context) {
                goToPloyeeHome();

            }
        }, false));

    }

    private void goToPloyeeHome() {
        IntentUtils.startActivity(this, PloyeeHomeActivity.class);
        finishThisActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initView() {
        mButtonSignin.setOnClickListener(this);
        mButtonSignup.setOnClickListener(this);
        mLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
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
                            String id = response.getJSONObject().getString("id");
                            if (TextUtils.isEmpty(id) && null != Profile.getCurrentProfile()) {
                                id = Profile.getCurrentProfile().getId();
                            }
                            requestSigninWithFacebook(SignInSignupActivity.this, email, firstName, lastName, id);
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
