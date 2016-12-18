package com.nos.ploy.flow.ployee.home;

import android.os.Bundle;
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
import com.nos.ploy.flow.ployee.account.main.PloyeeAccountFragment;
import com.nos.ploy.flow.ployee.home.content.service.list.PloyeeServiceListFragment;
import com.nos.ploy.flow.ployee.home.content.availability.PloyeeAvailabilityFragment;

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
    ImageView mImageViewFooterLogo1;
    @BindView(R.id.imageview_main_footer_logo2)
    ImageView mImageViewFooterLogo2;
    @BindView(R.id.viewstub_main_appbar_searchview)
    ViewStub mViewStubSearchView;
    @BindView(R.id.drawerlayout_ployee_home)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.recyclerview_main_drawer_menu)
    RecyclerView mRecyclerViewDrawer;
    private static final int MENU_SERVICE_LIST = 0;
    private static final int MENU_AVAILABLITY = 1;
    private SearchView mSearchView;
    private int mCurrentMenu = MENU_SERVICE_LIST;

    private Long DUMMY_USER_ID = 1L;
    private PloyeeServiceListFragment mListFragment = PloyeeServiceListFragment.newInstance(DUMMY_USER_ID);
    private PloyeeAvailabilityFragment mAvailabilityFragment = PloyeeAvailabilityFragment.newInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ployee_home);
        ButterKnife.bind(this);

        mSearchView = (SearchView) mViewStubSearchView.inflate().findViewById(R.id.searchview_main);
        mSearchView.setOnQueryTextListener(this);
        DrawerController.initDrawer(this, mDrawerLayout, mRecyclerViewDrawer, mToolbar, mImageViewMore, new DrawerController.OnMenuItemSelectedListener() {
            @Override
            public void onMenuItemSelected(@DrawerController.Menu int menu) {
                switch (menu) {
                    case DrawerController.NONE:
                        break;
                    case DrawerController.SETTINGS:
                        AccountGsonLoader.getAccountGson(PloyeeHomeActivity.this, String.valueOf(DUMMY_USER_ID), new Action1<AccountGson.Data>() {
                            @Override
                            public void call(AccountGson.Data accountData) {
                                if (null != accountData) {
                                    showFragment(PloyeeAccountFragment.newInstance(accountData));
                                }
                            }
                        });
                        break;
                }
            }
        });
        initFooter();
        initToolbar(mToolbar);
        addFragmentToActivity(mListFragment, R.id.framelayout_ployee_home_content_container);
    }

    private void initFooter() {
        mImageViewFooterLogo1.setVisibility(View.VISIBLE);
        mImageViewFooterLogo2.setVisibility(View.VISIBLE);


        mImageViewFooterLogo1.setImageResource(R.drawable.ic_business_gray_50dp);
        mImageViewFooterLogo2.setImageResource(R.drawable.ic_calendar_gray_50dp);

        mImageViewFooterLogo2.setOnClickListener(this);
    }

    private void initToolbar(Toolbar toolbar) {
        enableBackButton(toolbar);
        toolbar.setTitle(R.string.Ployee);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mImageViewFooterLogo1.getId()) {
            addFragmentToActivity(mListFragment, R.id.framelayout_ployee_home_content_container);
        } else if (id == mImageViewFooterLogo2.getId()) {
            addFragmentToActivity(mAvailabilityFragment, R.id.framelayout_ployee_home_content_container);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (null != mListFragment && mCurrentMenu == MENU_SERVICE_LIST) {
            return mListFragment.onQueryTextSubmit(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (null != mListFragment && mCurrentMenu == MENU_SERVICE_LIST) {
            return mListFragment.onQueryTextChange(newText);
        }
        return false;
    }
}
