package com.nos.ploy.flow.generic;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.AppLanguageGson;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.utils.loader.AppLanguageDataLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.cache.LanguageAppLabelManager;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.flow.generic.register.SignInSignupActivity;
import com.nos.ploy.flow.generic.settings.language.LanguageChooserFragment;
import com.nos.ploy.flow.ployee.home.PloyeeHomeActivity;
import com.nos.ploy.flow.ployer.service.PloyerHomeActivity;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.PopupMenuUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by User on 10/11/2559.
 */

public class FirstScreenActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.button_first_screen_search_services)
    Button mButtonSearchServices;
    @BindView(R.id.button_first_screen_search_jobs)
    Button mButtonSearchJobs;
    @BindView(R.id.btnSetLang)
    Button btnSetLang;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        ButterKnife.bind(this);
        mButtonSearchJobs.setOnClickListener(this);
        mButtonSearchServices.setOnClickListener(this);
        btnSetLang.setOnClickListener(this);


        String language = SharePreferenceUtils.getCurrentActiveAppLanguageCode(this);
//
//        String language  = getLanguage();
        language = language.substring(0,1).toUpperCase() + language.substring(1).toLowerCase();

        btnSetLang.setText(language);
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mButtonSearchJobs.setText(data.mainMenuOfferService);
        mButtonSearchServices.setText(data.mainMenuSearchService);

        String txtOkLabel = data.okLabel;

        String txtMsg = data.loginResetPasswordSuccess;

        if (TextUtils.isEmpty(txtMsg)) {
            txtMsg = "Your password has been reset";
        }




        Uri browsableData = getIntent().getData();
        if (browsableData != null) {

            getIntent().replaceExtras(new Bundle());
            getIntent().setAction("");
            getIntent().setData(null);
            getIntent().setFlags(0);

            String action = browsableData.getQueryParameter("action");
            String msg = browsableData.getQueryParameter("msg");

            if (!TextUtils.isEmpty(action) && !TextUtils.isEmpty(msg)) {

                if (action.equalsIgnoreCase("resetpassword") && msg.equalsIgnoreCase("resetpasswordsuccess")) {
                    PopupMenuUtils.showConfirmationAlertMenu(this, getStringCompat(R.string.app_name), txtMsg, null, txtOkLabel, null);
                }

            }
        }

    }

    private LanguageChooserFragment.OnDataChangedListener mOnChangedListener = new LanguageChooserFragment.OnDataChangedListener() {
        @Override
        public void onClickDone(final AppLanguageGson.Data data) {
            if (null == data) {
                return;
            }
            bindData(data.getCode());

        }
    };

    private void bindData(final String languageCode) {
        showLoading();
        LanguageAppLabelManager.forceRefreshLanguageLabel(this, new Action1<LanguageAppLabelGson.Data>() {
            @Override
            public void call(LanguageAppLabelGson.Data data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharePreferenceUtils.setCurrentActiveAppLanguageCode(FirstScreenActivity.this, languageCode);
                        dismissLoading();
                        invalidateLanguage();
                        AppLanguageDataLoader.getAppLanguageList(FirstScreenActivity.this, false, new Action1<ArrayList<AppLanguageGson.Data>>() {
                            @Override
                            public void call(ArrayList<AppLanguageGson.Data> datas) {
//                                btnSetLang.setText(AppLanguageDataLoader.languageCodeToLanguageName(datas, languageCode));
                            }
                        });
                    }
                });

            }
        });
    }

    private void invalidateLanguage() {
        String currentLgCode = SharePreferenceUtils.getCurrentActiveAppLanguageCode(this);
        if (!TextUtils.equals(currentLgCode, getLanguage())) {
            Locale.setDefault(new Locale(currentLgCode));
            setLanguage(Locale.getDefault().getLanguage());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mButtonSearchJobs.getId()) {
            if (UserTokenManager.isLogin(this)) {
                IntentUtils.startActivity(this, PloyeeHomeActivity.class);
            } else {
                IntentUtils.startActivity(this, SignInSignupActivity.class);
            }

        } else if (id == mButtonSearchServices.getId()) {
            IntentUtils.startActivity(this, PloyerHomeActivity.class);
        } else if (id == btnSetLang.getId()){
            AppLanguageDataLoader.getAppLanguageList(this, false, new Action1<ArrayList<AppLanguageGson.Data>>() {
                @Override
                public void call(ArrayList<AppLanguageGson.Data> datas) {
                    dismissLoading();
                    showFragment(LanguageChooserFragment.newInstance(mUserId, datas, mOnChangedListener));
                }
            });
        }
    }
}
