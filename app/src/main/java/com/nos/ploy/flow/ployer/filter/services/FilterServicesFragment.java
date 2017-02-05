package com.nos.ploy.flow.ployer.filter.services;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.PostGetPloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PostProviderFilterGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.PloyeeServiceDetailSubServiceRecyclerAdapter;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel.PloyeeServiceDetailSubServiceItemBaseViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 15/1/2560.
 */

public class FilterServicesFragment extends BaseFragment implements View.OnClickListener {

    private static final String KEY_SERVICE_DATA = "KEY_SERVICE_DATA";
    private static final String KEY_FILTERED_DATA = "SERVICE_IDS";
    private static final String KEY_TOTAL_COUNT = "TOTCAL";
    @BindView(R.id.radiobutton_filter_services_certificate)
    RadioButton mRadioButtonCertificate;
    @BindView(R.id.radiobutton_filter_services_equipment_needed)
    RadioButton mRadioButtonEquipment;
    @BindView(R.id.button_filter_services_no_pref)
    Button mButtonNoPref;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.textview_main_appbar_subtitle)
    TextView mTextViewSubtitle;
    @BindView(R.id.recyclerview_filter_services)
    RecyclerView mRecyclerView;
    private PloyerServicesGson.Data mServiceData;
    private PloyerApi mApi;
    private String mLgCode;
    private List<PloyerServiceDetailGson.Data.SubService> mDatas = new ArrayList<>();
    private PloyeeServiceDetailSubServiceRecyclerAdapter mAdapter = new PloyeeServiceDetailSubServiceRecyclerAdapter();
    private OnClickDoneListener listener;

    private RetrofitCallUtils.RetrofitCallback<PloyerServiceDetailGson> mCallbackRefresh = new RetrofitCallUtils.RetrofitCallback<PloyerServiceDetailGson>() {
        @Override
        public void onDataSuccess(PloyerServiceDetailGson data) {
            dismissRefreshing();
            if (null != data && null != data.getData() && null != data.getData().getSubServices()) {
                if (null != mServiceData) {
                    mTextViewSubtitle.setText(mServiceData.getPloyeeCount() + " " + mLanguageData.providersLabel);
                }

                bindData(data.getData().getSubServices());
            }
        }


        @Override
        public void onDataFailure(String failCause) {
            dismissRefreshing();
        }
    };
    private PostProviderFilterGson mFilteredData = new PostProviderFilterGson();
    private long mTotal;

    public static FilterServicesFragment newInstance(PloyerServicesGson.Data data, long total, PostProviderFilterGson filterData, OnClickDoneListener listener) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_SERVICE_DATA, data);
        args.putParcelable(KEY_FILTERED_DATA, filterData);
        args.putLong(KEY_TOTAL_COUNT, total);
        FilterServicesFragment fragment = new FilterServicesFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter_services, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mRadioButtonCertificate.setText(data.serviceScreenCertificateLabel);
        mRadioButtonEquipment.setText(data.serviceScreenEquipmentLabel);
        mButtonNoPref.setText(data.avaliabilityScreenNoPrefer);
        mTextViewSubtitle.setText(mTotal + " " + data.providersLabel);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mServiceData = getArguments().getParcelable(KEY_SERVICE_DATA);
            mTotal = getArguments().getLong(KEY_TOTAL_COUNT, 0);
            PostProviderFilterGson filterData = getArguments().getParcelable(KEY_FILTERED_DATA);
            if (filterData != null) {
                mFilteredData.setCertificate(filterData.isCertificate());
                mFilteredData.setEquipment(filterData.isEquipment());
                if (filterData.getSubServices() != null) {
                    mFilteredData.setSubServices(new ArrayList<Long>(filterData.getSubServices()));
                }

            }


        }
        mApi = getRetrofit().create(PloyerApi.class);
        mLgCode = SharePreferenceUtils.getCurrentActiveAppLanguageCode(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initRecyclerView();
        initView();
    }

    private void initToolbar() {
        enableBackButton(mToolbar);
        if (null != mServiceData) {
            mTextViewTitle.setText(mServiceData.getServiceName());
            mTextViewSubtitle.setVisibility(View.VISIBLE);

        }
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_done_item_done) {
                    if (null != mAdapter) {
                        getListener().onClickDone(mAdapter.gatheredSubServiceIds(), mRadioButtonCertificate.isChecked(), mRadioButtonEquipment.isChecked());
                    }
                    dismiss();
                }
                return false;
            }
        });
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                @PloyeeServiceDetailSubServiceItemBaseViewModel.ViewType int viewType = mAdapter.getItemViewType(position);
                if (viewType == PloyeeServiceDetailSubServiceItemBaseViewModel.ITEM || viewType == PloyeeServiceDetailSubServiceItemBaseViewModel.SPACE_ONE_ELEMENT) {
                    return 1;
                }
                return 2;
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        mButtonNoPref.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonNoPref.getId()) {
            onClickNoPref();
        }
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
        RetrofitCallUtils.with(mApi.getServiceDetail(new PostGetPloyerServiceDetailGson(mServiceData.getId(), -1L, mLgCode)), mCallbackRefresh).enqueue(getContext());
    }

    private void bindData(List<PloyerServiceDetailGson.Data.SubService> datas) {
        if (null == datas || null == mAdapter) {
            return;
        }

        mDatas.clear();
        mDatas.addAll(datas);
        if (null != mFilteredData) {
            mRadioButtonCertificate.setChecked(mFilteredData.isCertificate());
            mRadioButtonEquipment.setChecked(mFilteredData.isEquipment());
            if (null != mFilteredData.getSubServices() && !mFilteredData.getSubServices().isEmpty()) {
                for (PloyerServiceDetailGson.Data.SubService subService : mDatas) {
                    if (null != subService && null != subService.getSubServiceLV2()) {
                        for (PloyerServiceDetailGson.Data.SubService.SubServiceLv2 subServiceLv2 : subService.getSubServiceLV2()) {
                            if (null != subServiceLv2) {
                                for (Long checkedService : mFilteredData.getSubServices()) {
                                    if (subServiceLv2.getSubServiceLv2Id() == checkedService) {
                                        subServiceLv2.setChecked(true);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                for (PloyerServiceDetailGson.Data.SubService subService : mDatas) {
                    if (null != subService && null != subService.getSubServiceLV2()) {
                        for (PloyerServiceDetailGson.Data.SubService.SubServiceLv2 subServiceLv2 : subService.getSubServiceLV2()) {
                            if (null != subServiceLv2) {
                                subServiceLv2.setChecked(false);
                            }
                        }
                    }
                }

            }
        }
        mAdapter.replaceData(mDatas);
    }

    private void onClickNoPref() {
//        if (!mDatas.isEmpty()) {
//            for (PloyerServiceDetailGson.Data.SubService subService : mDatas) {
//                if (null != subService.getSubServiceLV2()) {
//                    for (PloyerServiceDetailGson.Data.SubService.SubServiceLv2 subServiceLv2 : subService.getSubServiceLV2()) {
//                        subServiceLv2.setChecked(false);
//                    }
//                }
//            }
//        }
        mFilteredData.setCertificate(false);
        mFilteredData.setEquipment(false);
        mFilteredData.setSubServices(new ArrayList<Long>());
        bindData(new ArrayList<>(mDatas));
    }

    public void setListener(OnClickDoneListener listener) {
        this.listener = listener;
    }

    public OnClickDoneListener getListener() {
        return listener;
    }

    public static interface OnClickDoneListener {
        public void onClickDone(List<Long> subServiceLv2Ids, boolean certificate, boolean equipment);
    }
}
