package com.nos.ploy.flow.ployee.account.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nos.ploy.R;
import com.nos.ploy.api.account.AccountApi;
import com.nos.ploy.api.authentication.AuthenticationApi;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.authentication.model.PostFacebookMapUser;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.account.phone.PloyeeAccountPhoneFragment;
import com.nos.ploy.utils.DatePickerUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Arrays;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 3/12/2559.
 */

public class PloyeeAccountFragment extends BaseFragment implements View.OnClickListener {
    private static final String KEY_ACCOUNT_GSON = "ACCOUNT_GSON";
    @BindView(R.id.edittext_ployee_account_main_first_name)
    MaterialEditText mEditTextFirstname;
    @BindView(R.id.edittext_ployee_account_main_birthday)
    MaterialEditText mEditTextBirthday;
    @BindView(R.id.edittext_ployee_account_main_email)
    MaterialEditText mEditTextEmail;
    @BindView(R.id.edittext_ployee_account_main_last_name)
    MaterialEditText mEditTextLastName;
    @BindView(R.id.edittext_ployee_account_main_phone)
    MaterialEditText mEditTextPhone;
    @BindView(R.id.loginbutton_ployee_account_main_facebook)
    LoginButton mLoginButtonFacebook;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.swiperefreshlayout_ployee_account_main)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.edittext_ployee_account_main_password)
    MaterialEditText mEdittextPassword;
    @BindString(R.string.Account)
    String LAccount;
    private AccountGson.Data mData;
    private CallbackManager mCallbackManager;
    private AuthenticationApi mAuthenApi;
    private AccountApi mAccountApi;
    private Long mUserId;
    private Action1<AccountGson.Data> mOnLoadAccountFinish = new Action1<AccountGson.Data>() {
        @Override
        public void call(AccountGson.Data data) {
            dismissRefreshing();
            if (null != data) {
                bindData(data);
            }
        }
    };
    private RetrofitCallUtils.RetrofitCallback<Object> mCallbackUpdateData = new RetrofitCallUtils.RetrofitCallback<Object>() {
        @Override
        public void onDataSuccess(Object data) {
            dismissLoading();
            showToast("Success");
            refreshData();
        }

        @Override
        public void onDataFailure(ResponseMessage failCause) {
            dismissLoading();
        }
    };
    private FacebookCallback<LoginResult> mLoginResultCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            requestFacebookMapping(loginResult);
        }

        @Override
        public void onCancel() {

        }


        @Override
        public void onError(FacebookException error) {

        }
    };

    public static PloyeeAccountFragment newInstance(AccountGson.Data data) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_ACCOUNT_GSON, data);
        PloyeeAccountFragment fragment = new PloyeeAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mData = getArguments().getParcelable(KEY_ACCOUNT_GSON);
            mAuthenApi = getRetrofit().create(AuthenticationApi.class);
            mAccountApi = getRetrofit().create(AccountApi.class);
            mUserId = mData.getUserId();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_account_main, container, false);
        ButterKnife.bind(this, v);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initView();
        initFacebookButton();
        bindData(mData);
    }

    private void initFacebookButton() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, mLoginResultCallback);
        mLoginButtonFacebook.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        mLoginButtonFacebook.setFragment(this);
        mLoginButtonFacebook.registerCallback(mCallbackManager, mLoginResultCallback);
    }

    private void initView() {
        disableEditable(mEditTextPhone);
        disableEditable(mEditTextEmail);
        disableEditable(mEditTextBirthday);
        setRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mEditTextPhone.setOnClickListener(this);
        mEditTextBirthday.setOnClickListener(this);
        mEditTextFirstname.setFocusFraction(1f);
        mEditTextEmail.setFocusFraction(1f);
        mEditTextPhone.setFocusFraction(1f);
        mEditTextLastName.setFocusFraction(1f);
        mEditTextBirthday.setFocusFraction(1f);
        mEdittextPassword.setFocusFraction(1f);
    }

    private void initToolbar() {
        mTextViewTitle.setText(LAccount);
        enableBackButton(mToolbar);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_done_item_done) {
                    requestUpdateAccount();
                }
                return false;
            }
        });
    }

    private void bindData(AccountGson.Data data) {
        mData = data;
        if (null != mData) {
            mEditTextFirstname.setText(mData.getFirstName());
            mEditTextLastName.setText(mData.getLastName());
            mEditTextPhone.setText(mData.getPhone());
            mEditTextEmail.setText(mData.getEmail());
            mEditTextBirthday.setText(mData.getBirthDay());
            if (TextUtils.isEmpty(mData.getFbUserId())) {
                mLoginButtonFacebook.setVisibility(View.GONE);
            } else {
                mLoginButtonFacebook.setVisibility(View.VISIBLE);
            }
        }
    }

    private void requestUpdateAccount() {
        AccountGson.Data data = mData.cloneThis();
        data.setFirstName(extractString(mEditTextFirstname));
        data.setLastName(extractString(mEditTextLastName));
        data.setBirthDay(extractString(mEditTextBirthday));
//        data.setEmail(extractString(mEditTextEmail));
        data.setPhone(extractString(mEditTextPhone));
        data.setPassword(extractString(mEdittextPassword));
        showLoading();
        RetrofitCallUtils.with(mAccountApi.postUpdateAccountGson(data), mCallbackUpdateData).enqueue(getContext());
    }

    private void requestFacebookMapping(LoginResult loginResult) {
        if (null != loginResult && null != loginResult.getAccessToken() && null != loginResult.getAccessToken().getUserId()) {
            showLoading();
            RetrofitCallUtils
                    .with(mAuthenApi.postFacebookMapUser(new PostFacebookMapUser(loginResult.getAccessToken().getUserId(), mUserId))
                            , mCallbackUpdateData);
        }

    }

    private void refreshData() {
        showRefreshing();
        AccountInfoLoader.getAccountGson(getContext(), mUserId, true, mOnLoadAccountFinish);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mEditTextPhone.getId()) {
            showFragment(PloyeeAccountPhoneFragment.newInstance(extractString(mEditTextPhone), new PloyeeAccountPhoneFragment.FragmentInteractionListener() {
                @Override
                public void onConfirmData(String phoneNumber) {
                    mEditTextPhone.setText(phoneNumber);
                }
            }));
        } else if (id == mEditTextBirthday.getId()) {
            DatePickerUtils.chooseDate(mEditTextBirthday.getContext(), new Action1<String>() {
                @Override
                public void call(String s) {
                    mEditTextBirthday.setText(s);
                }
            });
        }
    }
}
