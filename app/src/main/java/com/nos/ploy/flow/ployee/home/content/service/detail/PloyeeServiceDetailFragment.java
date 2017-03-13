package com.nos.ploy.flow.ployee.home.content.service.detail;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.BaseResponse;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployee.PloyeeApi;
import com.nos.ploy.api.ployee.model.DeleteServiceGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PostSavePloyerServiceDetailGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.custom.view.InputFilterMinMax;
import com.nos.ploy.custom.view.NumberTextWatcher;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.PloyeeServiceDetailContract;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.PloyeeServiceDetailPresenter;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.PloyeeServiceDetailVM;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.PloyeeServiceDetailSubServiceRecyclerAdapter;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel.PloyeeServiceDetailSubServiceItemBaseViewModel;
import com.nos.ploy.utils.PopupMenuUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 19/11/2559.
 */

public class PloyeeServiceDetailFragment extends BaseFragment implements PloyeeServiceDetailContract.View, View.OnClickListener {
    private static final String KEY_SERVICE_ID = "SERVICE_ID";
    private static final String KEY_TOOLBAR_TITLE = "TOOLBAR_TITLE";
    @BindView(R.id.materialrangebar_ployee_service_rate)
    RangeBar mRangeBar;
    @BindView(R.id.edittext_ployee_service_price_from)
    MaterialEditText mEditTextPriceFrom;
    @BindView(R.id.edittext_ployee_service_price_to)
    MaterialEditText mEditTextPriceTo;
    @BindView(R.id.edittext_ployee_service_others)
    MaterialEditText mEditTextOthers;
    @BindView(R.id.textview_ployee_service_price_per_hour)
    TextView mTextViewPricePerHour;
    @BindView(R.id.edittext_ployee_service_description)
    MaterialEditText mEditTextDescription;
    @BindView(R.id.edittext_ployee_service_certificate)
    MaterialEditText mEditTextCertificate;
    @BindView(R.id.edittext_ployee_service_equipment_needed)
    MaterialEditText mEditTextEquipmentNeeded;
    @BindView(R.id.button_ployee_service_delete)
    Button mButtonDelete;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_subtitle)
    TextView mTextViewSubtitle;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.swiprefreshlayout_ployee_service_detail)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview_ployee_service_sub_service)
    RecyclerView mRecyclerSubService;
    @BindView(R.id.textview_sub_services_header)
    TextView mTextViewSubServicesHeader;
    @BindView(R.id.button_ployee_service_reset)
    Button mButtonReset;
    @BindString(R.string.This_field_is_required)
    String LThis_field_is_required;
    @BindColor(R.color.colorPrimary)
    @ColorInt
    int mColorPrimary;


    private RangeBar.OnRangeBarChangeListener mRangeBarListener = new RangeBar.OnRangeBarChangeListener() {
        @Override
        public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
            isContentChanged = true;
            setText(mEditTextPriceFrom, leftPinValue);
            setText(mEditTextPriceTo, rightPinValue);
        }
    };
    private PloyeeApi mService;
    private PloyeeServiceDetailContract.Presenter mPresenter;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };
    private PloyeeServiceDetailContract.ViewModel mData;
    private PloyeeServiceDetailSubServiceRecyclerAdapter mAdapter = new PloyeeServiceDetailSubServiceRecyclerAdapter(new Action1<Boolean>() {
        @Override
        public void call(Boolean aBoolean) {
            isContentChanged = true;
        }
    });
    private long mUserId;
    private long mServiceId;
    private String mToolbarTitle;

    private boolean isContentChanged = false;


    public static PloyeeServiceDetailFragment newInstance(long userId, long serviceId, String currentActiveLanguageCode, String title) {
        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID, userId);
        args.putLong(KEY_SERVICE_ID, serviceId);
        args.putString(KEY_TOOLBAR_TITLE, title);
        PloyeeServiceDetailFragment fragment = new PloyeeServiceDetailFragment();
        fragment.setArguments(args);
        PloyerApi service = fragment.getRetrofit().create(PloyerApi.class);
        PloyeeServiceDetailPresenter.inject(service, userId, currentActiveLanguageCode, serviceId, fragment);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = getRetrofit().create(PloyeeApi.class);
        if (null != getArguments()) {
            mUserId = getArguments().getLong(KEY_USER_ID, 0);
            mServiceId = getArguments().getLong(KEY_SERVICE_ID, 0);
            mToolbarTitle = getArguments().getString(KEY_TOOLBAR_TITLE, "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_service, container, false);
        ButterKnife.bind(this, v);
        return v;
    }


    private TextWatcher mContentChangedTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isContentChanged = true;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void detectContentChanged() {
        mEditTextCertificate.addTextChangedListener(mContentChangedTextWatcher);
        mEditTextDescription.addTextChangedListener(mContentChangedTextWatcher);
        mEditTextEquipmentNeeded.addTextChangedListener(mContentChangedTextWatcher);
        mEditTextOthers.addTextChangedListener(mContentChangedTextWatcher);
        mEditTextPriceFrom.addTextChangedListener(mContentChangedTextWatcher);
        mEditTextPriceTo.addTextChangedListener(mContentChangedTextWatcher);
    }

    private void setText(EditText editText,String text){
        editText.removeTextChangedListener(mContentChangedTextWatcher);
        editText.setText(text);
        editText.addTextChangedListener(mContentChangedTextWatcher);
    }


    @Override
    public boolean onBackPressed() {
        if (isContentChanged) {
            PopupMenuUtils.showConfirmationAlertMenu(getContext(), null, mLanguageData.accountScreenConfirmBeforeBack, mLanguageData.okLabel, mLanguageData.cancelLabel, new Action1<Boolean>() {
                @Override
                public void call(Boolean yes) {
                    if (yes) {
                        dismiss();
                    }
                }
            });
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initRecyclerView();
        initView();
        initRangeBar();
        detectContentChanged();
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);

        PopupMenuUtils.setMenuTitle(mToolbar.getMenu(), R.id.menu_done_item_done, data.doneLabel);
        mTextViewSubtitle.setText(data.serviceScreenHeader);
        mButtonDelete.setText(data.serviceScreenDelete);
        mEditTextOthers.setFloatingLabelText(data.serviceScreenServicesHint);
        mEditTextOthers.setHint(data.serviceScreenServicesHint);
        mEditTextDescription.setFloatingLabelText(data.serviceScreenDescriptLabel);
        mEditTextDescription.setHint(data.serviceScreenDescriptHint);
        mEditTextCertificate.setFloatingLabelText(data.serviceScreenCertificateLabel);
        mEditTextCertificate.setHint(data.serviceScreenCertificateHint);
        mEditTextEquipmentNeeded.setFloatingLabelText(data.serviceScreenEquipmentLabel);
        mEditTextEquipmentNeeded.setHint(data.serviceScreenEquipmentHint);
        mAdapter.setLanguage(data);
        mEditTextPriceFrom.setHint(data.serviceScreenFrom);
        mTextViewSubServicesHeader.setText(data.servicesLabel);
        mEditTextPriceTo.setHint(data.serviceScreenTo);
        mButtonReset.setText(data.serviceScreenReset);
    }

    private void initRecyclerView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2) {
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
        mRecyclerSubService.setLayoutManager(gridLayoutManager);
        mRecyclerSubService.setAdapter(mAdapter);
    }


    private void initView() {
        disableEditable(mEditTextPriceFrom);
        disableEditable(mEditTextPriceTo);

        mTextViewSubServicesHeader.setBackgroundColor(mColorPrimary);
        mEditTextPriceFrom.setOnClickListener(this);
        mEditTextPriceFrom.addTextChangedListener(new NumberTextWatcher(mEditTextPriceFrom));
//        mEditTextPriceFrom.setFilters(mInputMinMaxFilter);

        mEditTextPriceTo.setOnClickListener(this);
        mEditTextPriceTo.addTextChangedListener(new NumberTextWatcher(mEditTextPriceTo));
//        mEditTextPriceTo.setFilters(mInputMinMaxFilter);
        mButtonReset.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        mButtonDelete.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void initToolbar() {
        enableBackButton(mToolbar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onBackPressed()) {
                    dismiss();
                }
            }
        });
        mTextViewTitle.setText(mToolbarTitle);
        mTextViewSubtitle.setVisibility(View.VISIBLE);
        mTextViewSubtitle.setText(R.string.Service);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_done_item_done) {
                    attempUpdateService();
                    return true;
                }
                return false;
            }
        });
    }

