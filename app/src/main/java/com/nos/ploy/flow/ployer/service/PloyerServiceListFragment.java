package com.nos.ploy.flow.ployer.service;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.flow.ployer.service.view.PloyerCategoryRecyclerAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 6/1/2560.
 */

public class PloyerServiceListFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    @BindView(R.id.swiperefreshlayout_swipe_recycler)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview_swipe_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    TextView emptyView;

    private PloyerApi mApi;
    private List<PloyerServicesGson.Data> mDatas = new ArrayList<>();
    private PloyerCategoryRecyclerAdapter mAdapter;
    private RetrofitCallUtils.RetrofitCallback mCallbackLoadData = new RetrofitCallUtils.RetrofitCallback<PloyerServicesGson>() {
        @Override
        public void onDataSuccess(PloyerServicesGson data) {
            dismissRefreshing();
            if (null != data && null != data.getData()) {
                bindData(data.getData());
            }
        }

        @Override
        public void onDataFailure(String failCause) {
            dismissRefreshing();
        }
    };
    private Action1<PloyerServicesGson.Data> mOnItemClick = new Action1<PloyerServicesGson.Data>() {
        @Override
        public void call(PloyerServicesGson.Data data) {
            getListener().onServiceClick(data);
        }
    };

    private static final Comparator<PloyerServicesGson.Data> ID_COMPARATOR = new Comparator<PloyerServicesGson.Data>() {
        @Override
        public int compare(PloyerServicesGson.Data a, PloyerServicesGson.Data b) {
            return a.getId().compareTo(b.getId());
        }
    };
    private FragmentInteractionListener listener;

    public static PloyerServiceListFragment newInstance(FragmentInteractionListener listener) {

        Bundle args = new Bundle();

        PloyerServiceListFragment fragment = new PloyerServiceListFragment();
        fragment.setArguments(args);

        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = getRetrofit().create(PloyerApi.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_swipe_recycler, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initRecyclerView();
    }

    private void initView() {
        setRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(mSwipeRefreshLayout.getContext());
            }
        });
    }

    private void refreshData(Context context) {
        if (null != context && isReadyForFragmentTransaction()) {
            showRefreshing();
            RetrofitCallUtils.with(mApi.getServiceList(SharePreferenceUtils.getCurrentActiveAppLanguageCode(getContext())), mCallbackLoadData).enqueue(getContext());
        }


    }


    private void initRecyclerView() {
        mAdapter = new PloyerCategoryRecyclerAdapter(mOnItemClick);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void bindData(ArrayList<PloyerServicesGson.Data> data) {
        mDatas.clear();
        mDatas.addAll(data);
        mAdapter.replaceData(data);

        if (data.size() == 0){
            emptyView.setVisibility(View.VISIBLE);
        }else {
            emptyView.setVisibility(View.GONE);
        }
//        mAdapter.edit()
//                .replaceAll(mDatas)
//                .commit();
    }



    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mAdapter.setLanguage(data);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
//        final List<PloyerServicesGson.Data> filteredModelList = filter(mDatas, query);
        if (null != mAdapter) {
//            mAdapter.edit()
//                    .replaceAll(filteredModelList)
//                    .commit();
            mAdapter.filterList(query);
        }

        if (null != mRecyclerView) {
            mRecyclerView.scrollToPosition(0);
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDatas.isEmpty()) {
            refreshData(getContext());
        }
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

    public void setListener(FragmentInteractionListener listener) {
        this.listener = listener;
    }

    public FragmentInteractionListener getListener() {
        return listener;
    }

    public static interface FragmentInteractionListener {
        public void onServiceClick(PloyerServicesGson.Data data);
    }
}
