package com.nos.ploy.flow.ployee.home.content.availability;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.ployee.PloyeeApi;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.home.content.availability.contract.AvailabilityViewModel;
import com.nos.ploy.flow.ployee.home.content.availability.contract.NormalItemAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.contract.WeekAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.view.AvailabilityRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 22/11/2559.
 */

public class PloyeeAvailabilityFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.recyclerview_ployee_availability_time_table)
    RecyclerView mRecyclerViewTimeTable;
    @BindView(R.id.switchcompat_ployee_availablity_holiday)
    SwitchCompat mSwitchHoliday;
    @BindView(R.id.button_ployee_availabality_no_preferences)
    Button mButtonNoPref;
    @BindView(R.id.swiperefreshlayout_ployee_availability)
    SwipeRefreshLayout mSwipRefreshlayout;
    private PloyeeApi mApi;
    private long mUserId;
    private AvailabilityRecyclerAdapter mAdapter = new AvailabilityRecyclerAdapter();
    private PloyeeAvailiabilityGson.Data mData;
    private Runnable mRunnableOnClickDone = new Runnable() {
        @Override
        public void run() {
            PloyeeAvailiabilityGson.Data data = mData.cloneThis();
            data.setHolidayMode(mSwitchHoliday.isChecked());
            data.setUserId(mUserId);
            requestSaveData(data);
        }
    };


    public static PloyeeAvailabilityFragment newInstance(long userId) {
        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID, userId);
        PloyeeAvailabilityFragment fragment = new PloyeeAvailabilityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(KEY_USER_ID);
        }
        mApi = getRetrofit().create(PloyeeApi.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_availability, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initView();
    }

    private void initView() {
        mButtonNoPref.setOnClickListener(this);
        setRefreshLayout(mSwipRefreshlayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null == mData) {
            refreshData();
        }
    }

    private void refreshData() {
        showRefreshing();
        RetrofitCallUtils.with(mApi.getAvailability(69), new RetrofitCallUtils.RetrofitCallback<PloyeeAvailiabilityGson>() {
            @Override
            public void onDataSuccess(PloyeeAvailiabilityGson data) {
                dismissRefreshing();
                if (null != data && null != data.getData()) {
                    bindData(data.getData());
                }
            }

            @Override
            public void onDataFailure(String failCause) {
                dismissRefreshing();
            }
        }).enqueue(getContext());
    }

    private void bindData(PloyeeAvailiabilityGson.Data data) {
        mData = data;
        if (null != mData) {
            if (null != mData.getAvailabilityItems()) {
                mAdapter.replaceData(toVm(mData.getAvailabilityItems()));
            }
            mSwitchHoliday.setChecked(data.getHolidayMode());
        }
    }

    private List<AvailabilityViewModel> toVm(List<PloyeeAvailiabilityGson.Data.AvailabilityItem> datas) {
        List<AvailabilityViewModel> vms = new ArrayList<>();
        vms.add(WeekAvailabilityVM.create());
        for (PloyeeAvailiabilityGson.Data.AvailabilityItem item : datas) {
            vms.add(new NormalItemAvailabilityVM(item));
        }
        return vms;
    }

    private void initRecyclerView() {
        //TODO : this should be change from grid to LinearLayoutManager with จัน ถึง ศุกร์
        mRecyclerViewTimeTable.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mRecyclerViewTimeTable.setAdapter(mAdapter);
    }


    public void onClickDone() {
        runOnUiThread(mRunnableOnClickDone);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonNoPref.getId()) {
            onClickNoPref();
        }
    }


    private void onClickNoPref() {
        PloyeeAvailiabilityGson.Data data = mData.cloneThis();
        data.setHolidayMode(false);
        data.setUserId(mUserId);
        List<PloyeeAvailiabilityGson.Data.AvailabilityItem> items = data.getAvailabilityItems();
        for (PloyeeAvailiabilityGson.Data.AvailabilityItem item : items) {
            item.setMon(false);
            item.setTues(false);
            item.setWed(false);
            item.setThurs(false);
            item.setFri(false);
            item.setSat(false);
            item.setSun(false);
        }
        bindData(mData);
    }

    private void requestSaveData(PloyeeAvailiabilityGson.Data data) {
        RetrofitCallUtils.with(mApi.postSaveAvailability(data), new RetrofitCallUtils.RetrofitCallback<PloyeeAvailiabilityGson>() {
            @Override
            public void onDataSuccess(PloyeeAvailiabilityGson data) {
                refreshData();
            }

            @Override
            public void onDataFailure(String failCause) {
                refreshData();
            }
        }).enqueue(getContext());

    }
}
