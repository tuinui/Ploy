package com.nos.ploy.flow.ployee.home;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.nos.ploy.R;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.flow.ployee.home.view.PloyeeHomeRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PloyeeHomeActivity extends BaseActivity {
    @BindView(R.id.imageview_main_footer_more)
    ImageView mImageViewMore;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview_ployee_home)
    RecyclerView mRecyclerView;
    @BindView(R.id.imageview_main_footer_logo1)
    ImageView mImageViewFooterLogo1;
    @BindView(R.id.imageview_main_footer_logo2)
    ImageView mImageViewFooterLogo2;
    @BindView(R.id.viewstub_main_appbar_searchview)
    ViewStub mViewStubSearchView;
    private SearchView mSearchView;

    private PloyeeHomeRecyclerAdapter mAdapter = new PloyeeHomeRecyclerAdapter() {
        @Override
        public void onBindViewHolder(PloyeeHomeRecyclerAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 100;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ployee_home);
        ButterKnife.bind(this);
        mSearchView = (SearchView) mViewStubSearchView.inflate().findViewById(R.id.searchview_main);
        initFooter();
        initToolbar(mToolbar);
        initRecyclerview(mRecyclerView);
    }

    private void initFooter(){
        mImageViewFooterLogo1.setVisibility(View.VISIBLE);
        mImageViewFooterLogo2.setVisibility(View.VISIBLE);


        mImageViewFooterLogo1.setImageResource(R.drawable.ic_business_gray_50dp);
        mImageViewFooterLogo2.setImageResource(R.drawable.ic_calendar_gray_50dp);
    }

    private void initToolbar(Toolbar toolbar) {
        enableBackButton(toolbar);
        toolbar.setTitle(R.string.Ployee);
    }

    private void initRecyclerview(RecyclerView recyclerView) {
        if (null == recyclerView) {
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }
}
