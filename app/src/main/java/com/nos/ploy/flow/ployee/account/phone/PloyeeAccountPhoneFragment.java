package com.nos.ploy.flow.ployee.account.phone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.utils.PopupMenuUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 5/12/2559.
 */

public class PloyeeAccountPhoneFragment extends BaseFragment {
    private static final String KEY_DEFAULT_VALUE = "DEFAULT_VALUE";
    @BindView(R.id.countrycodepicker_ployee_account_phone)
    CountryCodePicker mCcpPhone;
    @BindView(R.id.edittext_ployee_account_phone_number)
    MaterialEditText mEditTextPhoneNumber;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.textview_ployee_account_phone_description)
    TextView mTextViewDescription;
    @BindView(R.id.textview_ployee_account_phone_label)
    TextView mTextViewPhoneLabel;
    @BindString(R.string.Phone_number)
    String LPhone_number;
    private FragmentInteractionListener listener;
//    private String mDefaultPhoneNumber;

    public static PloyeeAccountPhoneFragment newInstance(String defaultValue, FragmentInteractionListener listener) {

        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(defaultValue) && defaultValue.contains("+")) {
//            defaultValue = String.valueOf(TextUtils.replace(defaultValue, new String[]{"+"}, new String[]{""}));
            defaultValue = defaultValue.replaceAll("\\+", "");
        }
        args.putString(KEY_DEFAULT_VALUE, defaultValue);
        PloyeeAccountPhoneFragment fragment = new PloyeeAccountPhoneFragment();
        fragment.setListener(listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
//            mDefaultPhoneNumber = getArguments().getString(KEY_DEFAULT_VALUE, "");
        }
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
//        bindData();
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mTextViewTitle.setText(data.phoneScreenHeader);
        mTextViewDescription.setText(data.phoneScreenDescript);
        mTextViewPhoneLabel.setText(data.phoneScreenHint);
        mEditTextPhoneNumber.setHint(data.phoneScreenHint);
        PopupMenuUtils.setMenuTitle(mToolbar.getMenu(),R.id.menu_done_item_done,data.doneLabel);
    }

    private void bindData() {
//        mEditTextPhoneNumber.setText(mDefaultPhoneNumber);
    }

    private void initToolbar() {
        mTextViewTitle.setText(LPhone_number);
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
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String number =mCcpPhone.getSelectedCountryCodeWithPlus() + extractString(mEditTextPhoneNumber);
        try {
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(extractString(mEditTextPhoneNumber), mCcpPhone.getSelectedCountryNameCode());
            number = phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        return number;
    }

    public FragmentInteractionListener getListener() {
        return listener;
    }

    public void setListener(FragmentInteractionListener listener) {
        this.listener = listener;
    }

    @Override
    protected boolean isDialog() {
        return true;
    }

    public static interface FragmentInteractionListener {
        public void onConfirmData(String phoneNumber);
    }
}
