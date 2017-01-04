package com.nos.ploy.flow.ployer.home;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.nos.ploy.DrawerController;
import com.nos.ploy.R;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.flow.generic.htmltext.HtmlTextFragment;
import com.nos.ploy.flow.ployee.account.main.PloyeeAccountFragment;
import com.nos.ploy.flow.ployee.settings.PloyeeSettingsFragment;
import com.nos.ploy.flow.ployer.home.view.PloyerCategoryRecyclerAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class PloyerHomeActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.imageview_main_footer_logo1)
    ImageView mImageViewFooterLogo1;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.recyclerview_ployer_home)
    RecyclerView mRecyclerView;
    @BindView(R.id.imageview_main_footer_more)
    ImageView mImageViewMore;
    @BindView(R.id.viewstub_main_appbar_searchview)
    ViewStub mStubSearchView;
    @BindView(R.id.swiperefreshlayout_ployer_home)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview_main_drawer_menu)
    RecyclerView mRecyclerViewDrawer;
    @BindView(R.id.drawerlayout_ployee_home)
    DrawerLayout mDrawerLayout;
    @BindString(R.string.PLOYEE)
    String LPLOYEE;
    @BindDrawable(R.drawable.ic_ployer_item_placeholder)
    Drawable mDrawableGenizPlaceHolder;
    @BindDrawable(R.drawable.ic_geniz_logo_133dp)
    Drawable mDrawableGenizLogo;
    @BindDimen(R.dimen.dp8)
    int dp16;
    private List<PloyerServicesGson.Data> mDatas = new ArrayList<>();
    private SearchView mSearchView;
    private PloyerCategoryRecyclerAdapter mAdapter;
    private Action1<PloyerServicesGson.Data> mOnItemClick = new Action1<PloyerServicesGson.Data>() {
        @Override
        public void call(PloyerServicesGson.Data data) {

        }
    };
    private static final Comparator<PloyerServicesGson.Data> ID_COMPARATOR = new Comparator<PloyerServicesGson.Data>() {
        @Override
        public int compare(PloyerServicesGson.Data a, PloyerServicesGson.Data b) {
            return Long.valueOf(a.getId()).compareTo(b.getId());
        }
    };

    private DrawerController.OnMenuItemSelectedListener mOnMenuItemSelectedListener = new DrawerController.OnMenuItemSelectedListener() {
        @Override
        public void onMenuItemSelected(@DrawerController.Menu int menu) {
            switch (menu) {
                case DrawerController.NONE:
                    break;
                case DrawerController.ACCOUNT:
                    AccountInfoLoader.getAccountGson(PloyerHomeActivity.this, mUserId, new Action1<AccountGson.Data>() {
                        @Override
                        public void call(AccountGson.Data accountData) {
                            if (null != accountData) {
                                showFragment(PloyeeAccountFragment.newInstance(accountData));
                            }
                        }
                    });
                    break;
                case DrawerController.SETTINGS:
                    showFragment(PloyeeSettingsFragment.newInstance(mUserId));
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
    private RetrofitCallUtils.RetrofitCallback mCallbackLoadData = new RetrofitCallUtils.RetrofitCallback<PloyerServicesGson>() {
        @Override
        public void onDataSuccess(PloyerServicesGson data) {
            dismissRefreshing();
            if (null != data && null != data.getData()) {
                bindData(data.getData());
            }

        }

        @Override
        public void onDataFailure(ResponseMessage failCause) {
            dismissRefreshing();
        }
    };

    private void bindData(ArrayList<PloyerServicesGson.Data> data) {
        mDatas.clear();
        mDatas.addAll(data);
        mAdapter.edit()
                .replaceAll(mDatas)
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ployer_home);
        ButterKnife.bind(this);
        mApi = getRetrofit().create(PloyerApi.class);

        initSearchView();
        initToolbar(mToolbar);
        initView();
        initFooter();
        initRecyclerView(mRecyclerView);
    }

    private void initSearchView() {
        mSearchView = (SearchView) mStubSearchView.inflate().findViewById(R.id.searchview_main);
        mSearchView.setOnQueryTextListener(this);
    }

    private void initFooter() {
        mImageViewFooterLogo1.setImageDrawable(mDrawableGenizLogo);
        mImageViewFooterLogo1.setPadding(dp16, dp16, dp16, dp16);
        mRecyclerViewDrawer.setBackgroundResource(android.R.color.white);
        DrawerController.initDrawer(this, mDrawerLayout, mRecyclerViewDrawer, mToolbar, mImageViewMore, Color.BLACK, mOnMenuItemSelectedListener);
        enableBackButton(mToolbar);
    }

    private void initView() {
        setRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDatas.isEmpty()) {
            refreshData();
        }
    }

    private void refreshData() {
        showRefreshing();
        RetrofitCallUtils.with(mApi.getServiceList(SharePreferenceUtils.getCurrentActiveAppLanguageCode(this)), mCallbackLoadData).enqueue(this);

    }

    private void initRecyclerView(RecyclerView rv) {
        mAdapter = new PloyerCategoryRecyclerAdapter(this, ID_COMPARATOR, mOnItemClick);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(mAdapter);

    }

    private void initToolbar(Toolbar toolbar) {
        mTextViewTitle.setText(R.string.Ployer);
        enableBackButton(toolbar);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<PloyerServicesGson.Data> filteredModelList = filter(mDatas, query);
        if (null != mAdapter) {
            mAdapter.edit()
                    .replaceAll(filteredModelList)
                    .commit();
        }

        if (null != mRecyclerView) {
            mRecyclerView.scrollToPosition(0);
        }

        return true;
    }

    private static List<PloyerServicesGson.Data> filter(List<PloyerServicesGson.Data> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<PloyerServicesGson.Data> filteredModelList = new ArrayList<>();
        for (PloyerServicesGson.Data model : models) {
            final String text = model.getServiceName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
