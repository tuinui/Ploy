package com.nos.ploy.flow.ployee.account.phone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hbb20.CountryCodePicker;
import com.nos.ploy.R;
import com.nos.ploy.base.BaseFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 5/12/2559.
 */

public class PloyeeAccountPhoneFragment extends BaseFragment {
    @BindView(R.id.countrycodepicker_ployee_account_phone)
    CountryCodePicker mCcpPhone;
    @BindView(R.id.edittext_ployee_account_phone_number)
    MaterialEditText mEditTextPhoneNumber;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindString(R.string.Phone_number)
    String LPhone_number;
    private FragmentInteractionListener listener;

    public static PloyeeAccountPhoneFragment newInstance(FragmentInteractionListener listener) {

        Bundle args = new Bundle();

        PloyeeAccountPhoneFragment fragment = new PloyeeAccountPhoneFragment();
        fragment.setListener(listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_account_phone, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setTitle(LPhone_number);
        enableBackButton(mToolbar);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_done_item_done) {
                    dismissWithData();
                }
                return false;
            }
        });
    }

    private void dismissWithData() {
        getListener().onConfirmData(gatheredData());
        dismiss();
    }

    private String gatheredData() {
        return mCcpPhone.getSelectedCountryCode() + extractString(mEditTextPhoneNumber);
    }

    public void setListener(FragmentInteractionListener listener) {
        this.listener = listener;
    }

    public FragmentInteractionListener getListener() {
        return listener;
    }


    public static interface FragmentInteractionListener {
        public void onConfirmData(String phoneNumber);
    }
}
