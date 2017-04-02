package com.nos.ploy.flow.ployer.person;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.ForegroundLinearLayout;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.nos.ploy.DrawerController;
import com.nos.ploy.R;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.PostProviderFilterGson;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.custom.view.CustomViewPager;
import com.nos.ploy.flow.generic.CommonFragmentStatePagerAdapter;
import com.nos.ploy.flow.generic.htmltext.HtmlTextFragment;
import com.nos.ploy.flow.generic.intro.IntroductionFragment;
import com.nos.ploy.flow.generic.register.SignInSignupActivity;
import com.nos.ploy.flow.generic.settings.SettingsFragment;
import com.nos.ploy.flow.ployee.account.main.PloyeeAccountFragment;
import com.nos.ploy.flow.ployee.home.PloyeeHomeActivity;
import com.nos.ploy.flow.ployee.profile.PloyeeProfileActivity;
import com.nos.ploy.flow.ployer.filter.FilterFragment;
import com.nos.ploy.flow.ployer.person.list.PloyerPersonListFragment;
import com.nos.ploy.flow.ployer.person.maps.PloyerPersonMapFragment;
import com.nos.ploy.utils.GoogleApiAvailabilityUtils;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.PopupMenuUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import rx.functions.Action1;

/**
 * Created by Saran on 8/1/2560.
 * Modified by adisit on 02/04/2560
 */

