package com.nos.ploy.flow.ployer.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.flow.ployer.home.view.PloyeeCategoryRecyclerAdapter;
import com.nos.ploy.flow.ployer.ployeelist.PloyeeListActivity;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PloyerHomeActivity extends BaseActivity {
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
    private PloyeeCategoryRecyclerAdapter mAdapter = new PloyeeCategoryRecyclerAdapter() {
        @Override
        public void onBindViewHolder(PloyeeCategoryRecyclerAdapter.ViewHolder holder, int position) {
            if (RecyclerUtils.isAvailableData(mDatas, position)) {
                PloyerServicesGson.Data data = mDatas.get(position);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentUtils.startActivity(PloyerHomeActivity.this, PloyeeListActivity.class);
                    }
                });
                Glide.with(holder.imgServiceImage.getContext()).load(data.getImgUrl()).placeholder(mDrawableGenizPlaceHolder).error(mDrawableGenizPlaceHolder).fallback(mDrawableGenizPlaceHolder).into(holder.imgServiceImage);
                holder.tvTitle.setText(data.getServiceName());
                holder.tvSubtitle.setText(data.getPloyeeCount() + " " + LPLOYEE);
            }
        }

        @Override
        public int getItemCount() {
            return RecyclerUtils.getSize(mDatas);
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
        mAdapter.notifyDataSetChanged();
        mDatas.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ployer_home);
        ButterKnife.bind(this);
//        mDrawableGenizPlaceHolder = DrawableUtils.changeDrawableColor(mDrawableGenizPlaceHolder,colorWhite);
        mApi = getRetrofit().create(PloyerApi.class);
        mSearchView = (SearchView) mStubSearchView.inflate().findViewById(R.id.searchview_main);
        initToolbar(mToolbar);
        initView();
        initFooter();
        initRecyclerView(mRecyclerView);
    }

    private void initFooter() {
        mImageViewFooterLogo1.setImageDrawable(mDrawableGenizLogo);
        mImageViewFooterLogo1.setPadding(dp16, dp16, dp16, dp16);
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
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(mAdapter);

    }

    private void initToolbar(Toolbar toolbar) {
        mTextViewTitle.setText(R.string.Ployer);
        enableBackButton(toolbar);

    }
}
