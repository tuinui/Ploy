package com.nos.ploy.flow.generic.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.ForegroundLinearLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.AppLanguageGson;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.utils.loader.AppLanguageDataLoader;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.LanguageAppLabelManager;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.flow.generic.htmltext.HtmlTextFragment;
import com.nos.ploy.flow.generic.settings.language.LanguageChooserFragment;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 19/12/2559.
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    //    @BindView(R.id.button_ployee_settings_logout)
//    Button mButtonLogout;
    @BindView(R.id.linearlayout_ployee_settings_language_container)
    ForegroundLinearLayout mLinearLayoutLanguageContainer;
    @BindView(R.id.textview_ployee_settings_privacy_policy)
    TextView mTextViewPolicy;
    @BindView(R.id.textview_ployee_settings_term_conditions)
    TextView mTextViewTerm;
    @BindView(R.id.textview_ployee_settings_legal)
    TextView mTextViewLegal;
    @BindView(R.id.textview_ployee_settings_faq)
    TextView mTextViewFaq;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.textview_ployee_settings_language_value)
    TextView mTextViewLanguageValue;
    @BindView(R.id.textview_ployee_settings_language_label)
    TextView mTextViewLanguageLabel;
    private long mUserId;
    private LanguageChooserFragment.OnDataChangedListener mOnChangedListener = new LanguageChooserFragment.OnDataChangedListener() {
        @Override
        public void onClickDone(final AppLanguageGson.Data data) {
            if (null == data) {
                return;
            }
            bindData(data.getCode());

        }
    };
//    private ArrayList<String> mDummyAppLanguages = new ArrayList<>();

    public static SettingsFragment newInstance(long userId) {

        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID, userId);
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mUserId = getArguments().getLong(KEY_USER_ID);
        }
//        dummyAppLanguages();
    }

//    private void dummyAppLanguages(){
//        if(mDummyAppLanguages.isEmpty()){
//            mDummyAppLanguages.add("French");
//            mDummyAppLanguages.add("English");
//            mDummyAppLanguages.add("German");
//            mDummyAppLanguages.add("Spanish");
//            mDummyAppLanguages.add("Arabic");
//            mDummyAppLanguages.add("Russian");
//            mDummyAppLanguages.add("Chinese");
//            mDummyAppLanguages.add("Japanese");
//            mDummyAppLanguages.add("Polish");
//            mDummyAppLanguages.add("Portuguese");
//        }
//    }

    private void bindData(final String languageCode) {
        showLoading();
        LanguageAppLabelManager.forceRefreshLanguageLabel(getContext(), new Action1<LanguageAppLabelGson.Data>() {
            @Override
            public void call(LanguageAppLabelGson.Data data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharePreferenceUtils.setCurrentActiveAppLanguageCode(mTextViewLanguageValue.getContext(), languageCode);
                        dismissLoading();
                        invalidateLanguage();
                        AppLanguageDataLoader.getAppLanguageList(mTextViewLanguageValue.getContext(), false, new Action1<ArrayList<AppLanguageGson.Data>>() {
                            @Override
                            public void call(ArrayList<AppLanguageGson.Data> datas) {
                                mTextViewLanguageValue.setText(AppLanguageDataLoader.languageCodeToLanguageName(datas, languageCode));
                            }
                        });
                    }
                });

            }
        });
    }

    private void invalidateLanguage() {
        String currentLgCode = SharePreferenceUtils.getCurrentActiveAppLanguageCode(getContext());
        if (!TextUtils.equals(currentLgCode, ((LocalizationActivity) getActivity()).getLanguage())) {
            Locale.setDefault(new Locale(currentLgCode));
            ((LocalizationActivity) getActivity()).setLanguage(Locale.getDefault().getLanguage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_settings, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initView();
        bindData(SharePreferenceUtils.getCurrentActiveAppLanguageCode(getContext()));
    }

    private void initView() {
//        mButtonLogout.setOnClickListener(this);
        mTextViewPolicy.setOnClickListener(this);
        mTextViewFaq.setOnClickListener(this);
        mTextViewLegal.setOnClickListener(this);
        mTextViewTerm.setOnClickListener(this);
        mLinearLayoutLanguageContainer.setOnClickListener(this);
    }

    private void initToolbar() {
        mTextViewTitle.setText(R.string.Settings);
        enableBackButton(mToolbar);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mTextViewPolicy.getId()) {
            showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.POLICY));
        } else if (id == mTextViewFaq.getId()) {
            showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.FAQ));
        } else if (id == mTextViewLegal.getId()) {
            showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.LEGAL));
        } else if (id == mTextViewTerm.getId()) {
            showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.TERM_AND_CONDITIONS));
        } else if (id == mLinearLayoutLanguageContainer.getId()) {
            showLoading();
            AppLanguageDataLoader.getAppLanguageList(v.getContext(), false, new Action1<ArrayList<AppLanguageGson.Data>>() {
                @Override
                public void call(ArrayList<AppLanguageGson.Data> datas) {
                    dismissLoading();
                    showFragment(LanguageChooserFragment.newInstance(mUserId, datas, mOnChangedListener));
                }
            });

        }
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mTextViewTitle.setText(data.settingHeader);
        mTextViewLanguageLabel.setText(data.settingLang);
        mTextViewPolicy.setText(data.settingPrivacy);
        mTextViewTerm.setText(data.settingTerms);
        mTextViewLegal.setText(data.settingLagal);
        mTextViewFaq.setText(data.settingFaq);
    }


}
