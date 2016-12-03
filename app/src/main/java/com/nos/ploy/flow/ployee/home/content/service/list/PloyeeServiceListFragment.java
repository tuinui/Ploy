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
import com.nos.ploy.api.ployee.PloyeeApi;
import com.nos.ploy.api.ployee.model.PloyeeServiceListGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.home.content.service.detail.PloyeeServiceDetailFragment;
import com.nos.ploy.flow.ployee.home.content.service.list.viewmodel.PloyeeServiceItemViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by User on 19/11/2559.
 */

public class PloyeeServiceListFragment extends BaseFragment implements SearchView.OnQueryTextListener {


    @BindView(R.id.recyclerview_ployee_service_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout_ployee_service_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private PloyeeApi mService;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };
    private Callback<PloyeeServiceListGson> mCallbackPloyeeService = new Callback<PloyeeServiceListGson>() {
        @Override
        public void onResponse(Call<PloyeeServiceListGson> call, Response<PloyeeServiceListGson> response) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (response.isSuccessful() && response.body().isSuccess()) {
                bindData(response.body().getData());
            } else {
                showToast("isNotSuccessful");
            }
        }


        @Override
        public void onFailure(Call<PloyeeServiceListGson> call, Throwable t) {
            mSwipeRefreshLayout.setRefreshing(false);
            showToast("onFailure");
        }
    };
    private Action1<PloyeeServiceItemViewModel> mActionOnClickServiceItem = new Action1<PloyeeServiceItemViewModel>() {
        @Override
        public void call(PloyeeServiceItemViewModel data) {
            showFragment(PloyeeServiceDetailFragment.newInstance(mUserId, data.getId()));
        }
    };
    private List<PloyeeServiceItemViewModel> mDatas = new ArrayList<>();
    private long mUserId;


    public static PloyeeServiceListFragment newInstance(Long userId) {
        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID, userId);
        PloyeeServiceListFragment fragment = new PloyeeServiceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static final Comparator<PloyeeServiceItemViewModel> ALPHABETICAL_COMPARATOR = new Comparator<PloyeeServiceItemViewModel>() {
        @Override
        public int compare(PloyeeServiceItemViewModel a, PloyeeServiceItemViewModel b) {
            return a.getText().compareTo(b.getText());
        }
    };

    private PloyeeHomeRecyclerAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mUserId = getArguments().getLong(KEY_USER_ID, 0L);
        }
        mService = getRetrofit().create(PloyeeApi.class);
        mAdapter = new PloyeeHomeRecyclerAdapter(getActivity(), ALPHABETICAL_COMPARATOR, mActionOnClickServiceItem);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_service_list, container, false);
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
        mAdapter.edit()
                .replaceAll(mDatas)
                .commit();
    }

    private List<PloyeeServiceItemViewModel> toVm(List<PloyeeServiceListGson.PloyeeServiceItemGson> datas) {
        List<PloyeeServiceItemViewModel> results = new ArrayList<>();
        for (PloyeeServiceListGson.PloyeeServiceItemGson data : datas) {
            results.add(new PloyeeServiceItemViewModel(data));
        }
        return results;
    }

    private void initRecyclerview(RecyclerView recyclerView) {
        if (null == recyclerView) {
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }


    private void refreshData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mService.getPloyeeServiceList("en").enqueue(mCallbackPloyeeService);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<PloyeeServiceItemViewModel> filteredModelList = filter(mDatas, query);
        mAdapter.edit()
                .replaceAll(filteredModelList)
                .commit();
        mRecyclerView.scrollToPosition(0);
        return true;
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
}
