package com.nos.ploy.flow.ployer.service;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.internal.ForegroundLinearLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nos.ploy.DrawerController;
import com.nos.ploy.R;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.flow.generic.htmltext.HtmlTextFragment;
import com.nos.ploy.flow.generic.intro.IntroductionFragment;
import com.nos.ploy.flow.generic.register.SignInSignupActivity;
import com.nos.ploy.flow.generic.settings.SettingsFragment;
import com.nos.ploy.flow.ployee.account.main.PloyeeAccountFragment;
import com.nos.ploy.flow.ployee.home.PloyeeHomeActivity;
import com.nos.ploy.flow.ployee.profile.PloyeeProfileActivity;
import com.nos.ploy.flow.ployer.person.PloyerPersonActivity;
import com.nos.ploy.utils.FragmentTransactionUtils;
import com.nos.ploy.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class PloyerHomeActivity extends BaseActivity implements SearchView.OnQueryTextListener, View.OnClickListener {
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.imageview_main_footer_logo1)
    ImageView mImageViewFooterLogo1;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.imageview_main_footer_more)
    ImageView mImageViewMore;
    @BindView(R.id.viewstub_main_appbar_searchview)
    ViewStub mStubSearchView;
    @BindView(R.id.recyclerview_main_drawer_menu)
    RecyclerView mRecyclerViewDrawer;
    @BindView(R.id.drawerlayout_ployee_home)
    DrawerLayout mDrawerLayout;
    @BindDrawable(R.drawable.ic_geniz_logo_133dp)
    Drawable mDrawableGenizLogo;
    @BindDimen(R.dimen.dp8)
    int dp16;
    @BindView(R.id.textview_main_drawer_switch_to)
    TextView mTextViewSwitchToPloyee;
    @BindView(R.id.foregroundlinearlayout_main_drawer_switch_container)
    ForegroundLinearLayout mForeGroundLinearLayoutSwitchToContainer;
    @BindView(R.id.foregroundlinearlayout_main_drawer_header_container)
    ForegroundLinearLayout mLinearLayoutHeaderContainer;
    @BindView(R.id.imageview_main_drawer_profile)
    ImageView mImageViewProfile;
    @BindView(R.id.textview_main_drawer_username)
    TextView mTextViewUsername;
    @BindView(R.id.imageview_main_drawer_switch_icon)
    ImageView mImageViewSwitchIcon;
    private SearchView mSearchView;

    private Action1<List<ProfileImageGson.Data>> mOnLoadProfileFinish = new Action1<List<ProfileImageGson.Data>>() {
        @Override
        public void call(List<ProfileImageGson.Data> datas) {
            if (null != datas && !datas.isEmpty()) {
                final ProfileImageGson.Data data = datas.get(0);
                runOnUiThread(new Action1<Context>() {
                    @Override
                    public void call(Context context) {
                        Glide.with(context).load(data.getImagePath()).into(mImageViewProfile);
                    }

                });
            }
        }
    };
    //    private PloyeeProfileActivity_Deprecated mProfileFragment = PloyeeProfileActivity_Deprecated.newInstance();
    private Action1<AccountGson.Data> mOnLoadAccountFinish = new Action1<AccountGson.Data>() {
        @Override
        public void call(final AccountGson.Data data) {
            if (null != data) {
                runOnUiThread(new Action1<Context>() {
                    @Override
                    public void call(Context context) {
                        mTextViewUsername.setText(data.getFullName());
                    }
                });
            }
        }
    };

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (null != mServiceListFragment) {
            mServiceListFragment.onQueryTextChange(newText);
        }
        return false;
    }

    private PloyerServiceListFragment mServiceListFragment = PloyerServiceListFragment.newInstance(new PloyerServiceListFragment.FragmentInteractionListener() {
        @Override
        public void onServiceClick(PloyerServicesGson.Data data) {
            goToPersonMenu(data);
        }
    });

    private DrawerController.OnMenuItemSelectedListener mOnMenuItemSelectedListener = new DrawerController.OnMenuItemSelectedListener() {
        @Override
        public void onMenuItemSelected(@DrawerController.Menu int menu) {
            switch (menu) {
                case DrawerController.NONE:
                    break;
                case DrawerController.ACCOUNT:
                    if (mUserId != -404) {
                        AccountInfoLoader.getAccountGson(PloyerHomeActivity.this, mUserId, new Action1<AccountGson.Data>() {
                            @Override
                            public void call(AccountGson.Data accountData) {
                                if (null != accountData) {
                                    showFragment(PloyeeAccountFragment.newInstance(accountData));
                                }
                            }
                        });
                    }
                    break;
                case DrawerController.SETTINGS:
                    if (null != mUserId) {
                        showFragment(SettingsFragment.newInstance(mUserId));
                    }

                    break;
                case DrawerController.WHAT_IS_PLOYEE:
                    showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.WHAT_IS_PLOYEE));
                    break;
                case DrawerController.WHAT_IS_PLOYER:
                    showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.WHAT_IS_PLOYER));
                    break;
                case DrawerController.INTRODUCTION:
                    showFragment(IntroductionFragment.newInstance(new IntroductionFragment.FragmentInteractionListener() {
                        @Override
                        public void onClickFindServices(Context context) {

                        }

                        @Override
                        public void onClickOfferServices(Context context) {
                            if (UserTokenManager.isLogin(context)) {
                                IntentUtils.startActivity(context, PloyeeHomeActivity.class);
                                finishThisActivity();
                            } else {
                                IntentUtils.startActivity(context, SignInSignupActivity.class);
                            }


                        }
                    }, true));

                    break;
            }
        }
    };

    private PloyerApi mApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ployer_home);
        ButterKnife.bind(this);
        mApi = getRetrofit().create(PloyerApi.class);

        initSearchView();

        initMenu();
        initFooter();
        initView();
        initToolbar();
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mTextViewTitle.setText(data.ployerHomeSearchService);
        mSearchView.setQueryHint(data.providerScreenSearch);
        if (UserTokenManager.isLogin(this)) {
            mTextViewSwitchToPloyee.setText(data.mainMenuOfferService);
        } else {
            mTextViewSwitchToPloyee.setText(data.sidemenuLoginLogout);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateSideBar();
    }

    private void invalidateSideBar() {
        List<DrawerController.DrawerMenuItem> menus = new ArrayList<>();
        if (UserTokenManager.isLogin(this)) {
            menus.add(DrawerController.MENU_SETTINGS);
            menus.add(DrawerController.MENU_ACCOUNT);
            menus.add(DrawerController.MENU_INTRODUCTION);
            mImageViewProfile.setImageResource(R.drawable.ic_circle_profile_120dp);
            mImageViewProfile.setColorFilter(Color.TRANSPARENT);
            mImageViewSwitchIcon.setVisibility(View.VISIBLE);
            mLinearLayoutHeaderContainer.setClickable(true);
            AccountInfoLoader.getProfileImage(this, mUserId, mOnLoadProfileFinish);
            AccountInfoLoader.getAccountGson(this, mUserId, mOnLoadAccountFinish);
        } else {
            menus.add(DrawerController.MENU_SETTINGS);
            menus.add(DrawerController.MENU_INTRODUCTION);
            mImageViewProfile.setImageResource(R.drawable.ic_geniz_logo_133dp);
            mImageViewProfile.setColorFilter(Color.WHITE);
            mImageViewSwitchIcon.setVisibility(View.GONE);
            mLinearLayoutHeaderContainer.setClickable(false);
            mTextViewUsername.setText("");
        }
        DrawerController.initDrawer(this, menus, mDrawerLayout, mRecyclerViewDrawer, mToolbar, mImageViewMore, Color.BLACK, mOnMenuItemSelectedListener);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_40dp);
    }

    private void initView() {
        mForeGroundLinearLayoutSwitchToContainer.setOnClickListener(this);
        mLinearLayoutHeaderContainer.setOnClickListener(this);
    }

    private void initToolbar() {
        mTextViewTitle.setText(R.string.Services);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_40dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initMenu() {
        FragmentTransactionUtils.addFragmentToActivity(getSupportFragmentManager(), mServiceListFragment, R.id.framelayout_content_ployer_service_list_container);
    }

    private void initSearchView() {
        mSearchView = (SearchView) mStubSearchView.inflate().findViewById(R.id.searchview_main);
        mSearchView.setOnQueryTextListener(this);
    }

    private void initFooter() {
        mImageViewFooterLogo1.setImageDrawable(mDrawableGenizLogo);
        mImageViewFooterLogo1.setPadding(dp16, dp16, dp16, dp16);
        mRecyclerViewDrawer.setBackgroundResource(android.R.color.white);
        DrawerController.initDrawer(this, DrawerController.PLOYER_MENUS, mDrawerLayout, mRecyclerViewDrawer, mToolbar, mImageViewMore, Color.BLACK, mOnMenuItemSelectedListener);
    }


    private void goToPersonMenu(PloyerServicesGson.Data data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PloyerPersonActivity.KEY_SERVICE_DATA, data);
        IntentUtils.startActivity(this, PloyerPersonActivity.class, bundle);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mForeGroundLinearLayoutSwitchToContainer.getId()) {
            if (UserTokenManager.isLogin(v.getContext())) {
                IntentUtils.startActivity(PloyerHomeActivity.this, PloyeeHomeActivity.class);
                finishThisActivity();
            } else {
                IntentUtils.startActivity(v.getContext(), SignInSignupActivity.class);
            }
        } else if (id == mLinearLayoutHeaderContainer.getId()) {
            if (UserTokenManager.isLogin(v.getContext())) {
                IntentUtils.startActivity(this, PloyeeProfileActivity.class);
            }
        }
    }
}
