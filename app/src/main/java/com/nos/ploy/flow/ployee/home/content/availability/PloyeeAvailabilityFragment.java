package com.nos.ploy.flow.ployee.home.content.availability;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.MasterApi;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployee.PloyeeApi;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.home.content.availability.contract.AvailabilityViewModel;
import com.nos.ploy.flow.ployee.home.content.availability.contract.NormalItemAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.contract.WeekAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.view.AvailabilityRecyclerAdapter;
import com.nos.ploy.utils.PopupMenuUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

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
    @BindView(R.id.textview_ployee_availability_holiday_mode_label)
    TextView mTextViewHolidayModeLabel;
    @BindView(R.id.swiperefreshlayout_ployee_availability)
    SwipeRefreshLayout mSwipRefreshlayout;
    @BindView(R.id.textview_ployee_availability_holiday_mode_description)
    TextView mTextViewHolidayDescription;
    private MasterApi mMasterApi;
    private PloyeeApi mPloyeeApi;
    private long mUserId;
    private boolean isContentChanged;

    private AvailabilityRecyclerAdapter mAdapter = new AvailabilityRecyclerAdapter(new Action1<Void>() {
        @Override
        public void call(Void aVoid) {
            isContentChanged = true;
        }
    });
    private PloyeeAvailiabilityGson.Data mData;


    private CompoundButton.OnCheckedChangeListener mHolidayChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isContentChanged = true;
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
        mMasterApi = getRetrofit().create(MasterApi.class);
        mPloyeeApi = getRetrofit().create(PloyeeApi.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_availability, container, false);
        ButterKnife.bind(this, v);
        return v;
    }


    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mTextViewHolidayModeLabel.setText(data.avaliabilityScreenHolidayMode);
        mTextViewHolidayDescription.setText(data.avaliabilityScreenHolidayDescript);
        mButtonNoPref.setText(data.avaliabilityScreenNoPrefer);
        mAdapter.setLanguage(data);
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
        isContentChanged = false;
        RetrofitCallUtils.with(mMasterApi.getAvailability(mUserId), new RetrofitCallUtils.RetrofitCallback<PloyeeAvailiabilityGson>() {
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

    private void bindData(final PloyeeAvailiabilityGson.Data data) {
        mData = data;
        mSwitchHoliday.setOnCheckedChangeListener(null);
        if (null != mData) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != mData.getAvailabilityItems()) {
                        mAdapter.replaceData(toVm(mData.getAvailabilityItems()));
                    }
                    mSwitchHoliday.setChecked(data.getHolidayMode());
                }
            });
        }
        mSwitchHoliday.setOnCheckedChangeListener(mHolidayChangeListener);
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
        PloyeeAvailiabilityGson.Data data = mData.cloneThis();
        if (null != data) {
            data.setHolidayMode(mSwitchHoliday.isChecked());
            data.setUserId(mUserId);
            if (null != mAdapter) {
                data.setAvailabilityItems(mAdapter.gatheredData());
            }
            requestSaveData(data);
        }

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
        bindData(data);
    }

    private void requestSaveData(PloyeeAvailiabilityGson.Data data) {
        showLoading();
        RetrofitCallUtils.with(mPloyeeApi.postSaveAvailability(data), new RetrofitCallUtils.RetrofitCallback<PloyeeAvailiabilityGson>() {
            @Override
            public void onDataSuccess(PloyeeAvailiabilityGson data) {
                dismissLoading();
                showToast("Success");
                refreshData();
            }

            @Override
            public void onDataFailure(String failCause) {
                dismissLoading();
                refreshData();
            }
        }).enqueue(getContext());

    }

    public boolean isContentChanged(final Action1<Boolean> onYes) {
        if (isContentChanged) {
            PopupMenuUtils.showConfirmationAlertMenu(getContext(), null, mLanguageData.accountScreenConfirmBeforeBack, mLanguageData.okLabel, mLanguageData.cancelLabel, new Action1<Boolean>() {
                @Override
                public void call(Boolean yes) {
                    if (null != onYes) {
                        onYes.call(yes);
                    }

                    if (yes) {
                        refreshData();
                    }
                }
            });
            return true;
        }
        return false;
    }
}
