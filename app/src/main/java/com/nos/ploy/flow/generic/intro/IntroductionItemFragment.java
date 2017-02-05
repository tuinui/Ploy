package com.nos.ploy.flow.generic.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 16/1/2560.
 */

public class IntroductionItemFragment extends BaseFragment {

    @BindView(R.id.textview_introduction_item_description)
    TextView mTextViewDescription;
    @BindView(R.id.textview_introduction_item_title)
    TextView mTextViewTitle;

    private static final String KEY_INDEX = "INDEX";
    private int mIndex = 0;

    public static IntroductionItemFragment newInstance(int i) {

        Bundle args = new Bundle();
        args.putInt(KEY_INDEX, i);
        IntroductionItemFragment fragment = new IntroductionItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_introduction_item, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        if (mIndex == 0) {
            mTextViewTitle.setText(data.introductionScreenTitle1);
            mTextViewDescription.setText(data.introductionScreenDescript1);
        } else if (mIndex == 1) {
            mTextViewTitle.setText(data.introductionScreenTitle2);
            mTextViewDescription.setText(data.introductionScreenDescript2);
        } else if (mIndex == 2) {
            mTextViewTitle.setText(data.introductionScreenTitle3);
            mTextViewDescription.setText(data.introductionScreenDescript3);
        }
    }

}
