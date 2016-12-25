package com.nos.ploy.flow.ployee.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.design.internal.ForegroundLinearLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nos.ploy.DrawerController;
import com.nos.ploy.R;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.custom.view.CustomViewPager;
import com.nos.ploy.flow.generic.htmltext.HtmlTextFragment;
import com.nos.ploy.flow.ployee.account.main.PloyeeAccountFragment;
import com.nos.ploy.flow.ployee.home.content.availability.PloyeeAvailabilityFragment;
import com.nos.ploy.flow.ployee.home.content.service.list.PloyeeServiceListFragment;
import com.nos.ploy.flow.ployee.profile.PloyeeProfileActivity;
import com.nos.ploy.flow.ployee.settings.PloyeeSettingsFragment;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.PopupMenuUtils;

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
    @BindView(R.id.imageview_main_footer_more)
    ImageView mImageViewMore;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
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
    @BindView(R.id.imageview_main_drawer_profile)
    ImageView mImageViewProfile;
    @BindView(R.id.textview_main_drawer_username)
    TextView mTextViewUsername;
    @BindView(R.id.recyclerview_main_drawer_menu)
    RecyclerView mRecyclerViewDrawer;
    @BindView(R.id.viewpager_ployee_home_content_container)
    CustomViewPager mViewPager;
    @BindDrawable(R.drawable.selector_drawable_calendar_gray_blue)
    Drawable mDrawableAvailabilityGray;
    @BindDrawable(R.drawable.selector_drawable_business_gray_blue)
    Drawable mDrawableServiceListGray;
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
    private PloyeeHomePagerAdapter mPagerAdapter;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SERVICE_LIST, AVAILABLITY})
    public @interface BottomMenu {
    }

    private static final int SERVICE_LIST = 1;
    private static final int AVAILABLITY = 2;

    private SearchView mSearchView;
    private
    @BottomMenu
    int mCurrentMenu = SERVICE_LIST;

    private PloyeeServiceListFragment mListFragment;
    private PloyeeAvailabilityFragment mAvailabilityFragment;
    //    private PloyeeProfileActivity mProfileFragment = PloyeeProfileActivity.newInstance();

    private Action1<List<ProfileImageGson.Data>> mOnLoadProfileFinish = new Action1<List<ProfileImageGson.Data>>() {
        @Override
        public void call(List<ProfileImageGson.Data> datas) {
            if (null != datas && !datas.isEmpty()) {
                final ProfileImageGson.Data data = datas.get(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(PloyeeHomeActivity.this).load(data.getImagePath()).into(mImageViewProfile);
                    }
                });
            }
        }
    };

    private Action1<AccountGson.Data> mOnLoadAccountFinish = new Action1<AccountGson.Data>() {
        @Override
        public void call(final AccountGson.Data data) {
            if (null != data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextViewUsername.setText(data.getName());
                    }
                });
            }
        }
    };
    private DrawerController.OnMenuItemSelectedListener mOnMenuItemSelectedListener = new DrawerController.OnMenuItemSelectedListener() {
        @Override
        public void onMenuItemSelected(@DrawerController.Menu int menu) {
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
                    showFragment(PloyeeSettingsFragment.newInstance());
                    break;
                case DrawerController.WHAT_IS_PLOYEE:
                    showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.WHAT_IS_PLOYEE));
                    break;
                case DrawerController.WHAT_IS_PLOYER:
                    showFragment(HtmlTextFragment.newInstance(HtmlTextFragment.WHAT_IS_PLOYER));
                    break;
            }
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
        DrawerController.initDrawer(this, mDrawerLayout, mRecyclerViewDrawer, mToolbar, mImageViewMore, mOnMenuItemSelectedListener);
        initFooter();
        initToolbar();
    }

    private void initView() {
        mViewPager.setPagingEnabled(false);
        mLinearLayoutHeaderContainer.setOnClickListener(this);
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

        mPagerAdapter = new PloyeeHomePagerAdapter(getSupportFragmentManager(), mContentFragments);
        mViewPager.setAdapter(mPagerAdapter);
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void initToolbar() {
        enableBackButton(mToolbar);
        mToolbar.setTitle(LPloyee);
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
        }
    }

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

    private void onClickDoneAvailability() {
        if (mAvailabilityFragment != null) {
            mAvailabilityFragment.onClickDone();
        }
    }

    private void onClickBottomMenu(@BottomMenu int menu) {
        if (menu == AVAILABLITY) {
            mToolbar.setTitle(LAvailability);
            mSearchView.setVisibility(View.GONE);
            mViewPager.setCurrentItem(1);
            PopupMenuUtils.clearAndInflateMenu(mToolbar, R.menu.menu_done, mAvailabilityMenuItemClickListener);
            mImageViewFooterServiceList.setActivated(false);
            mImageViewFooterAvailability.setActivated(true);
        } else if (menu == SERVICE_LIST) {
            mToolbar.setTitle(LPloyee);
            PopupMenuUtils.clearMenu(mToolbar);
            mSearchView.setVisibility(View.VISIBLE);
            mViewPager.setCurrentItem(0);
            mImageViewFooterServiceList.setActivated(true);
            mImageViewFooterAvailability.setActivated(false);
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


    public static class PloyeeHomePagerAdapter extends FragmentStatePagerAdapter {


        private final List<Fragment> fragments = new ArrayList<>();

        public PloyeeHomePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments.clear();
            this.fragments.addAll(fragments);
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


}
