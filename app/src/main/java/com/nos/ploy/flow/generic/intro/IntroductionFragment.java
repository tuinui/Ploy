package com.nos.ploy.flow.generic.intro;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.generic.CommonFragmentStatePagerAdapter;
import com.nos.ploy.utils.PopupMenuUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 16/1/2560.
 */

public class IntroductionFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.button_introduction_find_services)
    Button mButtonFindServices;
    @BindView(R.id.button_introduction_offer_services)
    Button mButtonOfferServices;
    @BindView(R.id.tablayout_introduction)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager_introduction)
    ViewPager mViewPager;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;

    private List<BaseFragment> mContentFragments = new ArrayList<>();
    private CommonFragmentStatePagerAdapter mPagerAdapter;
    private FragmentInteractionListener mListener;
    private static final String KEY_SHOW_CLOSE = "SHOW_CLOSE";
    private boolean mShowClose;

    public static IntroductionFragment newInstance(FragmentInteractionListener listener,boolean showClose) {

        Bundle args = new Bundle();
        args.putBoolean(KEY_SHOW_CLOSE,showClose);
        IntroductionFragment fragment = new IntroductionFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != getArguments()){
            mShowClose = getArguments().getBoolean(KEY_SHOW_CLOSE,true);
        }
        if (mContentFragments.isEmpty()) {
            for (int i = 1; i <= 6; i++) {
                mContentFragments.add(IntroductionItemFragment.newInstance(i));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_introduction, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initToolbar();
        initPager();
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mTextViewTitle.setText(data.introductionScreenHeader);
        mButtonOfferServices.setText(data.mainMenuOfferService);
        mButtonFindServices.setText(data.mainMenuSearchService );
        PopupMenuUtils.setMenuTitle(mToolbar.getMenu(),R.id.menuitem_close_close,data.closeLabel);
    }

    @Override
    public boolean onBackPressed() {
        return !mShowClose;
    }

    private void initToolbar() {
        if(mShowClose){
            mToolbar.inflateMenu(R.menu.menu_close);
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if(id == R.id.menuitem_close_close){
                        dismiss();
                        return true;
                    }
                    return false;
                }
            });
        }

        mTextViewTitle.setText(R.string.Introduction);
    }

    private void initPager() {
        mPagerAdapter = new CommonFragmentStatePagerAdapter(getChildFragmentManager(), mContentFragments);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
        mButtonFindServices.setOnClickListener(this);
        mButtonOfferServices.setOnClickListener(this);
    }

    public void setListener(FragmentInteractionListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonFindServices.getId()) {
            if (null != mListener) {
                mListener.onClickFindServices(v.getContext());
                dismiss();
            }
        } else if (id == mButtonOfferServices.getId()) {
            if (null != mListener) {
                mListener.onClickOfferServices(v.getContext());
                dismiss();
            }
        }
    }


    public static interface FragmentInteractionListener {
        public void onClickFindServices(Context context);

        public void onClickOfferServices(Context context);
    }
}