public class PloyerPersonActivity extends BaseActivity implements SearchView.OnQueryTextListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private boolean isRequesting;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<String> mSuggestions = new ArrayList<>();
    private SimpleCursorAdapter mSuggestionAdapter;
    private String mSuggestionFrom = "PloyeeName";
    private ProviderUserListGson.Data mData;
    private PostProviderFilterGson mPostData;
    private long mTotal = 0;
    private boolean didPagerSet;
    private FilterFragment filterFragment;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LIST, MAPS})
    public @interface Menu {
    }

    private static final int LIST = 1;
    private static final int MAPS = 2;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.imageview_main_footer_logo1)
    ImageView mImageViewFooterLogo1;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.textview_main_appbar_subtitle)
    TextView mTextViewSubTitle;
    @BindView(R.id.imageview_main_footer_more)
    ImageView mImageViewMore;
    @BindView(R.id.viewstub_main_appbar_searchview)
    ViewStub mStubSearchView;
    @BindView(R.id.recyclerview_main_drawer_menu)
    RecyclerView mRecyclerViewDrawer;
    @BindView(R.id.drawerlayout_ployee_home)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.viewpager_ployer_person)
    CustomViewPager mViewPager;
    @BindView(R.id.imageview_main_drawer_profile)
    ImageView mImageViewProfile;
    @BindView(R.id.textview_main_drawer_username)
    TextView mTextViewUsername;
    @BindDrawable(R.drawable.ic_geniz_logo_133dp)
    Drawable mDrawableGenizLogo;
    @BindDimen(R.dimen.dp8)
    int dp16;
    @BindView(R.id.textview_main_drawer_switch_to)
    TextView mTextViewSwitchToPloyee;
    @BindView(R.id.foregroundlinearlayout_main_drawer_header_container)
    ForegroundLinearLayout mLinearLayoutHeaderContainer;
    @BindView(R.id.foregroundlinearlayout_main_drawer_switch_container)
    ForegroundLinearLayout mForeGroundLinearLayoutSwitchToContainer;
    @BindView(R.id.imageview_main_drawer_switch_icon)
    ImageView mImageViewSwitchIcon;
    public static final String KEY_SERVICE_DATA = "SERVICE_DATA";
    private SearchView mSearchView;
    private CommonFragmentStatePagerAdapter mPagerAdapter;
    private boolean isInPersonListMenu = false;
    private PloyerServicesGson.Data mParentData = null;
    private PloyerPersonListFragment.OnFragmentInteractionListener mPersonFragmentInteractionListener = new PloyerPersonListFragment.OnFragmentInteractionListener() {
        @Override
        public void onRefreshData() {
            refreshData();
        }
    };
    private PloyerPersonListFragment mPersonListFragment;
    private
    @Menu
    int mCurrentMenu = LIST;

    private Action1<List<ProfileImageGson.Data>> mOnLoadProfileFinish = new Action1<List<ProfileImageGson.Data>>() {
        @Override
        public void call(List<ProfileImageGson.Data> datas) {
            if (null != datas && !datas.isEmpty()) {
                final ProfileImageGson.Data data = datas.get(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(PloyerPersonActivity.this).load(data.getImagePath()).into(mImageViewProfile);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextViewUsername.setText(data.getFullName());
                    }
                });
            }
        }
    };
    private SearchView.OnSuggestionListener mSuggestionClickListener = new SearchView.OnSuggestionListener() {
        @Override
        public boolean onSuggestionClick(int position) {
            String suggestion = getSuggestion(position);
            if (null != mPersonMapFragment) {
                mPersonMapFragment.onSuggestionClick(suggestion);
            }
            mSearchView.setQuery(suggestion, true);
            return true;
        }

        @Override
        public boolean onSuggestionSelect(int position) {
            return true;
        }
    };


    private Toolbar.OnMenuItemClickListener mListStateOnMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.menuitem_filter_maps_filter) {
                onClickFilterMenu();
                return true;
            } else if (id == R.id.menuitem_filter_maps_maps) {
                changeMenu(MAPS);
                return true;
            }
            return false;
        }
    };

    private Toolbar.OnMenuItemClickListener mMapStateOnMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.menuitem_filter_list_filter) {
                onClickFilterMenu();
                return true;
            } else if (id == R.id.menuitem_filter_list_list) {
                changeMenu(LIST);
                return true;
            }
            return false;
        }
    };

    private DrawerController.OnMenuItemSelectedListener mOnMenuItemSelectedListener = new DrawerController.OnMenuItemSelectedListener() {
        @Override
        public void onMenuItemSelected(@DrawerController.Menu int menu) {
            switch (menu) {
                case DrawerController.NONE:
                    break;
                case DrawerController.ACCOUNT:
                    if (mUserId != -404) {
                        AccountInfoLoader.getAccountGson(PloyerPersonActivity.this, mUserId, new Action1<AccountGson.Data>() {
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
    private PloyerPersonMapFragment mPersonMapFragment;
    private List<BaseFragment> mContentFragments = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ployer_person);
        ButterKnife.bind(this);
        mApi = getRetrofit().create(PloyerApi.class);
        if (GoogleApiAvailabilityUtils.checkPlayServices(this)) {
            mGoogleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        }
        initData();
        initSearchView();
        initFooter();
        initView();
        initToolbar();
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mSearchView.setQueryHint(data.ployerHomeSearchProvider);
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
            mLinearLayoutHeaderContainer.setClickable(false);
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

    @Override
    protected void onStart() {
        super.onStart();
        if (null != mGoogleApiClient) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (null != mGoogleApiClient) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    private void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            mParentData = getIntent().getExtras().getParcelable(KEY_SERVICE_DATA);
        }
    }

    private void initView() {
        mForeGroundLinearLayoutSwitchToContainer.setOnClickListener(this);
        mLinearLayoutHeaderContainer.setOnClickListener(this);
    }

    private void initToolbar() {

        mTextViewSubTitle.setVisibility(View.VISIBLE);
        if (null != mParentData) {

            mTextViewTitle.setText(mParentData.getServiceName());
        }

        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_40dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_filter_maps);
        mToolbar.setOnMenuItemClickListener(mListStateOnMenuItemClickListener);
    }

    private void initPager() {
        didPagerSet = true;
        mViewPager.setPagingEnabled(false);
        mContentFragments.clear();
        mPersonListFragment = PloyerPersonListFragment.newInstance(mParentData, mGoogleApiClient, mPersonFragmentInteractionListener);
        mPersonMapFragment = PloyerPersonMapFragment.newInstance(mParentData, mGoogleApiClient);
        mContentFragments.add(mPersonListFragment);
        mContentFragments.add(mPersonMapFragment);
        mPagerAdapter = new CommonFragmentStatePagerAdapter(getSupportFragmentManager(), mContentFragments);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initSearchView() {
        mSearchView = (SearchView) mStubSearchView.inflate().findViewById(R.id.searchview_main);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSuggestionListener(mSuggestionClickListener);
    }

    private void initFooter() {
        mImageViewFooterLogo1.setImageDrawable(mDrawableGenizLogo);
        mImageViewFooterLogo1.setPadding(dp16, dp16, dp16, dp16);
        mRecyclerViewDrawer.setBackgroundResource(android.R.color.white);


    }

    private void changeMenu(@Menu int menu) {
        mCurrentMenu = menu;
        switch (mCurrentMenu) {
            case LIST:
                mViewPager.setCurrentItem(0);
                PopupMenuUtils.clearAndInflateMenu(mToolbar, R.menu.menu_filter_maps, mListStateOnMenuItemClickListener);
                break;
            case MAPS:
                mViewPager.setCurrentItem(1);
                PopupMenuUtils.clearAndInflateMenu(mToolbar, R.menu.menu_filter_list, mMapStateOnMenuItemClickListener);
                break;
        }
    }

    private void onClickFilterMenu() {
        filterFragment = FilterFragment.newInstance(mParentData, mPostData, mTotal, new FilterFragment.OnFilterConfirmListener() {
            @Override
            public void onFilterConfirm(PostProviderFilterGson data) {
                mPostData = data;
                refreshData();
            }

            @Override
            public void onFilterClear() {
                mPostData = null;
                refreshData();
            }
        });

        showFragment(filterFragment);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (null != mPersonListFragment) {
            mPersonListFragment.onQueryTextChange(query);
        }
        populateSuggestionAdapter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (null != mPersonListFragment) {
            mPersonListFragment.onQueryTextChange(newText);
        }
        populateSuggestionAdapter(newText);
        return false;
    }


    private void refreshData() {
        if (null == mParentData || isRequesting) {
            return;
        }
        showRefreshing();
        isRequesting = true;
        Call<ProviderUserListGson> call;
        if (mPostData == null) {
            call = mApi.getProviderList(mParentData.getId());
        } else {
            call = mApi.postGetFilteredProvider(mPostData);
        }
        RetrofitCallUtils.with(call, new RetrofitCallUtils.RetrofitCallback<ProviderUserListGson>() {
            @Override
            public void onDataSuccess(ProviderUserListGson data) {
                isRequesting = false;
                dismissRefreshing();
                bindData(data.getData());

            }

            @Override
            public void onDataFailure(String failCause) {
                isRequesting = false;
                dismissRefreshing();
            }
        }).enqueue(this);
    }

    private void bindData(final ProviderUserListGson.Data data) {
        if (null != data && null != data.getUserServiceList()) {
            if (null != data.getPagination()) {
                mTotal = data.getPagination().getTotal();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mData = data;

                    try {
                        filterFragment.updateCountProviders(mData.getPagination().getTotal());
                    } catch (Exception ignored) {}

                    mTextViewSubTitle.setText(mData.getPagination().getTotal() + " " + mLanguageData.providersLabel);
                    if (null != mPersonListFragment) {
                        mPersonListFragment.bindData(data);
                    }

                    if (null != mPersonMapFragment) {
                        mPersonMapFragment.bindData(data.getUserServiceList());
                    }

                    mSuggestions = new ArrayList<>();
                    for (ProviderUserListGson.Data.UserService service : data.getUserServiceList()) {
                        if (null != service) {
                            mSuggestions.add(service.getFullName());
                        }
                    }

                    int[] to = new int[]{android.R.id.text1};
                    String[] from = new String[]{mSuggestionFrom};
                    mSuggestionAdapter = new SimpleCursorAdapter(mViewPager.getContext(), R.layout.support_simple_spinner_dropdown_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            text.setBackgroundColor(Color.WHITE);
                            return view;
                        }


                    };
                    mSearchView.setSuggestionsAdapter(mSuggestionAdapter);
                }
            });
        }
    }


    // You must implements your logic to get data using OrmLite
    private void populateSuggestionAdapter(String query) {
        if (mSuggestionAdapter == null || query == null) {
            return;
        }
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, mSuggestionFrom});
        for (int i = 0; i < mSuggestions.size(); i++) {
            String suggestion = mSuggestions.get(i);
            if (suggestion.toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[]{i, suggestion});
        }

        mSuggestionAdapter.changeCursor(c);
    }

    private String getSuggestion(int position) {
        String result = "";
        if (null != mSearchView && null != mSearchView.getSuggestionsAdapter()) {
            Cursor cursor = (Cursor) mSearchView.getSuggestionsAdapter().getItem(
                    position);
            if (null != cursor) {
                result = cursor.getString(cursor.getColumnIndex(mSuggestionFrom));
            }
        }

        return result;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(!didPagerSet){
            initPager();
        }

        if (mData == null) {
            refreshData();
        } else {
            bindData(mData);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mForeGroundLinearLayoutSwitchToContainer.getId()) {
            if (UserTokenManager.isLogin(v.getContext())) {
                IntentUtils.startActivity(v.getContext(), PloyeeHomeActivity.class);
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
