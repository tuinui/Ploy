package com.nos.ploy.flow.ployee.home.content.service.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.nos.ploy.R;
import com.nos.ploy.api.ployee.PloyeeApi;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PostSavePloyerServiceDetailGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.PloyeeServiceDetailContract;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.PloyeeServiceDetailPresenter;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.PloyeeServiceDetailVM;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.PloyeeServiceDetailSubServiceRecyclerAdapter;
import com.nos.ploy.utils.PopupMenuUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 19/11/2559.
 */

public class PloyeeServiceDetailFragment extends BaseFragment implements PloyeeServiceDetailContract.View, View.OnClickListener {
    @BindView(R.id.rangebar_ployee_service_rate)
    RangeBar mRangeBar;
    @BindView(R.id.edittext_ployee_service_price_from)
    MaterialEditText mEditTextPriceFrom;
    @BindView(R.id.edittext_ployee_service_price_to)
    MaterialEditText mEditTextPriceTo;
    @BindView(R.id.edittext_ployee_service_others)
    MaterialEditText mEditTextOthers;
    @BindView(R.id.edittext_ployee_service_description)
    MaterialEditText mEditTextDescription;
    @BindView(R.id.edittext_ployee_service_certificate)
    MaterialEditText mEditTextCertificate;
    @BindView(R.id.edittext_ployee_service_equipment_needed)
    MaterialEditText mEditTextEquipmentNeeded;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.swiprefreshlayout_ployee_service_detail)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview_ployee_service_sub_service)
    RecyclerView mRecyclerSubService;
    @BindView(R.id.textview_sub_services_header)
    TextView mTextViewSubServicesHeader;
    @BindView(R.id.button_ployee_service_reset)
    Button mButtonReset;

    private RangeBar.OnRangeBarChangeListener mRangeBarListener = new RangeBar.OnRangeBarChangeListener() {
        @Override
        public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
            setPriceText(mEditTextPriceFrom, leftPinValue);
            setPriceText(mEditTextPriceTo, rightPinValue);
        }
    };
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };
    private PloyeeApi mService;
    private PloyeeServiceDetailContract.Presenter mPresenter;
    private PloyeeServiceDetailContract.ViewModel mData;
    private PloyeeServiceDetailSubServiceRecyclerAdapter mAdapter = new PloyeeServiceDetailSubServiceRecyclerAdapter();
    private static final String KEY_SERVICE_ID = "SERVICE_ID";
    private long mUserId;
    private long mServiceId;

    public static PloyeeServiceDetailFragment newInstance(long userId, long serviceId) {
        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID, userId);
        args.putLong(KEY_SERVICE_ID, serviceId);
        PloyeeServiceDetailFragment fragment = new PloyeeServiceDetailFragment();
        fragment.setArguments(args);
        PloyerApi service = fragment.getRetrofit().create(PloyerApi.class);
        PloyeeServiceDetailPresenter.inject(service, userId, "en", serviceId, fragment);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = getRetrofit().create(PloyeeApi.class);
        if (null != getArguments()) {
            mUserId = getArguments().getLong(KEY_USER_ID, 0);
            mServiceId = getArguments().getLong(KEY_SERVICE_ID, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_service, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initRangeBar();
        initRecyclerView();
        initView();
    }

    private void initRecyclerView() {
        mRecyclerSubService.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerSubService.setAdapter(mAdapter);
    }

    private void initView() {
        disableEditable(mEditTextPriceFrom);
        disableEditable(mEditTextPriceTo);

        mEditTextPriceFrom.setOnClickListener(this);
        mEditTextPriceTo.setOnClickListener(this);
        mButtonReset.setOnClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);


    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void initToolbar() {
        enableBackButton(mToolbar);
        mToolbar.setTitle(R.string.Aide_au_senior);
        mToolbar.setSubtitle(R.string.Service);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_done_item_done) {
                    onClickDone();
                    return true;
                }
                return false;
            }
        });
    }

    private void setPriceText(MaterialEditText et, String price) {
        et.setText(price);
    }

    private void initRangeBar() {
        setPriceText(mEditTextPriceFrom, mRangeBar.getLeftPinValue());
        setPriceText(mEditTextPriceTo, mRangeBar.getRightPinValue());
        mRangeBar.setOnRangeBarChangeListener(mRangeBarListener);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mEditTextPriceFrom.getId()) {
            PopupMenuUtils.showPopupAlertEditTextFromEditTextMenu(mEditTextPriceFrom, new Action1<String>() {
                @Override
                public void call(String s) {
                    Integer value = Integer.valueOf(s);
                    mRangeBar.setTickStart(value);
                    mRangeBar.setRangePinsByIndices(value, mRangeBar.getRightIndex());
                }
            }, InputType.TYPE_CLASS_NUMBER);
        } else if (id == mEditTextPriceTo.getId()) {
            PopupMenuUtils.showPopupAlertEditTextFromEditTextMenu(mEditTextPriceTo, new Action1<String>() {
                @Override
                public void call(String s) {
                    Integer value = Integer.valueOf(s);
                    if (null != value && value >= 2) {
                        mRangeBar.setTickEnd(value);
                        mRangeBar.setRangePinsByIndices(mRangeBar.getLeftIndex(), value);
                    }

                }
            }, InputType.TYPE_CLASS_NUMBER);
        }else if(id == mButtonReset.getId()){
            onClickReset();
        }
    }

    private void refreshData() {
        mPresenter.refreshData();
    }

    @Override
    public void setPresenter(PloyeeServiceDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setRefreshing(boolean active) {
        mSwipeRefreshLayout.setRefreshing(active);
    }


    @Override
    public void toast(String s) {
        showToast(s);
    }

    @Override
    public void showLoadingDialog(boolean active) {
        if (active) {
            showLoading();
        } else {
            dismissLoading();
        }
    }

    @Override
    public void bindData(PloyerServiceDetailGson.Data data) {
        mData = new PloyeeServiceDetailVM(data);
        bindDataToView(mData);
    }

    private void onClickReset() {
        if (null != mData && null != mData.getData()) {
            PloyerServiceDetailGson.Data data = mData.getData().closeThis();
            data.setServiceNameOthers("");
            data.setPriceMax(2);
            data.setPriceMin(0);
            data.setEquipment("");
            data.setCertificate("");
            data.setDescription("");
            if (null != data.getSubServices()) {
                for (PloyerServiceDetailGson.Data.SubService subService : data.getSubServices()) {
                    if (null != subService.getSubServiceLV2()) {
                        for (PloyerServiceDetailGson.Data.SubService.SubServiceLv2 subServiceLv2 : subService.getSubServiceLV2()) {
                            subServiceLv2.setChecked(false);
                        }
                    }
                }
            }
            bindData(data);
        }

    }

    private void bindDataToView(PloyeeServiceDetailContract.ViewModel data) {
        if (null != data) {
            mEditTextDescription.setText(data.getDescription());
            mEditTextCertificate.setText(data.getCertificate());
            mEditTextEquipmentNeeded.setText(data.getEquipmentNeeded());
            mRangeBar.setTickStart(data.getPriceMin());
            mRangeBar.setTickEnd(data.getPriceMax());
            if (null != data.getData() && null != data.getData().getSubServices() && !data.getData().getSubServices().isEmpty()) {
                mAdapter.replaceData(data.getData().getSubServices());
                mTextViewSubServicesHeader.setVisibility(View.VISIBLE);
                mRecyclerSubService.setVisibility(View.VISIBLE);
            } else {
                mTextViewSubServicesHeader.setVisibility(View.GONE);
                mRecyclerSubService.setVisibility(View.GONE);
            }

            if (mServiceId == -1) {
                mEditTextOthers.setVisibility(View.VISIBLE);
                mEditTextOthers.setText(data.getServiceOthersName());
            } else {
                mEditTextOthers.setVisibility(View.GONE);
            }
        }
    }

    private PostSavePloyerServiceDetailGson gatheredData() {
        PostSavePloyerServiceDetailGson data = null;
        if (null != mData && null != mData.getData() && null != mAdapter) {
            data = new PostSavePloyerServiceDetailGson(mData.getData().closeThis());
            data.setUserId(mUserId);
            data.setServiceId(mServiceId);
            data.setCertificate(extractString(mEditTextCertificate));
            data.setDescription(extractString(mEditTextDescription));
            data.setEquipment(extractString(mEditTextEquipmentNeeded));
            data.setPriceMax(extractLong(mEditTextPriceTo));
            data.setPriceMin(extractLong(mEditTextPriceFrom));
            data.setServiceNameOthers(extractString(mEditTextOthers));
        }

        if (null != data && null != mAdapter) {
            data.setSubServiceLv2IdList(mAdapter.gatheredSubServiceIds());
        }

        return data;
    }

    private void onClickDone() {
        mPresenter.requestSaveServiceDetail(gatheredData());
    }


}
