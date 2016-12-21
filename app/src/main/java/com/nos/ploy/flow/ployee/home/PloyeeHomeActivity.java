package com.nos.ploy.flow.ployee.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.nos.ploy.DrawerController;
import com.nos.ploy.R;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.utils.loader.AccountGsonLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.flow.ployee.account.main.PloyeeAccountFragment;
import com.nos.ploy.flow.ployee.home.content.availability.PloyeeAvailabilityFragment;
import com.nos.ploy.flow.ployee.home.content.service.list.PloyeeServiceListFragment;
import com.nos.ploy.flow.ployee.settings.PloyeeSettingsFragment;
import com.nos.ploy.utils.DrawableUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindColor;
import butterknife.BindDrawable;
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
    @BindView(R.id.recyclerview_main_drawer_menu)
    RecyclerView mRecyclerViewDrawer;
    @BindDrawable(R.drawable.selector_drawable_calendar_gray_blue)
    Drawable mDrawableAvailabilityGray;
    @BindDrawable(R.drawable.selector_drawable_business_gray_blue)
    Drawable mDrawableServiceListGray;
    @BindColor(R.color.colorPrimary)
    @ColorInt
    int mColorBlue;
    @BindColor(android.R.color.darker_gray)
    @ColorInt
    int mColorGray;


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
    private DrawerController.OnMenuItemSelectedListener mOnMenuItemSelectedListener = new DrawerController.OnMenuItemSelectedListener() {
        @Override
        public void onMenuItemSelected(@DrawerController.Menu int menu) {
            switch (menu) {
                case DrawerController.NONE:
                    break;
                case DrawerController.ACCOUNT:
                    AccountGsonLoader.getAccountGson(PloyeeHomeActivity.this, String.valueOf(mUserId), new Action1<AccountGson.Data>() {
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

        initFragment();
        mSearchView = (SearchView) mViewStubSearchView.inflate().findViewById(R.id.searchview_main);
        mSearchView.setOnQueryTextListener(this);
        DrawerController.initDrawer(this, mDrawerLayout, mRecyclerViewDrawer, mToolbar, mImageViewMore, mOnMenuItemSelectedListener);
        initFooter();
        initToolbar(mToolbar);
        addFragmentToActivity(mListFragment, R.id.framelayout_ployee_home_content_container);
    }


    private void initFragment() {
        mListFragment = PloyeeServiceListFragment.newInstance(mUserId);
        mAvailabilityFragment = PloyeeAvailabilityFragment.newInstance(mUserId);
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


    private void initToolbar(Toolbar toolbar) {
        enableBackButton(toolbar);
        toolbar.setTitle(R.string.Ployee);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mImageViewFooterServiceList.getId()) {
            onClickBottomMenu(SERVICE_LIST);
        } else if (id == mImageViewFooterAvailability.getId()) {
            onClickBottomMenu(AVAILABLITY);
        }
    }


    private void onClickBottomMenu(@BottomMenu int menu) {
        if (menu == AVAILABLITY) {
            addFragmentToActivity(mAvailabilityFragment, R.id.framelayout_ployee_home_content_container);
            mImageViewFooterServiceList.setActivated(false);
            mImageViewFooterAvailability.setActivated(true);
        } else if (menu == SERVICE_LIST) {
            addFragmentToActivity(mListFragment, R.id.framelayout_ployee_home_content_container);
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


}