//    private void setPriceText(MaterialEditText et, String price) {
//        et.setText(price);
//    }

    private void initRangeBar() {
//        setPriceText(mEditTextPriceFrom, mRangeBar.getLeftPinValue());
//        setPriceText(mEditTextPriceTo, mRangeBar.getRightPinValue());
        mRangeBar.setOnRangeBarChangeListener(mRangeBarListener);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mEditTextPriceFrom.getId()) {
            showPopupAlertEditTextMenu(mEditTextPriceFrom.getContext(), String.valueOf(mEditTextPriceFrom.getHint()), String.valueOf(mEditTextPriceFrom.getText()), new Action1<String>() {
                @Override
                public void call(String s) {
                    if (Long.valueOf(s) >= 1000) {
                        setLeftPinValue(1000);
                    } else {
                        setLeftPinValue(Long.parseLong(s));
                    }

                }
            });
        } else if (id == mEditTextPriceTo.getId()) {
            showPopupAlertEditTextMenu(mEditTextPriceTo.getContext(), String.valueOf(mEditTextPriceTo.getHint()), String.valueOf(mEditTextPriceTo.getText()), new Action1<String>() {
                @Override
                public void call(String s) {
                    if (Long.valueOf(s) >= 1000) {
                        setRightPinValue(1000);
                    } else {
                        setRightPinValue(Long.parseLong(s));
                    }
                }
            });
        } else if (id == mButtonReset.getId()) {
            onClickReset();
        } else if (id == mButtonDelete.getId()) {
            attempDeleteService();
        }
    }

    private void attempDeleteService() {
//        mService.delete
        PopupMenuUtils.showConfirmationAlertMenu(getContext(), null, mLanguageData.serviceScreenConfirmDelete, mLanguageData.okLabel, mLanguageData.cancelLabel, new Action1<Boolean>() {
            @Override
            public void call(Boolean ok) {
                if (ok) {
                    requestDeleteService();
                }
            }
        });
    }

    private void requestDeleteService() {
        showLoading();
        RetrofitCallUtils.with(mService.deleteServiceMapping(DeleteServiceGson.with(mServiceId, mUserId)), new RetrofitCallUtils.RetrofitCallback<BaseResponse>() {
            @Override
            public void onDataSuccess(BaseResponse data) {
                dismissLoading();
                if (data.isSuccess()) {
                    dismiss();
                }
            }

            @Override
            public void onDataFailure(String failCause) {
                dismissLoading();
            }
        }).enqueue(getContext());
    }

    private void showPopupAlertEditTextMenu(Context context, String title, String defaultValue, final Action1<String> onConfirm) {
        if (null == context) {
            return;
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_edittext_in_alert_dialog, null);
        final EditText editText = (EditText) view.findViewById(R.id.edittext_in_alert_dialog);

        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (!TextUtils.isEmpty(defaultValue) && defaultValue.contains(",")) {
            defaultValue = String.valueOf(defaultValue.replaceAll(",", ""));
        }
        editText.setText(defaultValue);
//        editText.addTextChangedListener(new NumberTextWatcher(editText));
        editText.setFilters(new InputFilter[]{new InputFilterMinMax(0, Integer.MAX_VALUE, editText)});

        alert.setView(view);
        alert.setTitle(title);
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (onConfirm != null) {
                    onConfirm.call(editText.getText().toString());
                }

            }
        });
        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alert.create();
        if (null != dialog) {
            dialog.show();
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

    @Override
    public void saveServiceSuccess() {
        showLoadingDialog(false);
        showToast("Success");
        dismiss();
    }

    private void onClickReset() {
        if (null != mData && null != mData.getData()) {
            PloyerServiceDetailGson.Data data = mData.getData().cloneThis();
            data.setServiceNameOthers("");
            data.setPriceMax(1000);
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

    private void setLeftPinValue(long min) {
        mRangeBar.setOnRangeBarChangeListener(null);
        int minValueToSet;
        if (min >= 1000) {
            minValueToSet = 1000;
        } else {
            minValueToSet = (int) min;
        }
        setText(mEditTextPriceFrom,String.valueOf(minValueToSet));

        if (minValueToSet > Integer.valueOf(mRangeBar.getRightPinValue())) {
            mRangeBar.setRangePinsByValue(Float.parseFloat(mRangeBar.getRightPinValue()), minValueToSet);
        } else {
            mRangeBar.setRangePinsByValue(minValueToSet, Float.parseFloat(mRangeBar.getRightPinValue()));
        }
        mRangeBar.setOnRangeBarChangeListener(mRangeBarListener);
    }

    private void setRightPinValue(long max) {
        mRangeBar.setOnRangeBarChangeListener(null);
        int maxValueToSet;
        if (max >= 1000) {
            maxValueToSet = 1000;
        } else {
            maxValueToSet = (int) max;
        }
        setText(mEditTextPriceTo,String.valueOf(maxValueToSet));
        mRangeBar.setRangePinsByValue(Float.parseFloat(mRangeBar.getLeftPinValue()), maxValueToSet);
        if (Integer.valueOf(mRangeBar.getLeftPinValue()) > maxValueToSet) {
            mRangeBar.setRangePinsByValue(maxValueToSet, Float.parseFloat(mRangeBar.getLeftPinValue()));
        } else {
            mRangeBar.setRangePinsByValue(Float.parseFloat(mRangeBar.getLeftPinValue()), maxValueToSet);
        }
        mRangeBar.setOnRangeBarChangeListener(mRangeBarListener);
    }

    private void bindDataToView(final PloyeeServiceDetailContract.ViewModel data) {
        if (null != data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(data.getDescription())) {
                        mButtonDelete.setVisibility(View.VISIBLE);
                    } else {
                        mButtonDelete.setVisibility(View.GONE);
                    }
                    mTextViewPricePerHour.setText(data.getPriceUnit());
                    mEditTextPriceFrom.setHint(mLanguageData.serviceScreenFrom + " (" + mLanguageData.currencyLabel + ")");
                    mEditTextPriceTo.setHint(mLanguageData.serviceScreenTo + " (" + mLanguageData.currencyLabel + ")");
                    mEditTextPriceFrom.setFloatingLabelText(mLanguageData.serviceScreenFrom + " (" + mLanguageData.currencyLabel + ")");
                    mEditTextPriceTo.setFloatingLabelText(mLanguageData.serviceScreenTo + " (" + mLanguageData.currencyLabel + ")");

                    setText(mEditTextDescription,data.getDescription());
                    setText(mEditTextCertificate,data.getCertificate());
                    setText(mEditTextEquipmentNeeded,data.getEquipmentNeeded());

                    setLeftPinValue(data.getPriceMin());
                    setRightPinValue(data.getPriceMax());
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
                        setText(mEditTextOthers,data.getServiceOthersName());

                    } else {
                        mEditTextOthers.setVisibility(View.GONE);
                    }
                }
            });

        }
    }


    private PostSavePloyerServiceDetailGson gatheredData() {
        PostSavePloyerServiceDetailGson data = null;
        if (null != mData && null != mData.getData() && null != mAdapter) {
            data = new PostSavePloyerServiceDetailGson(mData.getData().cloneThis());
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

    private void attempUpdateService() {
        boolean canRequest = true;

        if (TextUtils.isEmpty(extractAndCheckError(mEditTextDescription))) {
            canRequest = false;
        }

        if (mServiceId == -1 && TextUtils.isEmpty(extractAndCheckError(mEditTextOthers))) {
            canRequest = false;
        }

        if (null != mData && null != mData.getData() && null != mData.getData().getSubServices() && !mData.getData().getSubServices().isEmpty() && gatheredData().getSubServiceLv2IdList().size() == 0) {
            canRequest = false;
            PopupMenuUtils.showConfirmationAlertMenu(getContext(), null, mLanguageData.serviceScreenAlert, mLanguageData.okLabel, null, null);
        }


        if (canRequest) {
            requestUpdateServiceDetail();
        }


    }

    private String extractAndCheckError(EditText editText) {
        String result = extractString(editText);
        if (TextUtils.isEmpty(result)) {
            editText.setError(LThis_field_is_required);
            editText.requestFocus();
        } else {
            editText.setError(null);
        }
        return result;
    }

    private void requestUpdateServiceDetail() {
        mPresenter.requestSaveServiceDetail(gatheredData());
    }


}
