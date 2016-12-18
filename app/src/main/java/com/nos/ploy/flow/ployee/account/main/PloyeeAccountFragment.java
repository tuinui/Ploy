package com.nos.ploy.flow.ployee.account.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.widget.LoginButton;
import com.nos.ploy.R;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.utils.loader.AccountGsonLoader;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.account.phone.PloyeeAccountPhoneFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 3/12/2559.
 */

public class PloyeeAccountFragment extends BaseFragment implements View.OnClickListener {
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

    @BindString(R.string.Account)
    String LAccount;

    private PloyeeAccountPhoneFragment mPhoneFragment = PloyeeAccountPhoneFragment.newInstance(new PloyeeAccountPhoneFragment.FragmentInteractionListener() {
        @Override
        public void onConfirmData(String phoneNumber) {
            mEditTextPhone.setText(phoneNumber);
        }
    });
    private AccountGson.Data mData;
    private static final String KEY_ACCOUNT_GSON = "ACCOUNT_GSON";

    public static PloyeeAccountFragment newInstance(AccountGson.Data data) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_ACCOUNT_GSON, data);
        PloyeeAccountFragment fragment = new PloyeeAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_account_main, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mData = getArguments().getParcelable(KEY_ACCOUNT_GSON);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initView();
        bindData();
    }

    private void bindData() {
        if (null != mData) {
            mEditTextFirstname.setText(mData.getFirstName());
            mEditTextLastName.setText(mData.getLastName());
            mEditTextPhone.setText(mData.getPhone());
            mEditTextEmail.setText(mData.getEmail());
            mEditTextBirthday.setText(mData.getBirthDay());
        }
    }

    private void initToolbar() {
        mToolbar.setTitle(LAccount);
        enableBackButton(mToolbar);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_done_item_done) {

                }
                return false;
            }
        });
    }

    private void requestUpdateAccount() {

    }

    private void initView() {
        mEditTextPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mEditTextPhone.getId()) {
            showFragment(mPhoneFragment);
        }
    }
}
