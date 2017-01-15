package com.nos.ploy.flow.ployer.service;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.nos.ploy.DrawerController;
import com.nos.ploy.R;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.flow.generic.htmltext.HtmlTextFragment;
import com.nos.ploy.flow.generic.settings.SettingsFragment;
import com.nos.ploy.flow.ployee.account.main.PloyeeAccountFragment;
import com.nos.ploy.flow.ployee.home.PloyeeHomeActivity;
import com.nos.ploy.flow.ployer.person.PloyerPersonActivity;
import com.nos.ploy.utils.FragmentTransactionUtils;
import com.nos.ploy.utils.IntentUtils;

import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class PloyerServiceListActivity extends BaseActivity implements SearchView.OnQueryTextListener {
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
    @BindDrawable(R.drawable.ic_geniz_logo_133dp)
    Drawable mDrawableGenizLogo;
    @BindDimen(R.dimen.dp8)
    int dp16;
    @BindView(R.id.textview_main_drawer_switch_to)
    TextView mTextViewSwitchToPloyee;
    private SearchView mSearchView;

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
                    AccountInfoLoader.getAccountGson(PloyerServiceListActivity.this, mUserId, new Action1<AccountGson.Data>() {
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

    private void initView() {
        mTextViewSwitchToPloyee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.startActivity(PloyerServiceListActivity.this, PloyeeHomeActivity.class);
                finishThisActivity();
            }
        });
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
        IntentUtils.startActivity(this, PloyerPersonActivity.class,bundle);
    }

}