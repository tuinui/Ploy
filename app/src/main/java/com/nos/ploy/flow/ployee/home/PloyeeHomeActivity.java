package com.nos.ploy.flow.ployee.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.design.internal.ForegroundLinearLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nos.ploy.DrawerController;
import com.nos.ploy.R;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.custom.view.CustomViewPager;
import com.nos.ploy.flow.generic.CommonFragmentStatePagerAdapter;
import com.nos.ploy.flow.generic.htmltext.HtmlTextFragment;
import com.nos.ploy.flow.generic.intro.IntroductionFragment;
import com.nos.ploy.flow.generic.settings.SettingsFragment;
import com.nos.ploy.flow.ployee.account.main.PloyeeAccountFragment;
import com.nos.ploy.flow.ployee.home.content.availability.PloyeeAvailabilityFragment;
import com.nos.ploy.flow.ployee.home.content.service.list.PloyeeServiceListFragment;
import com.nos.ploy.flow.ployee.profile.PloyeeProfileActivity;
import com.nos.ploy.flow.ployer.service.PloyerHomeActivity;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.PopupMenuUtils;
import com.nos.ploy.utils.URLHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class PloyeeHomeActivity extends BaseActivity implements View.OnClickListener, SearchView.OnQueryTextListener {
    private static final int SERVICE_LIST = 1;
    private static final int AVAILABLITY = 2;
    @BindView(R.id.imageview_main_footer_more)
    ImageView mImageViewMore;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    //    @BindView(R.id.recyclerview_ployee_home)
//    RecyclerView mRecyclerView;
    @BindView(R.id.imageview_main_footer_logo1)
    ImageView mImageViewFooterServiceList;
    @BindView(R.id.imageview_main_footer_logo2)
    ImageView mImageViewFooterAvailability;
    @BindView(R.id.viewstub_main_appbar_searchview)
    ViewStub mViewStubSearchView;
    @BindView(R.id.drawerlayout_ployee_home)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.foregroundlinearlayout_main_drawer_header_container)
    ForegroundLinearLayout mLinearLayoutHeaderContainer;
    @BindView(R.id.imageview_ployee_drawer_profile)
    ImageView mImageViewProfile;
    @BindView(R.id.textview_main_drawer_username)
    TextView mTextViewUsername;
    @BindView(R.id.recyclerview_main_drawer_menu)
    RecyclerView mRecyclerViewDrawer;
    @BindView(R.id.viewpager_ployee_home_content_container)
    CustomViewPager mViewPager;
    @BindView(R.id.textview_main_drawer_switch_to)
    TextView mTextViewSwitchToPloyer;
    @BindView(R.id.foregroundlinearlayout_main_drawer_switch_container)
    ForegroundLinearLayout mForeGroundLinearLayoutSwitchToContainer;
    @BindDrawable(R.drawable.selector_drawable_calendar_gray_blue)
    Drawable mDrawableAvailabilityGray;
    @BindDrawable(R.drawable.selector_drawable_business_gray_blue)
    Drawable mDrawableServiceListGray;

    @BindView(R.id.textview_main_drawer_editprofile)
    TextView mTextViewMainDrawerEditProfile;



    @BindString(R.string.Ployee)
    String LPloyee;
    @BindString(R.string.Availability)
    String LAvailability;
    @BindColor(R.color.colorPrimary)
    @ColorInt
    int mColorBlue;
    @BindColor(android.R.color.darker_gray)
    @ColorInt
    int mColorGray;
    private List<Fragment> mContentFragments = new ArrayList<>();
    private CommonFragmentStatePagerAdapter mPagerAdapter;
    private SearchView mSearchView;
    private
    @BottomMenu
    int mCurrentMenu = SERVICE_LIST;
    private PloyeeServiceListFragment mListFragment;
    private PloyeeAvailabilityFragment mAvailabilityFragment;
    private Action1<List<ProfileImageGson.Data>> mOnLoadProfileFinish = new Action1<List<ProfileImageGson.Data>>() {
        @Override
        public void call(List<ProfileImageGson.Data> datas) {
            if (null != datas && !datas.isEmpty()) {
                final ProfileImageGson.Data data = datas.get(0);
                runOnUiThread(new Action1<Context>() {
                    @Override
                    public void call(Context context) {

                        Glide.with(context)
                                .load(URLHelper.changURLEndpoint(data.getImagePath()))
                                .placeholder(R.drawable.ic_circle_profile_120dp)
                                .error(R.drawable.ic_circle_profile_120dp)
                                .into(mImageViewProfile);
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
    private DrawerController.OnMenuItemSelectedListener mOnMenuItemSelectedListener = new DrawerController.OnMenuItemSelectedListener() {
        @Override
        public void onMenuItemSelected(@DrawerController.Menu final int menu) {
            checkIfAvailabilityContentChanged(new Action1<Boolean>() {
                @Override
                public void call(Boolean yes) {
                    if (yes) {
                        switch (menu) {
                            case DrawerController.NONE:
                                break;
                            case DrawerController.ACCOUNT:
                                AccountInfoLoader.getAccountGson(PloyeeHomeActivity.this, mUserId, new Action1<AccountGson.Data>() {
                                    @Override
                                    public void call(AccountGson.Data accountData) {
                                        if (null != accountData) {
                                            showFragment(PloyeeAccountFragment.newInstance(accountData));
                                        }
                                    }
                                });
                                break;
                            case DrawerController.SETTINGS:
                                showFragment(SettingsFragment.newInstance(mUserId));
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
                                        IntentUtils.startActivity(context, PloyerHomeActivity.class);
                                        finishThisActivity();
                                    }

                                    @Override
                                    public void onClickOfferServices(Context context) {

                                    }
                                }, true));
                                break;
                        }

                    }
                }
            });
        }
    };
    private Toolbar.OnMenuItemClickListener mAvailabilityMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.menu_done_item_done) {
                onClickDoneAvailability();
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ployee_home);
        ButterKnife.bind(this);
        if (!UserTokenManager.isLogin(this)) {
            finishThisActivity();
        }
        initView();
        initFragment();
        mSearchView = (SearchView) mViewStubSearchView.inflate().findViewById(R.id.searchview_main);
        mSearchView.setOnQueryTextListener(this);

        AutoCompleteTextView search_text = (AutoCompleteTextView) mSearchView.findViewById(R.id.search_src_text);
        search_text.setTextColor(Color.WHITE);
        search_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.bottom_text_size));

        DrawerController.initDrawer(this, DrawerController.PLOYEE_MENUS, mDrawerLayout, mRecyclerViewDrawer, mToolbar, mImageViewMore, mOnMenuItemSelectedListener);
        initFooter();
        initToolbar();
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mSearchView.setQueryHint(data.providerScreenSearch);
        mTextViewSwitchToPloyer.setText(data.mainMenuSearchService);
        mTextViewTitle.setText(data.providerScreenHeader);

        mTextViewMainDrawerEditProfile.setText(data.profileSubtitleEditprofile);

    }

    private void initView() {
        mViewPager.setPagingEnabled(false);
        mLinearLayoutHeaderContainer.setOnClickListener(this);
        mForeGroundLinearLayoutSwitchToContainer.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        AccountInfoLoader.getProfileImage(this, mUserId, mOnLoadProfileFinish);
        AccountInfoLoader.getAccountGson(this, mUserId, mOnLoadAccountFinish);
    }

    private void initFragment() {
        mListFragment = PloyeeServiceListFragment.newInstance(mUserId);
        mAvailabilityFragment = PloyeeAvailabilityFragment.newInstance(mUserId);
        mContentFragments.clear();
        mContentFragments.add(mListFragment);
        mContentFragments.add(mAvailabilityFragment);

        mPagerAdapter = new CommonFragmentStatePagerAdapter(getSupportFragmentManager(), mContentFragments);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    mListFragment.refreshData();
                    mListFragment.getProfile();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void initFooter() {
        mImageViewFooterServiceList.setVisibility(View.VISIBLE);
        mImageViewFooterAvailability.setVisibility(View.VISIBLE);

        mImageViewFooterServiceList.setImageDrawable(mDrawableServiceListGray);
        mImageViewFooterAvailability.setImageDrawable(mDrawableAvailabilityGray);

        mImageViewFooterAvailability.setOnClickListener(this);
        mImageViewFooterServiceList.setOnClickListener(this);
        mImageViewFooterServiceList.setActivated(true);
    }

    public void openAvialability(){
        onClickBottomMenu(AVAILABLITY);

    }

    @Override
    public void onBackPressed() {
        checkIfAvailabilityContentChanged(new Action1<Boolean>() {
            @Override
            public void call(Boolean yes) {
                if (yes) {
                    PloyeeHomeActivity.super.onBackPressed();
                }
            }
        });

    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_40dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfAvailabilityContentChanged(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean yes) {
                        if (yes) {
                            finishThisActivity();
                        }

                    }
                });

            }
        });
    }

    private void checkIfAvailabilityContentChanged(Action1<Boolean> action) {
        if (null != mAvailabilityFragment && mAvailabilityFragment.isContentChanged(action)) {

        } else {
            action.call(true);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mImageViewFooterServiceList.getId()) {
            onClickBottomMenu(SERVICE_LIST);
        } else if (id == mImageViewFooterAvailability.getId()) {
            onClickBottomMenu(AVAILABLITY);
        } else if (id == mLinearLayoutHeaderContainer.getId()) {
            IntentUtils.startActivity(this, PloyeeProfileActivity.class);
        } else if (id == mForeGroundLinearLayoutSwitchToContainer.getId()) {
            if (UserTokenManager.isLogin(view.getContext())) {
                IntentUtils.startActivity(PloyeeHomeActivity.this, PloyerHomeActivity.class);
                finishThisActivity();
            } else {

            }

        }
    }

    private void onClickDoneAvailability() {
        if (mAvailabilityFragment != null) {
            mAvailabilityFragment.onClickDone();
        }
    }


    private void onClickBottomMenu(@BottomMenu int menu) {
        if (menu == AVAILABLITY) {
            mTextViewTitle.setText(mLanguageData.avaliabilityScreenHeader);
            mSearchView.setVisibility(View.GONE);
            mViewPager.setCurrentItem(1);
            PopupMenuUtils.clearAndInflateMenu(mToolbar, R.menu.menu_done, mAvailabilityMenuItemClickListener);
            PopupMenuUtils.setMenuTitle(mToolbar.getMenu(), R.id.menu_done_item_done, mLanguageData.doneLabel);
            mImageViewFooterServiceList.setActivated(false);
            mImageViewFooterAvailability.setActivated(true);
        } else if (menu == SERVICE_LIST) {
            checkIfAvailabilityContentChanged(new Action1<Boolean>() {
                @Override
                public void call(Boolean yes) {
                    if (yes) {
                        mTextViewTitle.setText(mLanguageData.providerScreenHeader);
                        PopupMenuUtils.clearMenu(mToolbar);
                        mSearchView.setVisibility(View.VISIBLE);
                        mViewPager.setCurrentItem(0);

                        mImageViewFooterServiceList.setActivated(true);
                        mImageViewFooterAvailability.setActivated(false);
                    }

                }
            });

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return null != mListFragment && mCurrentMenu == SERVICE_LIST && mListFragment.onQueryTextSubmit(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return null != mListFragment && mCurrentMenu == SERVICE_LIST && mListFragment.onQueryTextChange(newText);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SERVICE_LIST, AVAILABLITY})
    public @interface BottomMenu {
    }


}
