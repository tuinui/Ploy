package com.nos.ploy.flow.ployee.home;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.flow.ployee.home.content.PloyeeHomeListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PloyeeHomeActivity extends BaseActivity {
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
    private SearchView mSearchView;

    private PloyeeHomeListFragment mListFragment = PloyeeHomeListFragment.newInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ployee_home);
        ButterKnife.bind(this);

        mSearchView = (SearchView) mViewStubSearchView.inflate().findViewById(R.id.searchview_main);

        initDrawer(mDrawerLayout, mRecyclerViewDrawer, mToolbar,mImageViewMore);
        initFooter();
        initToolbar(mToolbar);
        addFragmentToActivity(mListFragment, R.id.framelayout_ployee_home_content_container);
    }

    private void initFooter() {
        mImageViewFooterLogo1.setVisibility(View.VISIBLE);
        mImageViewFooterLogo2.setVisibility(View.VISIBLE);


        mImageViewFooterLogo1.setImageResource(R.drawable.ic_business_gray_50dp);
        mImageViewFooterLogo2.setImageResource(R.drawable.ic_calendar_gray_50dp);
    }

    private void initToolbar(Toolbar toolbar) {
        enableBackButton(toolbar);
        toolbar.setTitle(R.string.Ployee);
    }

}
