package com.nos.ploy.flow.generic.intro;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.SharePreferenceUtils;

import java.lang.reflect.Field;

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
    @BindView(R.id.image)
    ImageView image;

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

        Bundle b = getArguments();

        mIndex = b.getInt(KEY_INDEX, 1);

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
//        if (mIndex == 0) {
//            mTextViewTitle.setText(data.introductionScreenTitle1);
//            mTextViewDescription.setText(data.introductionScreenDescript1);
//        } else if (mIndex == 1) {
//            mTextViewTitle.setText(data.introductionScreenTitle2);
//            mTextViewDescription.setText(data.introductionScreenDescript2);
//        } else if (mIndex == 2) {
//            mTextViewTitle.setText(data.introductionScreenTitle3);
//            mTextViewDescription.setText(data.introductionScreenDescript3);
//        }





        switch (mIndex){
            case 1:
                mTextViewTitle.setText(data.introductionScreenTitle1);
                mTextViewDescription.setText(data.introductionScreenDescript1);

                break;
            case 2:
                mTextViewTitle.setText(data.introductionScreenTitle2);
                mTextViewDescription.setText(data.introductionScreenDescript2);

                break;
            case 3:
                mTextViewTitle.setText(data.introductionScreenTitle3);
                mTextViewDescription.setText(data.introductionScreenDescript3);

                break;
            case 4:
                mTextViewTitle.setText(data.introductionScreenTitle4);
                mTextViewDescription.setText(data.introductionScreenDescript4);

                break;
            case 5:
                mTextViewTitle.setText(data.introductionScreenTitle5);
                mTextViewDescription.setText(data.introductionScreenDescript5);

                break;
            case 6:
                mTextViewTitle.setText(data.introductionScreenTitle6);
                mTextViewDescription.setText(data.introductionScreenDescript6);

                break;
        }


        String language = SharePreferenceUtils.getCurrentActiveAppLanguageCode(getActivity());
        language = language.substring(0,1).toLowerCase() + language.substring(1).toLowerCase();

        int idImage = getResId("introduction_screen_img" + mIndex + "_" + language , "drawable");




        image.setImageResource(idImage);


    }

    public int getResId(String resName, String pResourcename) {


        String pPackageName = getActivity().getPackageName();

        try {
            return getResources().getIdentifier(resName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
