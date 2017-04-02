package com.nos.ploy.flow.ployer.filter.services;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.PostProviderFilterGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.PloyeeServiceDetailSubServiceRecyclerAdapter;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel.PloyeeServiceDetailSubServiceItemBaseViewModel;
import com.nos.ploy.utils.PopupMenuUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 15/1/2560.
 * Modified by adisit on 02/04/2560
 */

public class FilterServicesFragment extends BaseFragment implements View.OnClickListener, Action1<Boolean>, CompoundButton.OnCheckedChangeListener {

    private static final String KEY_SERVICE_DETAIL = "KEY_SERVICE_DETAIL";
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
    private PloyerServiceDetailGson.Data mServiceDetail;
    private List<PloyerServiceDetailGson.Data.SubService> mDatas = new ArrayList<>();
    private PloyeeServiceDetailSubServiceRecyclerAdapter mAdapter;
    private OnClickDoneListener listener;

    private PostProviderFilterGson mFilteredData = new PostProviderFilterGson();
    private long mTotal;
    private String strProvidersLabel = "";

    public static FilterServicesFragment newInstance(PloyerServiceDetailGson.Data mServiceDetail, PloyerServicesGson.Data data, long total, PostProviderFilterGson filterData, OnClickDoneListener listener) {

        Bundle args = new Bundle();
        Gson gson = new Gson();

        args.putString(KEY_SERVICE_DETAIL, gson.toJson(mServiceDetail));
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
        PopupMenuUtils.setMenuTitle(mToolbar.getMenu(),R.id.menu_done_item_done,data.doneLabel);
        mRadioButtonCertificate.setText(data.serviceScreenCertificateLabel);
        mRadioButtonEquipment.setText(data.serviceScreenEquipmentLabel);
        mButtonNoPref.setText(data.avaliabilityScreenNoPrefer);
        mTextViewSubtitle.setText(mTotal + " " + data.providersLabel);

        strProvidersLabel = data.providersLabel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mServiceData = getArguments().getParcelable(KEY_SERVICE_DATA);
            mTotal = getArguments().getLong(KEY_TOTAL_COUNT, 0);

            Gson gson = new Gson();
            String strKeyServiceDetail  = getArguments().getString(KEY_SERVICE_DETAIL);
            mServiceDetail = gson.fromJson(strKeyServiceDetail, PloyerServiceDetailGson.Data.class);

            PostProviderFilterGson filterData = getArguments().getParcelable(KEY_FILTERED_DATA);
            if (filterData != null) {
                mFilteredData.setCertificate(filterData.isCertificate());
                mFilteredData.setEquipment(filterData.isEquipment());
                if (filterData.getSubServices() != null) {
                    mFilteredData.setSubServices(new ArrayList<Long>(filterData.getSubServices()));
                }

            }


        }

        mAdapter = new PloyeeServiceDetailSubServiceRecyclerAdapter(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initRecyclerView();
        initView();

        bindData(mServiceDetail.getSubServices());

        mRadioButtonCertificate.setOnCheckedChangeListener(this);
        mRadioButtonEquipment.setOnCheckedChangeListener(this);

    }

    private void initToolbar() {
        enableBackButton(mToolbar);
        if (null != mServiceData) {
            mTextViewTitle.setText(mServiceData.getServiceName());
            mTextViewSubtitle.setVisibility(View.VISIBLE);

        }
//        mToolbar.inflateMenu(R.menu.menu_done);
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int id = item.getItemId();
//                if (id == R.id.menu_done_item_done) {
//                    if (null != mAdapter) {
//                        getListener().onClickDone(mAdapter.gatheredSubServiceIds(), mRadioButtonCertificate.isChecked(), mRadioButtonEquipment.isChecked());
//                    }
//                    dismiss();
//                }
//                return false;
//            }
//        });
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
//        if (mDatas.isEmpty()) {
//            refreshData();
//        }
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

    @Override
    public void call(Boolean aBoolean) {
        getListener().onClickDone(mAdapter.gatheredSubServiceIds(), mRadioButtonCertificate.isChecked(), mRadioButtonEquipment.isChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        getListener().onClickDone(mAdapter.gatheredSubServiceIds(), mRadioButtonCertificate.isChecked(), mRadioButtonEquipment.isChecked());
    }

    public void updateCountProviders(long mTotalCount) {

        mTextViewSubtitle.setText(mTotalCount + " " + strProvidersLabel);
    }

    public static interface OnClickDoneListener {
        public void onClickDone(List<Long> subServiceLv2Ids, boolean certificate, boolean equipment);
    }
}
