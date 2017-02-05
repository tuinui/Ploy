package com.nos.ploy.flow.ployer.filter.availability;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.MasterApi;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.home.content.availability.contract.AvailabilityViewModel;
import com.nos.ploy.flow.ployee.home.content.availability.contract.NormalItemAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.contract.WeekAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.view.AvailabilityRecyclerAdapter;
import com.nos.ploy.utils.PopupMenuUtils;

import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 14/1/2560.
 */

public class FilterAvailabilityFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.swiperefreshlayout_ployer_filter_availability)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.button_ployer_filter_availabality_no_preferences)
    Button mButtonNoPref;
    @BindView(R.id.recyclerview_ployer_filter_availability_time_table)
    RecyclerView mRecyclerView;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.textview_main_appbar_subtitle)
    TextView mTextViewSubtitle;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    private MasterApi mApi;
    private PloyeeAvailiabilityGson.Data mData;
    private PloyerServicesGson.Data mServiceData;
    private AvailabilityRecyclerAdapter mAdapter = new AvailabilityRecyclerAdapter();
    private static final String KEY_SERVICE_DATA = "SERVICE_DATA";
    private static final String KEY_AVAILABILITY_ITEMS = "AVAILABILITY_ITEMS";
    private static final String KEY_TOTAL_COUNT = "TOTAL_COUNT";
    private OnClickDoneListener listener;
    private ArrayList<PloyeeAvailiabilityGson.Data.AvailabilityItem> mAvailabilityItems = new ArrayList<>();
    private long mTotal;

    public static FilterAvailabilityFragment newInstance(PloyerServicesGson.Data data, long total, ArrayList<PloyeeAvailiabilityGson.Data.AvailabilityItem> availabilityItems, OnClickDoneListener listener) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_SERVICE_DATA, data);
        args.putParcelableArrayList(KEY_AVAILABILITY_ITEMS, availabilityItems);
        args.putLong(KEY_TOTAL_COUNT,total);
        FilterAvailabilityFragment fragment = new FilterAvailabilityFragment();
        fragment.setArguments(args);
//        fragment.setAvailabilityItems(availabilityItems);
        fragment.setListener(listener);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployer_filter_availability, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mServiceData = getArguments().getParcelable(KEY_SERVICE_DATA);
            mTotal = getArguments().getLong(KEY_TOTAL_COUNT);
            ArrayList<PloyeeAvailiabilityGson.Data.AvailabilityItem> items = getArguments().getParcelableArrayList(KEY_AVAILABILITY_ITEMS);
            if(null != items){
                for(PloyeeAvailiabilityGson.Data.AvailabilityItem item : items){
                    mAvailabilityItems.add(SerializationUtils.clone(item));
                }
            }
        }
        mApi = getRetrofit().create(MasterApi.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initRecyclerView();
        initToolbar();
    }

    private void initToolbar() {
        enableBackButton(mToolbar);
        mTextViewSubtitle.setVisibility(View.VISIBLE);
        mTextViewTitle.setText(R.string.Availability);

        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_done_item_done)
                    onClickDone();
                return false;
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        mButtonNoPref.setOnClickListener(this);
        setRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        showRefreshing();
        RetrofitCallUtils.with(mApi.getAvailability(-1), new RetrofitCallUtils.RetrofitCallback<PloyeeAvailiabilityGson>() {
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

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        PopupMenuUtils.setMenuTitle(mToolbar.getMenu(),R.id.menu_done_item_done,data.doneLabel);
        mAdapter.setLanguage(data);
        mButtonNoPref.setText(data.avaliabilityScreenNoPrefer);
        mTextViewSubtitle.setText(mTotal +" " +data.providersLabel);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonNoPref.getId()) {
            onClickNoPref();
        }
    }

    public void onClickDone() {
        PloyeeAvailiabilityGson.Data data = mData.cloneThis();
        if (null != data) {
            if (null != mAdapter) {
                data.setAvailabilityItems(mAdapter.gatheredData());
            }
            getListener().onClickDone(data);
            dismiss();
        }
    }


    private void onClickNoPref() {
        PloyeeAvailiabilityGson.Data data = mData.cloneThis();
        data.setHolidayMode(false);
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
        bindData(data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == mData) {
            refreshData();
        }
    }

    private void bindData(final PloyeeAvailiabilityGson.Data data) {
        mData = data;
        if (!mAvailabilityItems.isEmpty()) {
            mData.setAvailabilityItems(mAvailabilityItems);
        }
        if (null != mData && null != mData.getAvailabilityItems()) {
            mAdapter.replaceData(toVm(mData.getAvailabilityItems()));
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

    public void setListener(OnClickDoneListener listener) {
        this.listener = listener;
    }

    public OnClickDoneListener getListener() {
        return listener;
    }




    public static interface OnClickDoneListener {
        public void onClickDone(PloyeeAvailiabilityGson.Data data);
    }
}
