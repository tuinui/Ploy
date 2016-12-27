package com.nos.ploy.flow.ployee.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.ForegroundLinearLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.flow.generic.htmltext.HtmlTextFragment;
import com.nos.ploy.flow.ployee.profile.language.SpokenLanguageChooserFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 19/12/2559.
 */

public class PloyeeSettingsFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.button_ployee_settings_logout)
    Button mButtonLogout;
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
    private long mUserId;
    private SpokenLanguageChooserFragment.OnDataChangedListener mOnChangedListener = new SpokenLanguageChooserFragment.OnDataChangedListener() {
        @Override
        public void onClickDone(ArrayList<String> datas) {

        }
    };
    private ArrayList<String> mDummyAppLanguages = new ArrayList<>();

    public static PloyeeSettingsFragment newInstance(long userId) {

        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID,userId);
        PloyeeSettingsFragment fragment = new PloyeeSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != getArguments()){
            mUserId = getArguments().getLong(KEY_USER_ID);
        }
        dummyAppLanguages();
    }

    private void dummyAppLanguages(){
        if(mDummyAppLanguages.isEmpty()){
            mDummyAppLanguages.add("French");
            mDummyAppLanguages.add("English");
            mDummyAppLanguages.add("German");
            mDummyAppLanguages.add("Spanish");
            mDummyAppLanguages.add("Arabic");
            mDummyAppLanguages.add("Russian");
            mDummyAppLanguages.add("Chinese");
            mDummyAppLanguages.add("Japanese");
            mDummyAppLanguages.add("Polish");
            mDummyAppLanguages.add("Portuguese");
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
    }

    private void initView() {
        mButtonLogout.setOnClickListener(this);
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
        if (id == mButtonLogout.getId()) {
            UserTokenManager.clearData(v.getContext());
            ActivityCompat.finishAfterTransition(getActivity());
        } else if (id == mTextViewPolicy.getId()) {
            showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.POLICY));
        } else if (id == mTextViewFaq.getId()) {
            showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.FAQ));
        } else if (id == mTextViewLegal.getId()) {
            showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.LEGAL));
        } else if (id == mTextViewTerm.getId()) {
            showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.TERM_AND_CONDITIONS));
        } else if (id == mLinearLayoutLanguageContainer.getId()) {
            showFragment(LanguageChooserFragment.newInstance(mUserId, mDummyAppLanguages, mOnChangedListener));
        }
    }

}
