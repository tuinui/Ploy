package com.nos.ploy.flow.ployee.home.content.service.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nos.ploy.R;
import com.nos.ploy.api.account.model.MemberProfileGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.MasterApi;
import com.nos.ploy.api.ployee.PloyeeApi;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.api.ployee.model.PloyeeServiceListGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.flow.ployee.home.content.service.detail.PloyeeServiceDetailFragment;
import com.nos.ploy.flow.ployee.home.content.service.list.viewmodel.PloyeeServiceItemViewModel;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by User on 19/11/2559.
 */

public class PloyeeServiceListFragment extends BaseFragment implements SearchView.OnQueryTextListener {


    //    private static final Comparator<PloyeeServiceItemViewModel> ALPHABETICAL_COMPARATOR = new Comparator<PloyeeServiceItemViewModel>() {
//        @Override
//        public int compare(PloyeeServiceItemViewModel a, PloyeeServiceItemViewModel b) {
//            return a.getId().compareTo(b.getId());
//        }
//    };
    @BindView(R.id.recyclerview_swipe_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout_swipe_recycler)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private PloyeeApi mService;
    private PloyerApi mApi;
    private List<PloyeeServiceItemViewModel> mDatas = new ArrayList<>();
    private long mUserId;
    private Action1<PloyeeServiceItemViewModel> mActionOnClickServiceItem = new Action1<PloyeeServiceItemViewModel>() {
        @Override
        public void call(final PloyeeServiceItemViewModel data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    if (isCanWork) {
                        showFragment(PloyeeServiceDetailFragment.newInstance(mUserId, data.getId(), SharePreferenceUtils.getCurrentActiveAppLanguageCode(getContext()), data.getText()));
//                    } else {
//                        PopupMenuUtils.showConfirmationAlertMenu(getContext(), null, mLanguageData.providerAvailabilityNotSelect, mLanguageData.okLabel, null, null);
//                    }
                }
            });

        }
    };
    private PloyeeHomeRecyclerAdapter mAdapter;
    //    private Callback<PloyeeServiceListGson> mCallbackPloyeeService = new Callback<PloyeeServiceListGson>() {
//        @Override
//        public void onResponse(Call<PloyeeServiceListGson> call, Response<PloyeeServiceListGson> response) {
//            mSwipeRefreshLayout.setRefreshing(false);
//            if (response.isSuccessful() && response.body().isSuccess()) {
//                bindData(response.body().getData());
//            } else {
//                showToast("isNotSuccessful");
//            }
//        }
//
//
//        @Override
//        public void onFailure(Call<PloyeeServiceListGson> call, Throwable t) {
//            mSwipeRefreshLayout.setRefreshing(false);
//            showToast("onFailure");
//        }
//    };
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };
    private boolean isCanWork = true;

    public static PloyeeServiceListFragment newInstance(Long userId) {
        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID, userId);
        PloyeeServiceListFragment fragment = new PloyeeServiceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static List<PloyeeServiceItemViewModel> filter(List<PloyeeServiceItemViewModel> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<PloyeeServiceItemViewModel> filteredModelList = new ArrayList<>();
        for (PloyeeServiceItemViewModel model : models) {
            final String text = model.getText().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mUserId = getArguments().getLong(KEY_USER_ID, 0L);
        }
        mService = getRetrofit().create(PloyeeApi.class);
        mApi = getRetrofit().create(PloyerApi.class);
        mAdapter = new PloyeeHomeRecyclerAdapter(mActionOnClickServiceItem);
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
        initRecyclerview(mRecyclerView);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDatas.isEmpty()) {
            refreshData();
        }
    }

    private void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    private void bindData(List<PloyeeServiceListGson.PloyeeServiceItemGson> datas) {
        mDatas.clear();
        mDatas.addAll(toVm(datas));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.replaceData(mDatas);
            }
        });

    }

    private List<PloyeeServiceItemViewModel> toVm(List<PloyeeServiceListGson.PloyeeServiceItemGson> datas) {
        List<PloyeeServiceItemViewModel> results = new ArrayList<>();
        for (PloyeeServiceListGson.PloyeeServiceItemGson data : datas) {
            results.add(new PloyeeServiceItemViewModel(data));
        }
        return results;
    }

    private void requestAvailability() {
        RetrofitCallUtils.with(getRetrofit().create(MasterApi.class).getAvailability(mUserId), new RetrofitCallUtils.RetrofitCallback<PloyeeAvailiabilityGson>() {
            @Override
            public void onDataSuccess(PloyeeAvailiabilityGson data) {
                dismissRefreshing();
                if (null != data && null != data.getData()) {
                    isCanWork = isCanWork(data.getData().getAvailabilityItems());
                }
            }

            @Override
            public void onDataFailure(String failCause) {
                dismissRefreshing();
            }
        }).enqueue(getContext());
    }

    private boolean isCanWork(ArrayList<PloyeeAvailiabilityGson.Data.AvailabilityItem> items) {
        boolean canWork = false;
        if (RecyclerUtils.getSize(items) > 0) {
            for (PloyeeAvailiabilityGson.Data.AvailabilityItem item : items) {
                if (item.isMon() || item.isTues() || item.isWed() || item.isThurs() || item.isFri() || item.isSat() || item.isSun()) {
                    canWork = true;
                }
            }
        }
        return canWork;
    }

    private void initRecyclerview(RecyclerView recyclerView) {
        if (null == recyclerView) {
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }


    private List<PloyerServiceDetailGson.Data> serviceOfUser = new ArrayList<>();
    private RetrofitCallUtils.RetrofitCallback<MemberProfileGson> mCallbackRefreshData = new RetrofitCallUtils.RetrofitCallback<MemberProfileGson>() {
        @Override
        public void onDataSuccess(MemberProfileGson data) {
            dismissRefreshing();
            if (null != data && null != data.getData()) {
                serviceOfUser = data.getData().getServiceDetails();

                if (serviceOfUser == null){
                    serviceOfUser = new ArrayList<>();
                }

                RetrofitCallUtils.with(mService.getServiceList(SharePreferenceUtils.getCurrentActiveAppLanguageCode(getContext())), new RetrofitCallUtils.RetrofitCallback<PloyeeServiceListGson>() {
                    @Override
                    public void onDataSuccess(PloyeeServiceListGson data) {
                        mSwipeRefreshLayout.setRefreshing(false);

                        List<PloyeeServiceListGson.PloyeeServiceItemGson> items = data.getData();

                        for (int i = 0 ; i < serviceOfUser.size() ; i++){
                            for (int j = 0 ; j < items.size() ; j ++){

                                if (serviceOfUser.get(i).getServiceId() == items.get(j).getId()){
                                    items.get(j).setSeleced(true);
                                }
                            }
                        }

                        bindData(items);
                    }

                    @Override
                    public void onDataFailure(String failCause) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }).enqueue(getContext());
                requestAvailability();


            }
        }


        @Override
        public void onDataFailure(String failCause) {
            dismissRefreshing();
        }
    };

    public void refreshData() {

        mSwipeRefreshLayout.setRefreshing(true);
        RetrofitCallUtils.with(mApi.getProviderProfileGson(mUserId, SharePreferenceUtils.getCurrentActiveAppLanguageCode(getActivity())), mCallbackRefreshData).enqueueDontToast(getActivity());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
//        final List<PloyeeServiceItemViewModel> filteredModelList = filter(mDatas, query);
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
}
