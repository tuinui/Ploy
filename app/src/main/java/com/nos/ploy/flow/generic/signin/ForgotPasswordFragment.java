package com.nos.ploy.flow.generic.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 29/11/2559.
 */

public class ForgotPasswordFragment extends BaseFragment {

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
    @BindString(R.string.Password_reset)
    String LPassword_reset;

    public static ForgotPasswordFragment newInstance() {
        Bundle args = new Bundle();
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void attempSubmit() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, v);
        initToolbar();
        return v;
    }

    private void initToolbar() {
        mToolbar.setTitle(LPassword_reset);
        enableBackButton(mToolbar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
